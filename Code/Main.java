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
import java.util.ArrayList;
import javax.swing.ImageIcon;
class Main extends BaseFrame{

public static final int WIDTH = 1200, HEIGHT = 800;
 private Handler handler; //handles gameObject actions

 private GameState gameState = GameState.MENU;
 private int menuOption = 0; 

 private Player player; //our player
 private Camera camera; //Our world camera
 private  BufferedImage lvl; //Current lvl image
 private int frameCount = 0;
 private int min = 0;
 private int seconds = 0;
 String timeString;
 private int shootFrameCount = 0; 
 private int gunDelay = 0; //Time between shots
 private boolean canShoot = true; //Lets us know if player can shoot
 private BufferedImageLoader loader = new BufferedImageLoader();
 private Image floorImg = (new ImageIcon("Assets/background/floor.png")).getImage();
 private Image menuImg = (new ImageIcon("Assets/gameIMG/menuIMG.png")).getImage();
 private Image controlsImg = (new ImageIcon("Assets/gameIMG/controlsIMG.png")).getImage();
 private Image storyImg = (new ImageIcon("Assets/gameIMG/storyIMG.png")).getImage();
 
 private Font fnt50;
 private Font fnt100;
 private Font fnt20;
 private int crateRespawnTime = 300; 
 private int money = 0;
 private HashTable<Crate> crateTable = new HashTable<Crate>();
 private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
 private int wave = 1;
 private boolean nextWave = false;

 private boolean waveTxtOn = true;
 private int waveTxtTImer;

 private int crateCount = 0;

 
 public Main(){
  
  super("Main", WIDTH, HEIGHT);
  handler = new Handler();
  player = new Player(100, 100, ID.Player, handler);
  handler.addObject(player);
  
  gunDelay = player.getGunDelay();
  // player.addPowerup("Dash");
  // player.addPowerup("SuperSpeed");
  // player.addGuns("RPG");
  
  
  
  
  
  

   if(wave == 1){
    lvl = loader.loadBuffImg("Assets/LvlImages/lvl1Image.png");
   }

  try {
      fnt50 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/fonts/gameFont.otf")).deriveFont(50f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(fnt50);
    } catch (Exception e) {
      e.printStackTrace();
      fnt50 = new Font("Monospaced", Font.PLAIN, 50);
    }

    try {
      fnt100 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/fonts/gameFont.otf")).deriveFont(100f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(fnt100);
    } catch (Exception e) {
      e.printStackTrace();
      fnt50 = new Font("Monospaced", Font.PLAIN, 100);
    }

    try {
      fnt20 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/fonts/gameFont.otf")).deriveFont(20f);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(fnt20
      );
    } catch (Exception e) {
      e.printStackTrace();
      fnt50 = new Font("Monospaced", Font.PLAIN, 20);
    }
  camera = new Camera(0,0,lvl);
  
  //Loads the level
  loadLevel(lvl);
 } 
 
