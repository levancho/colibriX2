package com.despani.core.services.rest;

import com.despani.core.beans.domains.DespRole;
import com.despani.core.beans.domains.DespaniUser;
import com.despani.core.config.beans.DespaniConfigProperties;
import com.despani.core.exception.DespUserAlreadyExistsException;
import com.despani.core.exception.DespUserInvalidPasswordException;
import com.despani.core.exception.DespUserNotFoundException;
import com.despani.core.exceptions.DespRuntimeException;
import com.despani.core.managers.base.IDespSecurityManager;
import com.despani.core.platform.interfaces.IUserService;
import com.despani.core.services.AUserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@ConditionalOnProperty(
        value="despani.app.ssoconf.enabled",
        havingValue = "true",
        matchIfMissing = true)
public class RestOauthUserService  extends AUserService implements IUserService {



    @Autowired
    IDespSecurityManager despSecurityManager;

    @Autowired
    DespaniConfigProperties props;

//    public void editUser(DespaniUser user) throws URISyntaxException {
//        String token = despaniSecurity.getSecurityToken();
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        String ur = "http://localhost:8092/auth/api/user/me";
//        URI uri = new URI(ur);
//
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        headers.set("Authorization","Bearer "+token);
//        headers.set("Accept","application/json");
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//
//        ResponseEntity<String> respEntity = null;
//        String result = null;
//        try{
//            respEntity = restTemplate.exchange(uri, HttpMethod.GET,entity, String.class);
//
//
//            if(respEntity.getStatusCode() == HttpStatus.OK){
//                result = respEntity.getBody();
//            }
//        }catch(Exception e){
//            //TODO crate specific exception if needed
//            System.out.println(e);
//        }
//
//        System.out.println(result);
//
//    }

 
    @Override
    public Integer editUser(DespaniUser user)   {
        String token = despSecurityManager.getSecurityToken();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        String ur = props.getSsoconf().getServerUrl() + "/api/admin/user/edit";
        URI uri = null;
        try {
            uri = new URI(ur);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new DespRuntimeException("invalid url ");
        }

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.set("Authorization","Bearer "+token);
        headers.set("Accept","application/json");

        HttpEntity<DespaniUser> userEntity=
                new HttpEntity<DespaniUser>(user, headers);

        ResponseEntity<Integer> respEntity = null;
        Integer result = null;

        try{
            respEntity = restTemplate.exchange(uri, HttpMethod.POST,userEntity, Integer.class);

            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }

        System.out.println(result);

        return result;

    }

    @Override
    public List<DespaniUser>getPagedUsers(int limit, int offset){

//        String token = DespSecurityManager.getSecurityToken();

        RestTemplate restTemplate = new RestTemplate();
        // TODO move base url to property file
        String url = props.getSsoconf().getServerUrl() + "/api/admin/user/all/{limit}/{offset}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization","Bearer "+despSecurityManager.getSecurityToken());
        headers.set("Accept","application/json");


        HttpEntity<?> entity = new HttpEntity<>(headers);
        List<DespaniUser> result = null;
        ResponseEntity<List<DespaniUser>> respEntity = null;


