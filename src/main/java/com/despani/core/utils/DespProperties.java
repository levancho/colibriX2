/*
 * Copyright (C) 2004-2008 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.despani.core.utils;

import com.despani.core.beans.enums.DespPropertyType;
import com.despani.core.exceptions.DespRuntimeException;
import com.despani.core.mybatis.mappers.IPropertiesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Retrieves and stores Jive properties. Properties are stored in the database.
 *
 * @author Matt Tucker
 */


public class DespProperties implements Map<String, DespPropertyX2> {

    private static final Logger Log = LoggerFactory.getLogger(DespProperties.class);




    private static DespProperties instance = null;

    // The map of property keys to their values
    private Map<String, DespPropertyX2> properties;
    // The map of property keys to a boolean indicating if they are encrypted or not
    private Map<String, Boolean> encrypted;



    private Map<String, List<DespPropertyX2>> propertiesBySection = new ConcurrentHashMap<>();
    private Map<DespPropertyType, List<DespPropertyX2>> propertiesByType = new ConcurrentHashMap<>();
    /**
     * Returns a singleton instance of JiveProperties.
     *
     * @return an instance of JiveProperties.
     */
    public synchronized static DespProperties getInstance() {
        if (instance == null) {
            DespProperties props = new DespProperties();
//            props.init();
            instance = props;
        }
        return instance;
    }

     @Autowired
    private IPropertiesMapper propMapper;
    private DespProperties() {

//        propMapper=  SpringContext.getBean(IPropertiesMapper.class);
    }

    /**
     * For internal use only. This method allows for the reloading of all properties from the
     * values in the database. This is required since it's quite possible during the setup
     * process that a database connection will not be available till after this class is
     * initialized. Thus, if there are existing properties in the database we will want to reload
     * this class after the setup process has been completed.
     */
    @PostConstruct
    public void init() {
        if (properties == null) {
            properties = new ConcurrentHashMap<>();
            encrypted = new ConcurrentHashMap<>();
        }
        else {
            properties.clear();
            encrypted.clear();
        }

        loadProperties();
    }

    @Override
    public int size() {
        return properties.size();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return properties.containsValue(value);
    }

    @Override
    public Collection<DespPropertyX2> values() {
        return Collections.unmodifiableCollection(properties.values());
    }

