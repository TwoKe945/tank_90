package cn.com.twoke.game.tank.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class ResourceLoader {

    private static Map<String, BufferedImage> IMAGE_POOL = new HashMap<>();

	/**
	 * 加载图片资源
	 * @param imagePath
	 * @return
	 */
	public static BufferedImage loadImage(String imagePath) {
        if (IMAGE_POOL.containsKey(imagePath)) {
            return IMAGE_POOL.get(imagePath);
        }
		BufferedImage image = null;
        InputStream is = ResourceLoader.class.getResourceAsStream(imagePath);
        try {
            image = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        IMAGE_POOL.put(imagePath, image);
        return image;
	}
	
	
	
}
