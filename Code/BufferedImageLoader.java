import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
//This class loads BufferedImages
public class BufferedImageLoader {
  //loads BufferedImages
  public BufferedImage loadBuffImg(String n){
    try {
      return ImageIO.read(new File(n));
    } 
    catch (IOException e) {
      System.out.println(e);
    }
    return null;
  }
}
