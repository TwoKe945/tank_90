package cn.com.twoke.game.tank.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceLoader {


	/**
	 * 加载图片资源
	 * @param imagePath
	 * @return
	 */
	public static BufferedImage loadImage(String imagePath) {
		BufferedImage image = null;
        InputStream is = ResourceLoader.class.getResourceAsStream(imagePath);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            System.out.println(imagePath);
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return image;
	}
	
	
	
}
