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

package com.despani.x2.core.utils;

import com.despani.x2.core.beans.enums.DespPropertyType;
import com.despani.x2.core.config.beans.DespaniConfigProperties;
import com.despani.x2.core.exceptions.base.DespRuntimeException;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class DespGlobals {

    public static Map<String, String> sitelangs = new HashMap<>();
    static {
        sitelangs.put("en_US","English");
        sitelangs.put("ka_GE","Georgia");
        init();

    }


    private static DespaniConfigProperties despConfig;
    private static void init() {
        // Load the properties from the database

        if(properties == null) {
            properties = SpringContext.getBean(DespProperties.class);

        }

        if(despConfig == null) {
            despConfig = SpringContext.getBean(DespaniConfigProperties.class);
        }
    }


    private static final Logger Log = LoggerFactory.getLogger(DespGlobals.class);
    private static final String ENCRYPTED_PROPERTY_NAME_PREFIX = "encrypt.";
    private static final String ENCRYPTION_ALGORITHM = ENCRYPTED_PROPERTY_NAME_PREFIX + "algorithm";
    private static final String ENCRYPTION_KEY_CURRENT = ENCRYPTED_PROPERTY_NAME_PREFIX + "key.current";
    private static final String ENCRYPTION_ALGORITHM_AES = "AES";
    private static final String ENCRYPTION_ALGORITHM_BLOWFISH = "Blowfish";
    private static DespPropertyX2 securityProperties ;
    private static DespProperties properties = null;
    private static Encryptor propertyEncryptor = null;
    private static Encryptor propertyEncryptorNew = null;
    private static String currentKey = null;


    public static boolean getPropertyAsBoolean(String name) {
        DespPropertyX2 ret = getProperty(name);
        return BooleanUtils.toBoolean(ret.getPropValue());
    }

    /**
     * Returns a Jive property.
     *
     * @param name the name of the property to return.
     * @return the property value specified by name.
     */
    public static DespPropertyX2 getProperty(String name) {
       init();
        DespPropertyX2 dbProp = properties.get(name);
        if (dbProp == null) {
           throw new DespRuntimeException("Property not found: " + name);
        }
        return dbProp;
    }


    public static String getPropertyValue(String name) {
        DespPropertyX2 property = getProperty(name);
        if (property == null) {
            throw new DespRuntimeException("Property not found: " + name);
        }

        return property.getPropValue();
    }

    /**
     * Returns a Jive property. If the specified property doesn't exist, the
     * {@code defaultValue} will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist.
     * @return the property value specified by name.
     */
    public static String getProperty(String name, String defaultValue) {
        if (properties == null) {
            properties=  SpringContext.getBean(DespProperties.class);
//            properties = DespProperties.getInstance();
        }
        DespPropertyX2 value = properties.get(name);
        if (value != null) {
            return value.getPropValue();
        } else {
            return defaultValue;
        }
    }




    /**
     * Returns an enum constant Jive property. If the specified property doesn't exist, or if it's value cannot be parsed
     * as an enum constant, the {@code defaultValue} will be returned.
     *
     * @param name         the name of the property to return.
     * @param enumType     the {@code Class} object of the enum type from which to return a constant.
     * @param defaultValue value returned if the property doesn't exist or it's value could not be parsed.
     * @param <E>          The enum type whose constant is to be returned.
     * @return the property value (as an enum constant) or {@code defaultValue}.
     */
    public static <E extends Enum<E>> E getEnumProperty(String name, Class<E> enumType, E defaultValue) {
        DespPropertyX2 value = getProperty(name);
        if (value != null) {
            try {
                return E.valueOf(enumType, value.getPropValue());
            } catch (IllegalArgumentException e) {
                // Ignore
            }
        }
        return defaultValue;
    }

    /**
     * Returns an integer value Jive property. If the specified property doesn't exist, the
     * {@code defaultValue} will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist or was not
     *                     a number.
     * @return the property value specified by name or {@code defaultValue}.
     */
    public static int getIntProperty(String name, int defaultValue) {
        DespPropertyX2 value = getProperty(name);
        if (value != null) {
            try {
                return Integer.parseInt(value.getPropValue());
            } catch (NumberFormatException nfe) {
                // Ignore.

            }
        }
        return defaultValue;
    }

    /**
     * Returns a long value Jive property. If the specified property doesn't exist, the
     * {@code defaultValue} will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist or was not
     *                     a number.
     * @return the property value specified by name or {@code defaultValue}.
     */
    public static long getLongProperty(String name, long defaultValue) {
        DespPropertyX2 value = getProperty(name);
        if (value != null) {
            try {
                return Long.parseLong(value.getPropValue());
            } catch (NumberFormatException nfe) {
                // Ignore.

            }
        }
        return defaultValue;
    }

    /**
     * Returns a boolean value Jive property.
     *
     * @param name the name of the property to return.
     * @return true if the property value exists and is set to {@code "true"} (ignoring case).
     * Otherwise {@code false} is returned.
     */
    public static boolean getBooleanProperty(String name) {
        DespPropertyX2 value = getProperty(name);
        if (value != null) {
            return Boolean.valueOf(value.getPropValue());
        } else {
            throw new DespRuntimeException("Property not found: " + name);
        }
    }

    /**
     * Returns a boolean value Jive property. If the property doesn't exist, the {@code defaultValue}
     * will be returned.
     * <p>
     * If the specified property can't be found, or if the value is not a number, the
     * {@code defaultValue} will be returned.
     *
     * @param name         the name of the property to return.
     * @param defaultValue value returned if the property doesn't exist.
     * @return true if the property value exists and is set to {@code "true"} (ignoring case).
     * Otherwise {@code false} is returned.
     */
    public static boolean getBooleanProperty(String name, boolean defaultValue) {
        DespPropertyX2 value = getProperty(name);
        if (value != null) {
            return Boolean.parseBoolean(value.getPropValue());
        } else {
            return defaultValue;
        }
    }






    public static boolean hasProperty(String key){
        if (properties == null) {
            properties = DespProperties.getInstance();
        }
        return properties.containsKey(key);
    }


    public static void setProperty(DespPropertyX2 prop) {
             properties.put(prop);
    }

    /**
     * Sets a Jive property. If the property doesn't already exists, a new
     * one will be created.
     *
     * @param name  the name of the property being set.
     * @param value the value of the property being set.
     */
    public static void setProperty(String name, DespPropertyX2 value) {
        setProperty(name, value, false);
    }

    public static void setProperty(String name, String value) {
        setProperty(name,new DespPropertyX2(value) , false);
    }


    /**
     * Sets a Jive property. If the property doesn't already exists, a new
     * one will be created.
     *
     * @param name    the name of the property being set.
     * @param value   the value of the property being set.
     * @param encrypt {@code true} to encrypt the property in the database, other {@code false}
     */
    public static void setProperty(String name, DespPropertyX2 value, boolean encrypt) {
        init();
        properties.put(name, value, encrypt);
    }



    /**
     * Deletes a Jive property. If the property doesn't exist, the method
     * does nothing. All children of the property will be deleted as well.
     *
     * @param name the name of the property to delete.
     */
    public static void deleteProperty(String name) {
        init();
        properties.remove(name);
    }





    /**
     * Flags certain properties as being sensitive, based on
     * property naming conventions. Values for matching property
     * names are hidden from the Openfire console.
     *
     * @param name The name of the property
     * @return True if the property is considered sensitive, otherwise false
     */
    public static boolean isPropertySensitive(String name) {

        return name != null && (
                name.toLowerCase().contains("passwd") ||
                        name.toLowerCase().contains("password") ||
                        name.toLowerCase().contains("cookiekey"));
    }



    /**
     * Determines whether a property is configured for encryption.
     *
     * @param name The name of the property
     * @return {@code true} if the property is stored using encryption, otherwise {@code false}
     */
    public static boolean isPropertyEncrypted(String name) {
        if (properties == null) {
            properties=  SpringContext.getBean(DespProperties.class);
//            properties = DespProperties.getInstance();
        }
        return properties.isEncrypted(name);
    }

    /**
     * Set the encryption status for the given property.
     *
     * @param name    The name of the property
     * @param encrypt True to encrypt the property, false to decrypt
     * @return True if the property's encryption status changed, otherwise false
     */
    public static boolean setPropertyEncrypted(String name, boolean encrypt) {
        if (properties == null) {
            properties=  SpringContext.getBean(DespProperties.class);
//            properties = DespProperties.getInstance();
        }
        return properties.setPropertyEncrypted(name, encrypt);
    }

    /**
     * Fetches the property encryptor.
     *
     * @param useNewEncryptor Should use the new encryptor
     * @return The property encryptor
     */
    public static Encryptor getPropertyEncryptor(boolean useNewEncryptor) {
        if (securityProperties == null) {
            loadSecurityProperties();
        }
        if (propertyEncryptor == null) {
            String algorithm = despConfig.getEncryptionType()!=null? despConfig.getEncryptionType() : ENCRYPTION_ALGORITHM_AES;
            propertyEncryptor = getEncryptor(algorithm, currentKey);
            propertyEncryptorNew = propertyEncryptor;
        }
        return useNewEncryptor ? propertyEncryptorNew : propertyEncryptor;
    }

    /**
     * Fetches the current property encryptor.
     *
     * @return The current property encryptor
     */
    public static Encryptor getPropertyEncryptor() {
        return getPropertyEncryptor(false);
    }



    /**
     * This method is called early during the setup process to
     * set a custom key for encrypting property values
     *
     * @param key the key used to encrypt properties
     */
    public static void setupPropertyEncryptionKey(String key) {
        // Get the old secret key and encryption type
        init();
        String oldAlg = despConfig.getEncryptionType()!=null? despConfig.getEncryptionType() : ENCRYPTION_ALGORITHM_AES;

        String oldKey = getCurrentKey();
        if ((StringUtils.isNotEmpty(oldKey) || propertyEncryptor != null) && StringUtils.isNotEmpty(key) && !key.equals(oldKey) && StringUtils.isNotEmpty(oldAlg)) {
            // update encrypted properties
            updateEncryptionProperties(oldAlg, key);
        }
        // Set the new key
//        securityProperties.setProperty(ENCRYPTION_KEY_CURRENT, new AesEncryptor().encrypt(key));
        currentKey = key == "" ? null : key;
        propertyEncryptorNew = getEncryptor(oldAlg, key);
        propertyEncryptor = propertyEncryptorNew;
    }

    /**
     * Get current encryptor key.
     */
    private static String getCurrentKey() {
        init();
        String encryptedKey = despConfig.getEncryptionType()!=null? despConfig.getEncryptionType() : ENCRYPTION_ALGORITHM_AES;
 ;
        String key = null;
        if (StringUtils.isNotEmpty(encryptedKey)) {
            key = new AesEncryptor().decrypt(encryptedKey);
        }
        return key;
    }

    /**
     * Get current encryptor according to alg and key.
     *
     * @param alg algorithm type
     * @param key encryptor key
     */
    public static Encryptor getEncryptor(String alg, String key) {
        Encryptor encryptor;
        if (ENCRYPTION_ALGORITHM_AES.equalsIgnoreCase(alg)) {
            encryptor = new AesEncryptor(key);
        } else {
            encryptor = new Blowfish(key);
        }
        return encryptor;
    }

    /**
     * Re-encrypted with a new key and new algorithm configuration
     *
     * @param newAlg new algorithm type
     * @param newKey new encryptor key
     */
    private static void updateEncryptionProperties(String newAlg, String newKey) {
       init();
        //create the new encryptor
        currentKey = newKey.isEmpty() ? null : newKey;
        propertyEncryptorNew = getEncryptor(newAlg, newKey);

        // Use new key to update configuration properties
        Iterator<Entry<String, DespPropertyX2>> iterator = properties.entrySet().iterator();
        Entry<String, DespPropertyX2> entry;
        String name;
        while (iterator.hasNext()) {
            entry = iterator.next();
            name = entry.getKey();
            // only need to update the encrypted ones
            if (isPropertyEncrypted(name)) {
                properties.put(name, entry.getValue());
            }
        }

        // Two encryptors are now the same
        propertyEncryptor = propertyEncryptorNew;
    }




    /**
     * Lazy-loads the security configuration properties.
     */
    private synchronized static void loadSecurityProperties() {

        if (securityProperties == null) {

                securityProperties = new DespPropertyX2();

        }
    }

    public static  List<DespPropertyX2> getPropertiesforSection(String section) {

        return properties.getPropertiesBySection(section);
    }

    public static List<DespPropertyX2> getPropertiesByType(DespPropertyType type) {

        return properties.getPropertiesByType(type);
    }

    public static void setProperties(Map<String, String> propertyMap) {
       init();
        Map<String, DespPropertyX2> convertedMap = propertyMap.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new DespPropertyX2(entry.getValue())
                ));

        properties.putAll(convertedMap);
    }




    public static void reset() {

        if (properties != null) {
            properties.init();
        }
    }
}
