package com.despani.x2.core.xusers.beans.domains;

import com.despani.x2.core.serializer.CustomAuthorityDeserializer;
import com.despani.x2.core.beans.base.ADespaniDisplayObject;
import com.despani.x2.core.beans.enums.DespContentTypes;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
@Data
@AllArgsConstructor

    public class DespaniUser extends ADespaniDisplayObject implements UserDetails, Serializable, Principal {


    private String firstName;
    private String lastName;
    private String username;
    private String password;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private DespUserProfile profile;

    @Getter @Setter
    private Map<String,List<DespaniUser>> friends;

    @Getter @Setter
    private boolean locked;

    @Getter @Setter
    private boolean enabled;

    private Collection<DespRole> roles;

    @Getter @Setter
    private Map<String, Object> attributes;



    public DespaniUser() {
        profile = new DespUserProfile();
        this.roles = new ArrayList<>();
    }




    public static DespaniUser buildDespaniUserForRegistration(Map<String, ?> user){

        DespaniUser newUser = new DespaniUser();
        newUser.setFirstName((String) user.get("firstName"));
        newUser.setLastName((String) user.get("lastName"));
        newUser.setEmail((String) user.get("email"));
        newUser.setUsername((String) user.get("username"));
        newUser.setPassword("NA");
        return newUser;
    }








    //comprenhanceable

//
//    public DespaniUser(Set<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String acessToken) {
//
//        if (!attributes.containsKey(nameAttributeKey)) {
//            throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
//        } else {
//            this.authorities = Collections.unmodifiableSet(new LinkedHashSet(this.sortAuthorities(authorities)));
//            this.attributes = Collections.unmodifiableMap(new LinkedHashMap(attributes));
//            this.nameAttributeKey = nameAttributeKey;
//            this.accessToken = acessToken;
//        }
//    }



    public boolean hasRole(DespRole role) {
        if(role==null) return false;
        return this.roles.contains(role);
    }




    public String toListOfRoles(){
        List<String> listOfRoles = new ArrayList<>();

        this.getRoles().stream().forEach(userRole->listOfRoles.add(userRole.getRole()));

        return listOfRoles.toString().replace("[", "").replace("]", "");

    }



    @Override
    @JsonDeserialize(using = CustomAuthorityDeserializer.class)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


    public Collection<DespRole> getRoles() {
        return roles;
    }

    public void setRoles(Collection<DespRole> roles) {
        this.roles = roles;
    }







    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setPassword(String password) {
        this.password = password;
    }




    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    @Override
    public String toString() {
        return "DespaniUser{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roles=" + roles +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", oid=" + oid +
                '}';
    }


    @Override
    public DespContentTypes getContentType() {
        return DespContentTypes.USER;
    }

    @Override
    public String getName() {
        return this.getFirstName()+ " "+this.getLastName();
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }



}
