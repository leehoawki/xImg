package ximg;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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


    public static void resize(String originalFile, String resizedFile, int width, int height) {
        try {
            ImageIcon ii = new ImageIcon(originalFile);
            Image i = ii.getImage();
            if (width == 0) width = i.getWidth(null);
            if (height == 0) height = i.getHeight(null);
            Image resizedImage = i.getScaledInstance(width, height, Image.SCALE_FAST);
            Image temp = new ImageIcon(resizedImage).getImage();
            BufferedImage bufferedImage = new BufferedImage(temp.getWidth(null), temp.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics g = bufferedImage.createGraphics();
            g.setColor(Color.white);
            g.fillRect(0, 0, temp.getWidth(null), temp.getHeight(null));
            g.drawImage(temp, 0, 0, null);
            g.dispose();
            float softenFactor = 0.05f;
            float[] softenArray = {0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor, 0};
            Kernel kernel = new Kernel(3, 3, softenArray);
            ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
            bufferedImage = cOp.filter(bufferedImage, null);
            FileOutputStream out = new FileOutputStream(resizedFile);
            ImageIO.write(bufferedImage, "jpg", new File(resizedFile));
            out.flush();
            out.close();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
