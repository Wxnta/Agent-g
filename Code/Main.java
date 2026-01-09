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
 private Handler handler; //handles gameObject actions
 private Player player; //our player
 private Camera camera; //Our world camera
 private  BufferedImage lvl; //Current lvl image
 private int frameCount = 0; 
 private int gunDelay = 0; //Time between shots
 private boolean canShoot = true; //Lets us know if player can shoot
 
 public Main(){
  
  super("Main", WIDTH, HEIGHT);
  handler = new Handler();
  player = new Player(0, 0, ID.Player, handler);
  handler.addObject(player);
  
  gunDelay = player.getGunDelay();
  
  
  BufferedImageLoader loader = new BufferedImageLoader();
  lvl = loader.loadBuffImg("Assets/LvlImages/lvl1Image.png");
  camera = new Camera(0,0,lvl);
  
  //Loads the level
  loadLevel(lvl);
 } 
 
@Override
 public void update(){
 //Makes sure the camera is always there
  if(camera == null) return;
  frameCount++;
  //Handles all game objects update
  handler.update(keys);
  
  //Camera follows player
  camera.update(player);

  player.setAngle(getAngle());

  // PLAYER SHOOTING LOGIC
  //Let player schoots between cooldown
  if(frameCount % gunDelay == 0 && canShoot == false){
        canShoot = true;
         
  }
  if(canShoot){
      if(mb == MouseEvent.BUTTON1){
       
        player.shoot(mx,my, player, camera);
      
      mb = 0;
      frameCount = 1;
      
      canShoot = false;
  }
  }
 }

  private double getAngle(){
  int wmx = mx + camera.getX(); // should we make these global variables
  int wmy = my + camera.getY();
 
  int px = (int)player.getX() + player.getWidth()/2;
  int py = (int)player.getY() + player.getHeight()/2;
 
  double radians = Math.atan2(wmy-py, wmx-px);
 
 
    // System.out.println(radians);
  //   double degrees = Math.toDegrees(radians);
  return radians;
  }
 
 


 //Loads level
 private void loadLevel(BufferedImage image){
  int w = image.getWidth();
  int h = image.getHeight();

  //Gets evey position in map, checks color and places objects depending of color of location
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
        handler.addObject(new Enemy(xx * 32, yy * 32, ID.Enemy, handler, player));
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
  
  //Draws all gameObject
  handler.draw(g);
  g2D.translate(camera.getX(), camera.getY());
 }
 
 public static void main(String[] args) {
  new Main();
    } 
}




