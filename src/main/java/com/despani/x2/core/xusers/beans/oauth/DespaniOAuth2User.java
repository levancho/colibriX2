package com.despani.x2.core.xusers.beans.oauth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class DespaniOAuth2User implements OAuth2User, Serializable {
    private static final long serialVersionUID = 500L;
    private final Set<GrantedAuthority> authorities;
    private final Map<String, Object> attributes;
    private final String nameAttributeKey;
    private final String accessToken;

    public String getAccessToken() {
        return accessToken;
    }



    public DespaniOAuth2User(Set<GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String acessToken) {
        Assert.notEmpty(authorities, "authorities cannot be empty");
        Assert.notEmpty(attributes, "attributes cannot be empty");
        Assert.hasText(nameAttributeKey, "nameAttributeKey cannot be empty");
        if (!attributes.containsKey(nameAttributeKey)) {
            throw new IllegalArgumentException("Missing attribute '" + nameAttributeKey + "' in attributes");
        } else {
            this.authorities = Collections.unmodifiableSet(new LinkedHashSet(this.sortAuthorities(authorities)));
            this.attributes = Collections.unmodifiableMap(new LinkedHashMap(attributes));
            this.nameAttributeKey = nameAttributeKey;
            this.accessToken = acessToken;
        }
    }


    public String getName() {
        return this.getAttributes().get(this.nameAttributeKey).toString();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    private Set<GrantedAuthority> sortAuthorities(Set<GrantedAuthority> authorities) {
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(Comparator.comparing(GrantedAuthority::getAuthority));
        sortedAuthorities.addAll(authorities);
        return sortedAuthorities;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj != null && this.getClass() == obj.getClass()) {
            org.springframework.security.oauth2.core.user.DefaultOAuth2User that = (org.springframework.security.oauth2.core.user.DefaultOAuth2User)obj;
            if (!this.getName().equals(that.getName())) {
                return false;
            } else {
                return !this.getAuthorities().equals(that.getAuthorities()) ? false : this.getAttributes().equals(that.getAttributes());
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.getName().hashCode();
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