@Override
 public void update(){
      if (gameState == GameState.MENU) {
        loadMenu();
        return;
    }

    if (gameState == GameState.PLAY) {
        loadGame();
        return;
    }

    if(gameState == GameState.CONTROLS){
      loadControls();
      return;
    }


    if (gameState == GameState.STORY) {
        loadStory();
        return;
    }

    if (gameState == GameState.PAUSE) {
        return;
    }



 
  }

  private void loadGame(){
     long before = System.nanoTime();
     //player.addGuns("Freeze");
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
      crate.setHitboxDisabled(true);
      crateCount -=1;
      crateTable.remove(crate);
      
  }
  if(enemyList != null){
  for(int i = 0; i < enemyList.size(); i++){
    if(enemyList.get(i).getHealth() <=0  ){
      handler.removeObject(enemyList.get(i));
      enemyList.remove(enemyList.get(i));
    }
  }
}

  if(enemyList.isEmpty()){
    nextWave = true;
    
  }
  if(nextWave){
    min = 0;
    seconds = 0;
    frameCount = 0;
    waveTxtOn = true;
    wave += 2;
    System.err.println("Wave: " + wave);
    lvl = loader.loadBuffImg("Assets/LvlImages/lvl1Image - Copy.png");
    spawnZombie(lvl);
    nextWave = false;
  }
  long after = System.nanoTime();
  //System.out.println(after-before);
 }

  //Returns the angle between player and mouse
  private double getAngle(){
  int wmx = mx + camera.getX(); // should we make these global variables
  int wmy = my + camera.getY();
 
  int px = (int)player.getX() + player.getWidth()/2;
  int py = (int)player.getY() + player.getHeight()/2;
 
  double radians = Math.atan2(wmy-py, wmx-px);
 
 
  return radians;
  }

  private void loadMenu(){
  //   g.drawRect(10, 610, 320, 140);

  //  g.drawRect(410, 610, 320, 140);

  //  g.drawRect(810, 610, 320, 140);

  if(mb == MouseEvent.BUTTON1 && 10 <= mx && mx <= 330 && 610 <= my && my <= 750 ){
    gameState = gameState.PLAY;
  }

  if(mb == MouseEvent.BUTTON1 && 410 <= mx && mx <= 740 && 610 <= my && my <= 750 ){
    gameState = gameState.CONTROLS;
  }

  if(mb == MouseEvent.BUTTON1 && 810 <= mx && mx <= 1140 && 610 <= my && my <= 750 ){
    gameState = gameState.STORY;
  }
     
  }

  private void loadControls(){
    // g.drawRect(20, 10, 120, 120);
    if(mb == MouseEvent.BUTTON1 && 20 <= mx && mx <= 140 && 10 <= my && my <= 130 ){
    gameState = gameState.MENU;
  }
  }

  private  void loadStory(){
    if(mb == MouseEvent.BUTTON1 && 20 <= mx && mx <= 140 && 10 <= my && my <= 130 ){
    gameState = gameState.MENU;
  }
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

    if(wave == 1){
      if (c.getBlue() == 255 && c.getRed() == 0 && c.getGreen() == 0) {
          player.setX(xx * 32);
          player.setY(yy * 32);
      }
  }
    if(wave ==1 ){
    if (c.getGreen() == 255 && c.getRed() == 0 && c.getBlue() == 0) {
       
        Enemy enemy = new Enemy(xx * 32, yy * 32, ID.Enemy, handler, player, wave, "attackingEnemy", camera);
        enemyList.add(enemy);
        handler.addObject(enemy);
        
    }
  }

    if (c.getRed() == 0 && c.getGreen() == 255 && c.getBlue() == 255 && Util.randint(0, 3) == 0) {
   // System.err.println("Adding Crate at ("+xx*32+","+yy*32+")");
        Crate baseCrate = new Crate(xx * 32, yy * 32, ID.Crate, handler, player, wave);
        crateTable.add(baseCrate);
        crateCount += 1;
        handler.addObject(baseCrate);
    }

    
   }
  }
 }

 //Spawn Crates around the map
 private void spawnCrate(BufferedImage image){
    int w = image.getWidth();
    int h = image.getHeight();
    for(int xx = 0; xx < w; xx++){
    for(int yy = 0; yy < h; yy++){
   

    //Reads the RGB value on each position from the image
    Color c = new Color(image.getRGB(xx, yy), true);

    if (c.getRed() == 0 && c.getGreen() == 255 && c.getBlue() == 255 && Util.randint(0, 1) == 0) {
    //System.err.println("Adding Crate at ("+xx*32+","+yy*32+")");
    if(crateCount < 5){
      Crate baseCrate = new Crate(xx * 32, yy * 32, ID.Crate, handler, player,wave);
        if(crateTable.samePos(baseCrate.hashCode()) != null){
          //System.err.println(" Crate Stacked at ("+xx*32+","+yy*32+")");
          return;
        } else {
          
          crateTable.add(baseCrate);
          
          crateCount += 1;
          //System.err.println(crateCount);
          handler.addObject(baseCrate);
        }
    }
  }

    
   }
  }
 }

 private void spawnZombie(BufferedImage image){
  int w = image.getWidth();
    int h = image.getHeight();

    for(int xx = 0; xx < w; xx++){
      for(int yy = 0; yy < h; yy++){
   

    //Reads the RGB value on each position from the image
        Color c = new Color(image.getRGB(xx, yy), true);
      if (c.getGreen() == 255 && c.getRed() == 0 && c.getBlue() == 0 && enemyList.size() <= (wave*2 + 3) && Util.randint(0, 3) == 0 ) {
          // System.err.println("Adding enemies");
          String type;
          if(Util.randint(1, 4) == 1 && wave>=3){
            type = "shootingEnemy";
            
          }
          else{
            type = "attackingEnemy";

          }

          System.err.println(type);
          if(type.equals("shootingEnemy")){
            System.err.println("Shooting Enemy Spawned");
          }
          Enemy enemy = new Enemy(xx * 32, yy * 32, ID.Enemy, handler, player, wave, type, camera);
          enemyList.add(enemy);
          handler.addObject(enemy);
          
      }
    
  }

    
   }
  }



  private void drawGame(Graphics g){
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
   seconds = frameCount/50;
  
  if(seconds == 60){
   // System.err.println("AGAGAG");
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

  if(waveTxtOn){
    g.setFont(fnt100);
    g.setColor(Color.GREEN);
    g.drawString("Wave " + wave, WIDTH/2 + camera.getX() - 130, 150+ camera.getY());
    
    if(seconds == 3){
      waveTxtOn = false;
    }
  }


  //Draw Health Bar
  g.setColor(Color.BLACK);
  g.fillRect(890 + camera.getX(), 10 + camera.getY(), 220, 50);

  if(player.getHealth() >= 50){
    g.setColor(Color.GREEN);
  }
  else if (player.getHealth() >= 30){
    g.setColor(Color.YELLOW);
  }
  else{
    g.setColor(Color.RED);
  }
  g.fillRect(900 + camera.getX(), 20 + camera.getY(), player.getHealth() * 2, 30);


   
  //  g.setFont(fnt50);
  //  g.drawString("Current Gun: " + player.getCurrentGun(), camera.getX() + WIDTH/2 -200, camera.getY() + HEIGHT - 10);
   g.setFont(fnt20);
   if(player.getPowerups().contains("SuperStrength") && player.getSuperStrengthTime() > 0){
    g.setColor(Color.CYAN);
    g.drawString("X2 DAMAGE: " + ((1000 - player.getSuperStrengthTime())/50) +"s", camera.getX() + 10, camera.getY() + HEIGHT - 130);
   }
   else{
    g.setColor(Color.ORANGE);
    g.drawString("SuperStrength: " + player.getPowerups().contains("SuperStrength"), camera.getX() + 10, camera.getY() + HEIGHT - 130);
   }

   if(player.getFreezeAmmo() == 0){
    g.setColor(Color.ORANGE);
   g.drawString("Freeze Bullets: "+ player.getFreezeAmmo() +"/5", camera.getX() + 10, camera.getY() + HEIGHT - 100);
   }
   else{
    g.setColor(Color.CYAN);
   g.drawString("Freeze Bullets: "+ player.getFreezeAmmo() +"/5", camera.getX() + 10, camera.getY() + HEIGHT - 100);
   }

   if(player.getRpgAmmo() == 0){
    g.setColor(Color.ORANGE);
   g.drawString("RPG Bullets: "+ player.getRpgAmmo() +"/3", camera.getX() + 10, camera.getY() + HEIGHT - 70);
   }
   else{
    g.setColor(Color.CYAN);
   g.drawString("RPG Bullets: "+ player.getRpgAmmo() +"/3", camera.getX() + 10, camera.getY() + HEIGHT - 70);
   }


   if(!player.getPowerups().contains("SuperSpeed")){
    g.setColor(Color.ORANGE);
    g.drawString("SuperSpeed: " + player.getPowerups().contains("SuperSpeed"), camera.getX() + 10, camera.getY() + HEIGHT - 40);
   }
   else if(player.isCanSuperSpeed()){
    g.setColor(Color.CYAN);
    g.drawString("SuperSpeed: " + ((250 - player.getSuperSpeedTime())/50) +"s", camera.getX() + 10, camera.getY() + HEIGHT - 40);
   }
   else{
    g.setColor(Color.CYAN);
    g.drawString("SuperSpeed: Ready", camera.getX() + 10, camera.getY() + HEIGHT - 40);
   }
   
   if(!player.getCanDash(player.getPowerups())){
    g.setColor(Color.ORANGE);
    g.drawString("Dash: "+ player.getCanDash(player.getPowerups()), camera.getX() + 10, camera.getY() + HEIGHT - 10);
   }
   else{
    g.setColor(Color.CYAN);
    g.drawString("Dash: "+ player.getCanDash(player.getPowerups()), camera.getX() + 10, camera.getY() + HEIGHT - 10);
   }
   
   
  
  g2D.translate(camera.getX(), camera.getY());
  }
 

 //Draws everything in our code
 @Override
 public void draw(Graphics g){
  if (gameState == GameState.MENU) {
    g.drawImage(menuImg, 0, 0, null);
  }
  else if(gameState == GameState.PLAY){
  drawGame(g);
  }
  else if(gameState == GameState.CONTROLS){
    g.drawImage(controlsImg, 0, 0, null);
    
  }
  else if(gameState == GameState.STORY){
    g.drawImage(storyImg, 0, 0, null);
  }
  
 }
 
 //Runs our code
 public static void main(String[] args) {
  new Main();
    } 

}




