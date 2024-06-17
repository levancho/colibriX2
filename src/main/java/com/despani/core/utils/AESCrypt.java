  package com.despani.core.utils;


import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;

  public class AESCrypt {

      private static final byte[] SALT = {
          (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
          (byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
      };
      private static final int ITERATION_COUNT = 65536;
      private static final int KEY_LENGTH = 128;
      private Cipher ecipher;
      private Cipher dcipher;

      public AESCrypt(String passPhrase) throws Exception {

          if(passPhrase.length() < 16){
              int i = 16-passPhrase.length();
              while(i > 0){
                  passPhrase += "0";
                  i--;
              }
          }

          SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
          KeySpec spec = new PBEKeySpec(passPhrase.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
          SecretKey tmp = factory.generateSecret(spec);
          SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

          ecipher = Cipher.getInstance("AES");
          ecipher.init(Cipher.ENCRYPT_MODE, secret);

          dcipher = Cipher.getInstance("AES");
          /* byte[] iv = ecipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV(); */
          dcipher.init(Cipher.DECRYPT_MODE, secret); //, new IvParameterSpec(iv));
      }

      public String encrypt(String encrypt) throws Exception {
          byte[] bytes = encrypt.getBytes("UTF8");
          byte[] encrypted = encrypt(bytes);
          return new String(Base64.encodeBase64(encrypted),"UTF8");
      }

      public byte[] encrypt(byte[] plain) throws Exception {
          return ecipher.doFinal(plain);
      }

      public String decrypt(String encrypt) throws Exception {
          byte[] bytes = Base64.decodeBase64(encrypt);
          byte[] decrypted = decrypt(bytes);
          return new String(decrypted, "UTF8");
      }

      public byte[] decrypt(byte[] encrypt) throws Exception {
          return dcipher.doFinal(encrypt);
      }
  }
