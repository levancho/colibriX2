package com.despani.x2.core.xusers.services;

import  com.despani.x2.core.xusers.beans.domains.DespaniUser;
import com.despani.x2.core.xusers.beans.oauth.DespaniPrincipal;
import com.despani.x2.core.xusers.mybatis.mappers.IUserMapper;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class AUserService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    protected IUserMapper userMapper;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        DespaniUser user = this.getDespaniUser(username);
        if(user == null){
            throw new UsernameNotFoundException("User can't be found");
        }
        Map<String, Object> attributes = new HashMap();
        attributes.put("user_name",username);
        DespaniPrincipal despaniPrincipal = new DespaniPrincipal(
                attributes,  user);
        return despaniPrincipal;
    }

    @Transactional
    public DespaniUser getDespaniUser(String username){
        DespaniUser despaniUser = null;
        String SIMPLE_EMEIL_REGEXP = "^(.+)@(.+)$";
        EmailValidator validator = EmailValidator.getInstance();

        Pattern pattern = Pattern.compile(SIMPLE_EMEIL_REGEXP);
        boolean isEmail=false;

        if(pattern.matcher(username).matches()){
            isEmail=true;
            despaniUser = userMapper.getUserByUsernameEmail(username,"email");
        }else{
            despaniUser = userMapper.getUserByUsernameEmail(username,null);
        }


        return despaniUser;
    }
}
