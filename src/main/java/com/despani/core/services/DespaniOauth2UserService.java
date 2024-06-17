package com.despani.core.services;

import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.beans.oauth.DespaniPrincipal;

import com.despani.core.exception.DespUserAlreadyExistsException;
import com.despani.core.services.rest.RestOauthUserService;
import com.despani.core.exception.DespUserInvalidPasswordException;
import com.despani.core.exception.DespUserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequestEntityConverter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@ConditionalOnProperty(
        value = {"despani.app.ssoconf.enabled"},
        havingValue = "true",
        matchIfMissing = true
)
public class DespaniOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    RestOauthUserService userServices;

    @Autowired
    RestOauthUserService restUserService;

    private static final String MISSING_USER_INFO_URI_ERROR_CODE = "missing_user_info_uri";
    private static final String MISSING_USER_NAME_ATTRIBUTE_ERROR_CODE = "missing_user_name_attribute";
    private static final String INVALID_USER_INFO_RESPONSE_ERROR_CODE = "invalid_user_info_response";
    private static final ParameterizedTypeReference<Map<String, Object>> PARAMETERIZED_RESPONSE_TYPE = new ParameterizedTypeReference<Map<String, Object>>() {

    };

    private Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter = new OAuth2UserRequestEntityConverter();
    private RestOperations restOperations;

    public DespaniOauth2UserService() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
        this.restOperations = restTemplate;
    }

    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        Assert.notNull(userRequest, "userRequest cannot be null");
        if (!StringUtils.hasText(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri())) {
            OAuth2Error oauth2Error = new OAuth2Error("missing_user_info_uri", "Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String)null);
            throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
        } else {
            String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
            if (!StringUtils.hasText(userNameAttributeName)) {
                OAuth2Error oauth2Error = new OAuth2Error("missing_user_name_attribute", "Missing required \"user name\" attribute name in UserInfoEndpoint for Client Registration: " + userRequest.getClientRegistration().getRegistrationId(), (String)null);
                throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString());
            } else {
                RequestEntity request = (RequestEntity)this.requestEntityConverter.convert(userRequest);

                ResponseEntity response;
                OAuth2Error oauth2Error;
                try {
                    response = this.restOperations.exchange(request, PARAMETERIZED_RESPONSE_TYPE);
                } catch (OAuth2AuthorizationException var10) {
                    oauth2Error = var10.getError();
                    StringBuilder errorDetails = new StringBuilder();
                    errorDetails.append("Error details: [");
                    errorDetails.append("UserInfo Uri: ").append(userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri());
                    errorDetails.append(", Error Code: ").append(oauth2Error.getErrorCode());
                    if (oauth2Error.getDescription() != null) {
                        errorDetails.append(", Error Description: ").append(oauth2Error.getDescription());
                    }

                    errorDetails.append("]");
                    oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + errorDetails.toString(), (String)null);
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var10);
                } catch (RestClientException var11) {
                    oauth2Error = new OAuth2Error("invalid_user_info_response", "An error occurred while attempting to retrieve the UserInfo Resource: " + var11.getMessage(), (String)null);
                    throw new OAuth2AuthenticationException(oauth2Error, oauth2Error.toString(), var11);
                }

                Map<String, ?> userAttributes = (Map)response.getBody();
                Set<GrantedAuthority> authorities = new LinkedHashSet();


                List<HashMap<String,?>> authoritiesY = (List<HashMap<String,?>>) userAttributes.get("authorities");

                for (HashMap<String,?> m : authoritiesY) {
//                    authorities.add(new OAuth2UserAuthority(m.get("authority"),userAttributes));
                    DespRole despRole = new DespRole((String)m.get("authority"));
                    despRole.setOid((Integer)m.get("oid"));
                    // TODO add next 4 fields in db roles table
//                    despRole.setCreatedOn(m.get("createdOn"));
//                    despRole.setUpdatedOn(m.get("updatedOn"));
//                    despRole.setCreatedByOid((Integer)m.get("createdByOid"));
//                    despRole.setUpdatedByOid((Integer)m.get("updatedByOid"));
                    despRole.setWeight((Integer)m.get("weight"));
                    despRole.setRoleDescription((String)m.get("roleDescription"));

                    authorities.add(despRole);
                }



                // Load app specific user roles;
                Map<String, ?> principal = (Map<String, ?>) userAttributes.get("principal");
                Map<String, ?> user = (Map<String, ?>) principal.get("user");
                DespaniPrincipal appPrincipal=null;
                try{
                    appPrincipal = (DespaniPrincipal)userServices.loadUserByUsername((String)user.get("email"));
                }catch(UsernameNotFoundException unfe){

                    DespaniUser newUser = DespaniUser.buildDespaniUserForRegistration(user);
                    try {
                        newUser = userServices.createUser(newUser);
                    } catch (DespUserNotFoundException e) {
                        e.printStackTrace();
                    } catch (DespUserInvalidPasswordException despUserInvalidPassword) {
                        despUserInvalidPassword.printStackTrace();
                    } catch (DespUserAlreadyExistsException e) {
                        // never be reached
                        e.printStackTrace();
                    }

                    // Adding app specific roles to global authorities
                    authorities.addAll(newUser.getRoles());
                    String tokenVal = userRequest.getAccessToken().getTokenValue();
                    return DespaniPrincipal.buildDespaniPrincipleForClient(authorities,userAttributes,tokenVal);
                }
                //
                DespaniUser appUser = appPrincipal.getUser();
                ArrayList<DespRole> appUserRoles = (ArrayList<DespRole>) appUser.getRoles();

                // Adding app specific roles to global authorities
                authorities.addAll(appUserRoles);
                String tokenVal = userRequest.getAccessToken().getTokenValue();
                return DespaniPrincipal.buildDespaniPrincipleForClient(authorities,userAttributes,tokenVal);
            }
        }
    }

    public final void setRequestEntityConverter(Converter<OAuth2UserRequest, RequestEntity<?>> requestEntityConverter) {
        Assert.notNull(requestEntityConverter, "requestEntityConverter cannot be null");
        this.requestEntityConverter = requestEntityConverter;
    }

    public final void setRestOperations(RestOperations restOperations) {
        Assert.notNull(restOperations, "restOperations cannot be null");
        this.restOperations = restOperations;
    }
}