    @Override
    public void putAll(Map<? extends String, ? extends DespPropertyX2> t) {
        for (Entry<? extends String, ? extends DespPropertyX2> entry : t.entrySet() ) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public Set<Entry<String, DespPropertyX2>> entrySet() {
        return Collections.unmodifiableSet(properties.entrySet());
    }

    @Override
    public Set<String> keySet() {
        return Collections.unmodifiableSet(properties.keySet());
    }

    @Override
    public DespPropertyX2 get(Object key) {
        return properties.get(key);
    }

    /**
     * Indicates the encryption status for the given property.
     * 
     * @param name
     *            The name of the property
     * @return {@code true} if the property exists and is encrypted, otherwise {@code false}
     */
    boolean isEncrypted(final String name) {
        if (name == null) {
            return false;
        }
        final Boolean isEncrypted = encrypted.get(name);
        return isEncrypted != null && isEncrypted;
    }

    /**
     * Set the encryption status for the given property.
     *
     * @param name
     *            The name of the property
     * @param encrypt
     *            True to encrypt the property, false to decrypt
     * @return {@code true} if the property's encryption status changed, otherwise {@code false}
     */
    boolean setPropertyEncrypted(String name, boolean encrypt) {
        final boolean encryptionWasChanged = name != null && properties.containsKey(name) && isEncrypted(name) != encrypt;
        if (encryptionWasChanged) {
            final DespPropertyX2 value = get(name);
            put(name, value, encrypt);
        }
        return encryptionWasChanged;
    }
    
    /**
     * Return all children property names of a parent property as a Collection
     * of String objects. For example, given the properties {@code X.Y.A},
     * {@code X.Y.B}, and {@code X.Y.C}, then the child properties of
     * {@code X.Y} are {@code X.Y.A}, {@code X.Y.B}, and {@code X.Y.C}. The method
     * is not recursive; ie, it does not return children of children.
     *
     * @param parentKey the name of the parent property.
     * @return all child property names for the given parent.
     */
    public Collection<String> getChildrenNames(String parentKey) {
        Collection<String> results = new HashSet<>();
        for (String key : properties.keySet()) {
            if (key.startsWith(parentKey + ".")) {
                if (key.equals(parentKey)) {
                    continue;
                }
                int dotIndex = key.indexOf(".", parentKey.length()+1);
                if (dotIndex < 1) {
                    if (!results.contains(key)) {
                        results.add(key);
                    }
                }
                else {
                    String name = parentKey + key.substring(parentKey.length(), dotIndex);
                    results.add(name);
                }
            }
        }
        return results;
    }

    /**
     * Returns all property names as a Collection of String values.
     *
     * @return all property names.
     */
    public Collection<String> getPropertyNames() {
        return properties.keySet();
    }

    @Override
    public DespPropertyX2 remove(Object key) {
        DespPropertyX2 value;
        synchronized (this) {
            value = properties.remove(key);
            // Also remove any children.
            Collection<String> propNames = getPropertyNames();
            for (String name : propNames) {
                if (name.startsWith((String)key)) {
                    properties.remove(name);
                }
            }
            deleteProperty((String)key);
        }

        // Generate event.
        Map<String, Object> params = Collections.emptyMap();
        PropertyEventDispatcher.dispatchEvent((String)key, PropertyEventDispatcher.EventType.property_deleted, params);

        return value;
    }



    /**
     * Saves a property, optionally encrypting it
     * 
     * @param key
     *            The name of the property
     * @param value
     *            The value of the property
     * @param isEncrypted
     *            {@code true} to encrypt the property, {@code true} to leave in plain text
     * @return The previous value associated with {@code key}, or {@code null} if there was no mapping for
     *         {@code key}.
     */
    public DespPropertyX2 put(String key, DespPropertyX2 value, boolean isEncrypted) {
        if (value == null) {
            // This is the same as deleting, so remove it.
            return remove(key);
        }
        if (key == null) {
            throw new NullPointerException("Key cannot be null. Key=" +
                    key + ", value=" + value);
        }
        if (key.endsWith(".")) {
            key = key.substring(0, key.length()-1);
        }
        key = key.trim();
        DespPropertyX2 result;
        synchronized (this) {
            if (properties.containsKey(key)) {
                updateProperty(key, value, isEncrypted);
            }
            else {
                insertProperty(key, value, isEncrypted);
            }

            result = properties.put(key, value);
            encrypted.put(key, isEncrypted);
            // We now know the database is correct - so we can remove the entry from security.conf

        }

        // Generate event.
        Map<String, Object> params = new HashMap<>();
        params.put("value", value);

        // Todo replace with spring event dispatcher.
        PropertyEventDispatcher.dispatchEvent(key, PropertyEventDispatcher.EventType.property_set, params);

        return result;
    }

    //todo beka
    public void put(DespPropertyX2 prop) {
        synchronized (this) {
            insertProperty(prop);
            properties.put(prop.getName(), prop);
            encrypted.put(prop.getName(), prop.isEncrypted());
            // We now know the database is correct - so we can remove the entry from security.conf

        }
        // Generate event.
        Map<String, Object> params = new HashMap<>();
        params.put("value", prop.getPropValue());
        PropertyEventDispatcher.dispatchEvent(prop.getName(), PropertyEventDispatcher.EventType.property_set, params);

    }

    @Override
    public DespPropertyX2 put(String key, DespPropertyX2 value) {
        return put(key, value, isEncrypted(key));
    }



    public DespPropertyX2 getProperty(String name, String defaultValue) {
        DespPropertyX2 value = properties.get(name);
        if (value != null) {
            return value;
        }
        else {
            return new DespPropertyX2(defaultValue);
        }
    }

    public boolean getBooleanProperty(String name) {
        DespPropertyX2 value = get(name);
        if (value != null) {
            return Boolean.valueOf(value.getPropValue());
        }
        else {
            throw new DespRuntimeException("Property not found: " + name);
        }
    }

     boolean getBooleanProperty(String name, boolean defaultValue) {
        DespPropertyX2 value = get(name);
        if (value != null) {
            return Boolean.parseBoolean(value.getPropValue());
        }
        else {
            return defaultValue;
        }
    }

    private void insertProperty(String name, DespPropertyX2 value, boolean isEncrypted) {
        Encryptor encryptor = getEncryptor(true);
            DespPropertyX2 prop = new DespPropertyX2();
            final String valueToSave;
            final String ivString;
            if (isEncrypted) {
                final byte[] iv = new byte[16];
                new SecureRandom().nextBytes(iv);
                ivString = java.util.Base64.getEncoder().encodeToString(iv);
                valueToSave = encryptor.encrypt(value.getPropValue(), iv);
            } else {
                ivString = null;
                valueToSave = value.getPropValue();
            }
            prop.setName(name);
            prop.setPropValue(valueToSave);
            prop.setEncrypted(isEncrypted);
            prop.setIv(ivString);
            prop.setXtype(value.getXtype());
            prop.setXsection(value.getXsection());
            propMapper.insertProperty(prop);

    }

    //todo beka
    private void insertProperty(DespPropertyX2 prop) {
        Encryptor encryptor = getEncryptor(true);
        final String valueToSave;
        final String ivString;
        if (prop.isEncrypted()) {
            final byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            ivString = java.util.Base64.getEncoder().encodeToString(iv);
            valueToSave = encryptor.encrypt(prop.getPropValue(), iv);
            prop.setPropValue(valueToSave);
        } else {
            ivString = null;
        }
        prop.setIv(ivString);
        propMapper.insertProperty(prop);

    }

    private void updateProperty(String name, DespPropertyX2 value, boolean isEncrypted) {
        Encryptor encryptor = getEncryptor(true);
        Connection con = null;
        PreparedStatement pstmt = null;
            DespPropertyX2 prop = new DespPropertyX2();
            final String valueToSave;
            final String ivString;
            if (isEncrypted) {
                final byte[] iv = new byte[16];
                new SecureRandom().nextBytes(iv);
                ivString = java.util.Base64.getEncoder().encodeToString(iv);
                valueToSave = encryptor.encrypt(value.getPropValue(), iv);
            } else {
                ivString = null;
                valueToSave = value.getPropValue();
            }
            prop.setName(name);
            prop.setPropValue(valueToSave);
            prop.setEncrypted(isEncrypted);
            prop.setIv(ivString);
            propMapper.updateProperty(prop);
    }

    // todo
    private void deleteProperty(String name) {
        propMapper.deleteProperty(name + "%");
    }

    private void loadProperties() {
        Encryptor encryptor = getEncryptor();
        try {
            List<DespPropertyX2> despProperties = propMapper.loadProperties();
            for (DespPropertyX2 _propx2 : despProperties) {


                DespPropertyX2 localProp=null;
                String name = _propx2.getName();
                String value = _propx2.getPropValue();
                String ivString = _propx2.getIv();
                DespPropertyType type = _propx2.getXtype();
                byte[] iv = null;
                if (ivString != null) {
                    try {
                        iv = java.util.Base64.getDecoder().decode(ivString);
                        if (iv.length != 16) {
                            Log.error("Unable to correctly decode iv from string " + ivString);
                            iv = null;
                        }
                    } catch (final IllegalArgumentException e) {
                        Log.error("Unable to decode iv from string " + ivString, e);
                    }
                }

                boolean isEncrypted = _propx2.isEncrypted();
                if (isEncrypted) {
                    try {
                        value = encryptor.decrypt(value, iv);
                    } catch (Exception ex) {
                        Log.error("Failed to load encrypted property value for " + name, ex);
                        value = null;
                    }
                }

                if (value != null) {

                    localProp= new DespPropertyX2(name, value, type, _propx2.getXsection(), isEncrypted, ivString);
                    properties.put(name, localProp);
                    encrypted.put(name, isEncrypted);
                }else {
                    throw new DespRuntimeException("Failed to load property value for " + name);
                }

                if(propertiesBySection.containsKey(_propx2.getXsection())) {
                    propertiesBySection.get(_propx2.getXsection()).add(localProp);
                }else {
                    List<DespPropertyX2> list = new ArrayList<>();
                    list.add(localProp);
                    propertiesBySection.put(_propx2.getXsection(), list);
                }

                if(propertiesByType.containsKey(_propx2.getXtype())) {
                    propertiesByType.get(_propx2.getXtype()).add(localProp);
                } else {
                    List<DespPropertyX2> list = new ArrayList<>();
                    list.add(localProp);
                    propertiesByType.put(_propx2.getXtype(), list);
                }

            }
        }
        catch (Exception e) {
            Log.error(e.getMessage(), e);
            throw new DespRuntimeException("Failed to load properties from the database");
        }

    }
    
    private Encryptor getEncryptor(boolean useNewEncryptor) {
        return DespGlobals.getPropertyEncryptor(useNewEncryptor);
    }
    
    private Encryptor getEncryptor() {
        return getEncryptor(false);
    }

    public List<DespPropertyX2> getPropertiesBySection(String section) {
        return propertiesBySection.get(section);
    }

    public List<DespPropertyX2> getPropertiesByType(DespPropertyType type) {
        return propertiesByType.get(type);
    }
}
