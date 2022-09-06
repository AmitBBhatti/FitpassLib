package com.fitpass.libfitpass.base.dataencription;

import android.util.Log;

import com.fitpass.libfitpass.base.constants.ConfigConstants;

import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.NoSuchPaddingException;

public class RandomKeyGenrator {
    public static String getRandomKey() {
        return RandomKey;
    }

    public static void setRandomKey(String randomKey) {
        RandomKey = randomKey;
    }

    static String  RandomKey="";
    public static String getAlphaNumericString(int n)
    {

        // chose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }
        Log.e("string_key_genrated",sb.toString());
        return sb.toString()+System.currentTimeMillis();
    }
    public static String encrptBodydataWithRandomKey(String data){
        Log.e("encrptdata Metod Log",data);
        String encrptData="";
        try {
            CryptLib cryptLib=new CryptLib();
            encrptData=  cryptLib.encryptPlainTextWithRandomIV(data,getRandomKey());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            encrptData=e.getMessage();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            encrptData=e.getMessage();
        }catch (Exception e){
            encrptData=e.getLocalizedMessage();
        }
        return encrptData;
    }
    public static String encrptBodydata(String data){
        Log.e("encrptdata Metod Log",data);
        String encrptData="";
        try {
            CryptLib cryptLib=new CryptLib();
            encrptData=  cryptLib.encryptPlainTextWithRandomIV(data, ConfigConstants.Companion.getDEFALUT_SECRET_KEY());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            encrptData=e.getMessage();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
            encrptData=e.getMessage();
        }catch (Exception e){
            encrptData=e.getLocalizedMessage();
        }
        return encrptData;
    }
    public static JSONObject decrptBODYFile(String data) {

        try {
            //String decrptedData = Aes256.decrypt(data, "s1d2f1j4");
            //String decrptedData = Aes256.decrypt(data, "s1d2f1j4");
            CryptLib cryptLib=new CryptLib();
            String decrptedData = cryptLib.decryptCipherTextWithRandomIV(data,getRandomKey());
            Log.e("decrptff",decrptedData);
            //return objectData(decrptedData);
            return new JSONObject(decrptedData);

        } catch (Exception e) {

            e.printStackTrace();
            return null;
        }

    }

    public static String generate16DigitRandom() {
        Random rand = new Random();
        long x = (long)(rand.nextDouble()*10000000000000000L);
        String s =String.format("%014d", x);
        return s;
    }
}
