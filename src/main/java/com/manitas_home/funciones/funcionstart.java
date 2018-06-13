package com.manitas_home.funciones;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.tomcat.util.codec.binary.Base64;

import com.manitas_home.repositories.AdministradorRepository;

public class funcionstart {
	
	private static boolean first=true;
	
	public static boolean getFirst(AdministradorRepository RAdministrador){
			if(first&&RAdministrador.count()>0){
				first=false;
			}
		return first;
	}
	public static String suscriptionCoder(String emailremitente,String emaildestino){
		String codificado="",param1="",param2="";
		char [] codes={'q','w','e','r','t','y','u','i','o','p','a','s','d','f','g','h','j','k','l','z','x','c','v','b','n','m'};
		int i=0;
		while(param1==""&&param2==""){
			if(emailremitente.equals(emaildestino)){
				param2=emaildestino;
				param1=emailremitente;
			}
			else if(emailremitente.length()==i){
				param2=emaildestino;
				param1=emailremitente;
			}
			else if(emaildestino.length()==i){
				param1=emaildestino;
				param2=emailremitente;
			}
			else if(emailremitente.toLowerCase().charAt(i)>emaildestino.toLowerCase().charAt(i)){
				param1=emaildestino;
				param2=emailremitente;
			}
			else if(emailremitente.toLowerCase().charAt(i)<emaildestino.toLowerCase().charAt(i)){
				param2=emaildestino;
				param1=emailremitente;
			}
			else if(emailremitente.toLowerCase().charAt(i)==emaildestino.toLowerCase().charAt(i)){
				i++;
			}
			
		}
		for(i=param1.length()-1;i>=0;i--){
			int a=(int)param1.charAt(i)*(i+1);
			int b=a+i;
			int c=codes[b%26];
			codificado+=b+""+c+""+a;
		}
		for(i=0;i<param2.length();i++){
			int a=(int)param2.charAt(i)*(i+1)+i;
			int b=a+i;
			int c=codes[b%26];
			codificado+=a+""+c+""+b;
		}
		return codificado;
	}
	private static String key = "R7wbBoGzcyCdcNMf"; // 128 bit key
    private static String initVector = "RandomInitVector"; // 16 bytes IV
	
	public static String encrypt(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string: "
                    + Base64.encodeBase64String(encrypted));

            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(encrypted));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
