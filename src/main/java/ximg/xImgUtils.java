package ximg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class xImgUtils {
    static final Log LOG = LogFactory.getLog(xImgUtils.class);

    public static String toId(byte[] bytes) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        byte[] digest = md5.digest(bytes);
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            int bt = b & 0xFF;
            if (bt < 16) {
                sb.append(0);
            }
            sb.append(Integer.toHexString(bt));
        }
        return sb.toString();
    }

    public static String toPath(String id) {
        String s1 = id.substring(0, 3);
        String s2 = id.substring(3, 6);
        String l1 = String.valueOf(Integer.parseInt(s1, 16) / 4);
        String l2 = String.valueOf(Integer.parseInt(s2, 16) / 4);
        return "/" + l1 + "/" + l2 + "/" + id + "/";
    }
}
