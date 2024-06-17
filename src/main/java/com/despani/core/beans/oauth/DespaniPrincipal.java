package com.despani.core.beans.oauth;

import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.services.DespaniOauth2UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class DespaniPrincipal implements OAuth2User, UserDetails, CredentialsContainer,Serializable {
    private static final long serialVersionUID = 500L;
    private  Map<String, Object> attributes;

    private final String accessToken;

    public void setAttributes(Map<String,Object> attr){
        this.attributes = attr;
    }

    @Getter
    protected DespaniUser user;

        public DespaniUser getUser() {
                return user;
            }

            public void setUser(DespaniUser user) {
                this.user = user;
            }

    public String getAccessToken() {
        return accessToken;
    }

    public static DespaniPrincipal buildDespaniPrincipleForClient(Set<GrantedAuthority> authorities,
                                                          Map<String, ?> attributes,
                                                          String acessToken) {


        DespaniUser user = new DespaniUser();
        DespaniPrincipal zis = new DespaniPrincipal(attributes,acessToken,user);

        Assert.notEmpty(authorities, "authorities cannot be empty");
        Assert.notEmpty(attributes, "attributes cannot be empty");


        Map<String, ?> principle = (Map<String, ?>) attributes.get("principal");
        Map<String, ?> uzer = (Map<String, ?>) principle.get("user");

//            "firstName" -> "shalikia"
//            "lastName" -> "desp"
//            "user_name" -> "shalvasteph@gmail.com"
//            "scope" -> {ArrayList@12038}  size = 1
//            "updatedOn" -> {Long@12040} 1535132840000
//            "userName" -> "shalvasteph@gmail.com"
//            "exp" -> {Integer@12044} 1575851530
//            "createdOn" -> {Long@12046} 1535132840000


            try {


                user.setOid((Integer) uzer.get("oid"));
                user.setCreatedByOid((Integer)  uzer.get("createdByOid"));
                user.setUpdatedByOid((Integer)  uzer.get("updatedByOid"));
                user.setLocked((Boolean) uzer.get("locked"));
//                user.setRoles((Collection<DespRole>) uzer.get("authorities"));
                user.setEnabled((Boolean) uzer.get("enabled"));
                user.setUsername((String) uzer.get("username"));
//                user.setac((Integer) uzer.get("accountNonExpired"));
//                user.setOid((Integer) uzer.get("accountNonLocked"));
//                user.setOid((Integer) uzer.get("credentialsNonExpired"));
                user.setFirstName((String) uzer.get("firstName"));
                user.setLastName((String) uzer.get("lastName"));
//                user.setUsername((String) uzer.get("user_name"));


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
                format.setTimeZone(TimeZone.getTimeZone("GMT"));
                Date createdOn = format.parse((String)uzer.get("createdOn"));
                Date updatedOn = format.parse((String)uzer.get("createdOn"));
                user.setCreatedOn(createdOn);
                user.setUpdatedOn(updatedOn);
//                Object createdOn = uzer.get("createdOn");


            } catch (Exception e) {
                e.printStackTrace();
                log.error(e.getMessage());
                e.printStackTrace();
            }

             user.setRoles(Collections.unmodifiableSet(new LinkedHashSet(zis.sortAuthorities(authorities))));
            return zis;
    }


    public DespaniPrincipal(Map<String, ?> attributes,String acessToken, DespaniUser user) {
        this.attributes = Collections.unmodifiableMap(new LinkedHashMap(attributes));
        this.accessToken = acessToken;
        this.user = user;
    }

    public DespaniPrincipal(Map<String, Object> attributes, DespaniUser user) {
        this(attributes,null, user);
    }

    public String getName() {
//        return this.getAttributes().get(this.nameAttributeKey).toString();
        return this.getUsername();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getUser().getAuthorities();
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.user.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.user.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.user.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    private Set<GrantedAuthority> sortAuthorities(Set<GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }


//    @Override
//    public boolean implies(Subject subject) {
//        return false;
//    }


    @Override
    public void eraseCredentials() {
        this.getUser().setPassword("NA");
    }




    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            DespaniPrincipal that = (DespaniPrincipal)obj;
            return (this.getUser().getOid().equals(that.getUser().getOid()));
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.getUser().getOid().hashCode();
        result = 31 * result + this.getAuthorities().hashCode();
        result = 31 * result + this.getAttributes().hashCode();
        return result;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: [");
        sb.append(this.getName());
        sb.append("], Granted Authorities: [");
        sb.append((String)this.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(", ")));
        sb.append("], User Attributes: [");
        sb.append((String)this.getAttributes().entrySet().stream().map((e) -> {
            return (String)e.getKey() + "=" + e.getValue();
        }).collect(Collectors.joining(", ")));
        sb.append("]");
        return sb.toString();
    }
}
