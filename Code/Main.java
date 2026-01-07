/* ----------------------------------------------------------------------------------
 * Sprite.java
 * Mr. McKenzie
 * December 15 2022
 *
 * use BaseFrame for Sprite
*/


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

class Main extends BaseFrame{

public static final int WIDTH = 1200, HEIGHT = 800;
 private Handler handler;
 private Player player;
 private Camera camera;
 private  BufferedImage lvl;
 
 public Main(){
  
  super("Main", WIDTH, HEIGHT);
  handler = new Handler();
  player = new Player(0, 0, ID.Player, handler);
  handler.addObject(player);
  
  
  BufferedImageLoader loader = new BufferedImageLoader();
  lvl = loader.loadBuffImg("Assets/LvlImages/lvl1Image.png");
  camera = new Camera(0,0,lvl);
  

  loadLevel(lvl);
 } 
 
@Override
 public void update(){
  if(camera == null) return;
  handler.update(keys);
  
  camera.update(player);

  if(mb == MouseEvent.BUTTON1){
    player.shoot(mx,my, player, camera);
    mb = 0;
  } 
 }


 //Load level
 private void loadLevel(BufferedImage image){
  int w = image.getWidth();
  int h = image.getHeight();

  for(int xx = 0; xx < w; xx++){
   for(int yy = 0; yy < h; yy++){
    // int pixel = image.getRGB(xx, yy);
    // int red = (pixel >> 16) & 0xff;
    // int green = (pixel >> 8) & 0xff;
    // int blue = (pixel) & 0xff;

    //Reads the RGB value on each position from the image
    Color c = new Color(image.getRGB(xx, yy), true);
    
    if (c.getRed() == 255) {
        handler.addObject(new Block(xx * 32, yy * 32, ID.Block));
    }

    if (c.getBlue() == 255) {
        player.setX(xx * 32);
        player.setY(yy * 32);
    }

    if (c.getGreen() == 255) {
        handler.addObject(new Enemy(xx * 32, yy * 32, ID.Enemy, handler));
    }
   }
  }
 }

 
 @Override
 public void draw(Graphics g){
 if (camera==null)return;
  Graphics2D g2D = (Graphics2D) g;
  //everything sandwiched between translate will move with camera
  g2D.translate(-camera.getX(), -camera.getY());

  handler.draw(g);
  g2D.translate(camera.getX(), camera.getY());
 }
 
 public static void main(String[] args) {
  new Main();
    } 
}