        Map<String,Integer> params = new HashMap<>();
        params.put("limit",limit);
        params.put("offset",offset);
        try{
            respEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<List<DespaniUser>>() {},params);



            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }

        return result;
    }


    @Override
    public DespaniUser getUserByOid(int oid)  {



        RestTemplate restTemplate = new RestTemplate();
        // TODO move base url to property file
        String url = props.getSsoconf().getServerUrl() + "/api/admin/user/oid/{oid}";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization","Bearer "+despSecurityManager.getSecurityToken());
        headers.set("Accept","application/json");


        HttpEntity<?> entity = new HttpEntity<>(headers);
        DespaniUser result = null;
        ResponseEntity<DespaniUser> respEntity = null;
        try{
            respEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    DespaniUser.class,oid);



            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }

        return result;

    }

    @Override
    public DespaniUser getUserByEmail(String email) {

        // TODO implement
        return null;
    }


    @Override
    public List<DespRole> getAllRoles() {

        RestTemplate restTemplate = new RestTemplate();
        String url = props.getSsoconf().getServerUrl() + "/api/admin/role/all";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization","Bearer "+despSecurityManager.getSecurityToken());
//        headers.set("Accept","application/json");


        HttpEntity<?> entity = new HttpEntity<>(headers);
        List<DespRole> result = null;
        ResponseEntity<List<DespRole>> respEntity = null;
        try{
            respEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<DespRole>>() {}
                    );
            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }

        return result;

    }



    @Override
    public boolean updateRolesForUser(int oid, List<Integer> roleIds)   {

        String token = despSecurityManager.getSecurityToken();

        RestTemplate restTemplate = new RestTemplate();
        // TODO move base url to property file
        String url = props.getSsoconf().getServerUrl() + "/api/admin/user/edit/role";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Authorization","Bearer "+token);
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String, Object> data = new HashMap<>();
        data.put("oid",oid);
        data.put("roleIds",roleIds);


        HttpEntity<?> entity = new HttpEntity<>(data,headers);
        String result = null;
        ResponseEntity<String> respEntity = null;


        try{
            respEntity = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    String.class,oid);



            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
            return false;
        }

        return true;

    }

    @Override
    public DespaniUser createUser(DespaniUser despaniUser) throws DespUserNotFoundException, DespUserInvalidPasswordException, DespUserAlreadyExistsException {
        //TODO
        return null;
    }

    @Override
    public int countUsers() {
        // /count/users

        String token = despSecurityManager.getSecurityToken();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // TODO move base url to property file
        String url =  props.getSsoconf().getServerUrl() +"/api/admin/user/count/users";


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.set("Authorization","Bearer "+token);
        headers.set("Accept","application/json");

        HttpEntity userEntity=
                new HttpEntity<>( headers);

        ResponseEntity<Integer> respEntity = null;
        Integer result = null;

        try{
            respEntity = restTemplate.exchange(url, HttpMethod.GET,userEntity, Integer.class);

            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }
        return result;
    }



    @Override
    public void lockUser(Integer userOid, Boolean lock) {
        ///{userOid}/lock/{lock}"

        String token = despSecurityManager.getSecurityToken();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // TODO move base url to property file
        String url =  props.getSsoconf().getServerUrl() +"/api/admin/user/{userOid}/lock/{lock}";


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.set("Authorization","Bearer "+token);
        headers.set("Accept","application/json");

        HttpEntity userEntity=
                new HttpEntity<>( headers);

        ResponseEntity<String> respEntity = null;
        String result = null;

        try{
            respEntity = restTemplate.exchange(url, HttpMethod.GET,userEntity, String.class,userOid,lock);

            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @Override
    public void enableUser(Integer userOid, Boolean enable) {
///{userOid}/enable/{enable}
        String token = despSecurityManager.getSecurityToken();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // TODO move base url to property file
        String url =  props.getSsoconf().getServerUrl() +"/api/admin/user/{userOid}/enable/{enable}";


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.set("Authorization","Bearer "+token);
        headers.set("Accept","application/json");

        HttpEntity userEntity=
                new HttpEntity<>( headers);

        ResponseEntity<String> respEntity = null;
        String result = null;

        try{
            respEntity = restTemplate.exchange(url, HttpMethod.GET,userEntity, String.class,userOid,enable);

            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }
    }

    @SneakyThrows
    public String updateRole(DespRole despRole)
    {

        String token = despSecurityManager.getSecurityToken();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // TODO move base url to property file
        String url =  props.getSsoconf().getServerUrl() +"/auth/api/admin/role/edit";
        URI uri = new URI(url);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.set("Authorization","Bearer "+token);
        headers.set("Accept","application/json");

        HttpEntity<DespRole> userEntity=
                new HttpEntity<>(despRole, headers);

        ResponseEntity<String> respEntity = null;
        String result = null;

        try{
            respEntity = restTemplate.exchange(uri, HttpMethod.POST,userEntity, String.class);

            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }


        return result;

    }


    public String createRole(DespRole despRole)  {

//        String token = despSecurityManager.getDespOAuth2Token();
        String token = despSecurityManager.getSecurityToken();


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        // TODO move base url to property file
        String url =  props.getSsoconf().getServerUrl() +"/api/admin/role/create";


        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        headers.set("Authorization","Bearer "+token);
        headers.set("Accept","application/json");

        HttpEntity<DespRole> userEntity=
                new HttpEntity<>(despRole, headers);

        ResponseEntity<String> respEntity = null;
        String result = null;

        try{
            respEntity = restTemplate.exchange(url, HttpMethod.POST,userEntity, String.class);

            if(respEntity.getStatusCode() == HttpStatus.OK){
                result = respEntity.getBody();
            }
        }catch(Exception e){
            //TODO crate specific exception if needed
            e.printStackTrace();
            System.out.println(e);
        }


        return result;
    }
}
