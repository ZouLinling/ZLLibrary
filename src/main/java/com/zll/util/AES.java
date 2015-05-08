package com.zll.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.zll.logs.Log;

/**
 * 
 * @author zou
 * 
 */

public class AES {
	  public static String encrypt(String seed, String cleartext) throws Exception {      
//          byte[] rawKey = generateKey(seed.toCharArray());  
          byte[] rawKey = getRawKey(seed.getBytes());
          byte[] result = encrypt(rawKey, cleartext.getBytes());      
          return toHex(result);      
      }      
            
      public static String decrypt(String seed, String encrypted) throws Exception {      
//          byte[] rawKey = generateKey(seed.toCharArray());      
          byte[] rawKey = getRawKey(seed.getBytes());
          byte[] enc = toByte(encrypted);      
          byte[] result = decrypt(rawKey, enc);      
          return new String(result);      
      }      
            
      public static SecretKey generateKey() throws NoSuchAlgorithmException {
    	    // Generate a 256-bit key
    	    final int outputKeyLength = 256;

    	    SecureRandom secureRandom = new SecureRandom();
    	    // Do *not* seed secureRandom! Automatically seeded from system entropy.
    	    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    	    keyGenerator.init(outputKeyLength, secureRandom);
    	    SecretKey key = keyGenerator.generateKey();
    	    return key;
    	}
      
      public static byte[] generateKey(char[] passphraseOrPin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
  	    // Number of PBKDF2 hardening rounds to use. Larger values increase
  	    // computation time. You should select a value that causes computation
  	    // to take >100ms.
  	    final int iterations = 1000; 

  	    // Generate a 256-bit key
  	    final int outputKeyLength = 256;

  	    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
  	    KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt, iterations, outputKeyLength);
  	    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
  	    return secretKey.getEncoded();
  	}
      
      public static byte[] generateKey(char[] passphraseOrPin) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	    SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    	    final int iterations = 1000; 

      	    // Generate a 256-bit key
      	    final int outputKeyLength = 256;
      	    String salt = "1";
      	  KeySpec keySpec = new PBEKeySpec(passphraseOrPin, salt.getBytes(), iterations, outputKeyLength);
    	    SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
    	    return secretKey.getEncoded();
    	}
      
      private static byte[] getRawKey(byte[] seed) throws Exception {      
          KeyGenerator kgen = KeyGenerator.getInstance("AES");      
          SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");      
          sr.setSeed(seed);      
          kgen.init(192, sr); // 192 and 256 bits may not be available      
          SecretKey skey = kgen.generateKey();      
          byte[] raw = skey.getEncoded();      
          return raw;
      }      
       
            
      private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {      
          SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");      
          Cipher cipher = Cipher.getInstance("AES");      
          cipher.init(Cipher.ENCRYPT_MODE, skeySpec);      
          byte[] encrypted = cipher.doFinal(clear);      
          return encrypted;      
      }      
       
      private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {      
          SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");      
          Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");      
          cipher.init(Cipher.DECRYPT_MODE, skeySpec);      
          byte[] decrypted = cipher.doFinal(encrypted);      
          return decrypted;      
      }      
       
      public static String toHex(String txt) {      
          return toHex(txt.getBytes());      
      }      
      public static String fromHex(String hex) {      
          return new String(toByte(hex));      
      }      
            
      public static byte[] toByte(String hexString) {      
          int len = hexString.length()/2;      
          byte[] result = new byte[len];      
          for (int i = 0; i < len; i++)      
              result[i] = Integer.valueOf(hexString.substring(2*i, 2*i+2), 16).byteValue();      
          return result;      
      }      
       
      public static String toHex(byte[] buf) {      
          if (buf == null)      
              return "";      
          StringBuffer result = new StringBuffer(2*buf.length);
          for (int i = 0; i < buf.length; i++) {      
              appendHex(result, buf[i]);      
          }      
          return result.toString();      
      }      
      private final static String HEX = "0123456789ABCDEF";
      private static void appendHex(StringBuffer sb, byte b) {      
          sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));      
      }  
}