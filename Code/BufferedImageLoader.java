import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BufferedImageLoader {
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
