package burp;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class BurpExtender implements IBurpExtender, IIntruderPayloadProcessor {
    private static IExtensionHelpers helpers;
    public final static String extensionName = "AESCrack"; // 插件名称
    public final static String version = "1.0";
    public final static String AES_IV = "1234567812345678"; // 设置 AES IV 值
    public final static String AES_KEY = "key12345key67890"; // 设置 AES KEY 值


    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {
        // obtain an extension helpers object
        helpers = callbacks.getHelpers();

        // set our extension name
        callbacks.setExtensionName(extensionName); //// 插件名称

        // register ourselves as an Intruder payload processor
        callbacks.registerIntruderPayloadProcessor(this);

        // obtain our output and error streams
        PrintWriter stdout = new PrintWriter(callbacks.getStdout(), true);
        stdout.println(getBanner());
    }

    public String getProcessorName() {
        return "AESCrack";
    }

    public static String decryptAES(String paramString1) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Object localObject = new byte[0];
        {
            localObject = new SecretKeySpec(AES_KEY.getBytes(), "AES");
            IvParameterSpec localIvParameterSpec = new IvParameterSpec(AES_IV.getBytes());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, (Key) localObject, localIvParameterSpec);
            return new String(cipher.doFinal(helpers.base64Decode(paramString1)));
        }
    }

    public static String encryptAES(String paramString1)
            throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        IvParameterSpec iv = new IvParameterSpec(AES_IV.getBytes());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(1, key, iv);
        return helpers.base64Encode(cipher.doFinal(paramString1.getBytes()));
    }

    public byte[] processPayload(byte[] currentPayload, byte[] originalPayload, byte[] baseValue) {
        String dataParameter = helpers.bytesToString(currentPayload);
        String AesEncodeStr = null;
        try {
            AesEncodeStr = encryptAES(dataParameter);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return helpers.stringToBytes(AesEncodeStr);
    }

    public static String getBanner() {
        String bannerInfo =
                "[+] " + BurpExtender.extensionName + " is loaded\n"
                        + "[+] ^_^\n"
                        + "[+]\n"
                        + "[+] #####################################\n"
                        + "[+]    " + BurpExtender.extensionName + " v" + BurpExtender.version + "\n"
                        + "[+]    anthor: Jas502n\n"
                        + "[+]    email:  jas502n@gmail.com\n"
                        + "[+]    github: http://github.com/jas502n/Burp_AES_Plugin\n"
                        + "[+] ####################################";
        return bannerInfo;
    }

}