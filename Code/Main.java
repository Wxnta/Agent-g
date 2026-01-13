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
import java.io.File;
import javax.swing.ImageIcon;
class Main extends BaseFrame{

public static final int WIDTH = 1200, HEIGHT = 800;
 private Handler handler; //handles gameObject actions
 private Player player; //our player
 private Camera camera; //Our world camera
 private  BufferedImage lvl; //Current lvl image
 private int frameCount = 0;
 private int min = 0;
 String timeString;
 private int shootFrameCount = 0; 
 private int gunDelay = 0; //Time between shots
 private boolean canShoot = true; //Lets us know if player can shoot
 private Image floorImg = (new ImageIcon("Assets/background/floor.png")).getImage();
 private Font fnt50;
 private int crateRespawnTime = 300; 
 private int money = 0;
 private HashTable<Crate> crateTable = new HashTable<Crate>();
 
 public Main(){
  
  super("Main", WIDTH, HEIGHT);
  handler = new Handler();
  player = new Player(0, 0, ID.Player, handler);
  handler.addObject(player);
  
  gunDelay = player.getGunDelay();
  player.addPowerup("Dash");
  player.addPowerup("SuperSpeed");
  player.addGuns("RPG");
  
  
  
  
  BufferedImageLoader loader = new BufferedImageLoader();
  

  
  lvl = loader.loadBuffImg("Assets/LvlImages/lvl1Image.png");
  try {
      fnt50 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/fonts/gameFont.otf")).deriveFont(50f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(fnt50);
    } catch (Exception e) {
      e.printStackTrace();
      fnt50 = new Font("Monospaced", Font.PLAIN, 40);
    }
  camera = new Camera(0,0,lvl);
  
  //Loads the level
  loadLevel(lvl);
 } 
 
@Override
 public void update(){
  
 //Makes sure the camera is always there
  if(camera == null) return;
  frameCount++;
  shootFrameCount++;
  //Handles all game objects update
  handler.update(keys);
  
  //Camera follows player
  camera.update(player);

  player.update(keys, camera, mx, my);
  

  player.setAngle(getAngle());

  // PLAYER SHOOTING LOGIC
  //Let player schoots between cooldown
  if(shootFrameCount % gunDelay == 0 && canShoot == false && player.getAmmoCount() !=0){
        canShoot = true;
         
  }
  if(canShoot){
      if(mb == MouseEvent.BUTTON1){
       
        player.shoot(mx,my, player, camera);
      
      mb = 0;
      
      player.setAmmoCount(player.getAmmoCount() -1);
      canShoot = false;
  }
  }

  if(shootFrameCount % crateRespawnTime == 0){
      spawnCrate(lvl);  
         
  }

  if(player.checkCrateCollision() != null){
      Crate crate = player.checkCrateCollision();
      crate.giveLoot();
      crateTable.remove(crate);
      
  }
 }

  //Returns the angle between player and mouse
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
   

    //Reads the RGB value on each position from the image
    Color c = new Color(image.getRGB(xx, yy), true);
    
    //Adds object to corresponding colours
    if (c.getRed() == 255) {
        handler.addObject(new Block(xx * 32, yy * 32, ID.Block));
    }

    if (c.getBlue() == 255 && c.getRed() == 0 && c.getGreen() == 0) {
        player.setX(xx * 32);
        player.setY(yy * 32);
    }

    if (c.getGreen() == 255 && c.getRed() == 0 && c.getBlue() == 0) {
        handler.addObject(new Enemy(xx * 32, yy * 32, ID.Enemy, handler, player));
    }

    if (c.getRed() == 0 && c.getGreen() == 255 && c.getBlue() == 255 && Util.randint(0, 2) == 0) {
   // System.err.println("Adding Crate at ("+xx*32+","+yy*32+")");
    
        handler.addObject(new Crate(xx * 32, yy * 32, ID.Crate, handler, player));
    }

    
   }
  }
 }

 private void spawnCrate(BufferedImage image){
    int w = image.getWidth();
    int h = image.getHeight();
    for(int xx = 0; xx < w; xx++){
    for(int yy = 0; yy < h; yy++){
   

    //Reads the RGB value on each position from the image
    Color c = new Color(image.getRGB(xx, yy), true);

    if (c.getRed() == 0 && c.getGreen() == 255 && c.getBlue() == 255 && Util.randint(0, 2) == 0) {
    //System.err.println("Adding Crate at ("+xx*32+","+yy*32+")");
    if(handler.getCrateCount() < 5){
      Crate baseCrate = new Crate(xx * 32, yy * 32, ID.Crate, handler, player);
        if(crateTable.samePos(baseCrate.hashCode()) != null){
          //System.err.println(" Crate Stacked at ("+xx*32+","+yy*32+")");
          return;
        } else {
          crateTable.add(baseCrate);
         // System.err.println(crateTable);
          handler.addObject(baseCrate);
        }
    }
  }

    
   }
  }
 }

 //Draws everything in our code
 @Override
 public void draw(Graphics g){
 if (camera==null)return;
    Graphics2D g2D = (Graphics2D) g;
    //everything sandwiched between translate will move with camera
    g2D.translate(-camera.getX(), -camera.getY());
    

    //level tiles
    for(int xx = 0; xx < lvl.getWidth()*64; xx+=64){
      for(int yy = 0; yy < lvl.getHeight()*64; yy+=64){
        
        g.drawImage(floorImg, xx, yy, null);
      }
    }
    
  //Draws all gameObject
  handler.draw(g);


  g.setFont(fnt50);
  g.setColor(Color.RED);
  g.drawString("Ammo: " + player.getAmmoCount(), 50 + camera.getX(), 50 + camera.getY());
  int seconds = frameCount/50;
  
  if(seconds == 60){
    System.err.println("AGAGAG");
    min += 1;
    seconds = 0;
    frameCount = 0;
  }
  
  if(min == 0 && seconds < 10){
     timeString = String.format("00:0%d", seconds);
  }
  else if(min == 0 && seconds >= 10){
    timeString = String.format("00:%d", seconds);
  }
  else if(min <= 10 && seconds <= 10){
      timeString = String.format("%d:0%d", min, seconds);
  }
  else if(min <= 10 && seconds >= 10){
      timeString = String.format("%d:%d", min, seconds);
  }
  
  
  g.drawString(timeString, 550 + camera.getX(), 50 + camera.getY());

  //Draw Health Bar
  g.setColor(Color.BLACK);
  g.fillRect(890 + camera.getX(), 10 + camera.getY(), 220, 50);
  g.setColor(Color.GREEN);
  g.fillRect(900 + camera.getX(), 20 + camera.getY(), player.getHealth() * 2, 30);
  
  g2D.translate(camera.getX(), camera.getY());
  
 }
 
 //Runs our code
 public static void main(String[] args) {
  new Main();
    } 

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}




