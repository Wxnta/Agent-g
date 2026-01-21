import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;
 

//Player Class
//Controls Players Movements an action
public class Player extends GameObject{

 private int frameCount = 0;
 private int speed = 6;
 private int SUPERSPEED = speed + 5;
 private  int width = 50, height = 56;
 private double angle = 0;
 private int gunDelay = 20;
 private int ammoCount = 25;
 private int health = 100;
 private int maxHealth = 100;
 private int dashDamage = 50;
 private int superSpeedTime = 0;
 private int superStrengthTime = 0;
 private int playerFreezeTime = 25;
 private boolean canDash= true;
 private boolean  canSuperSpeed;
 private boolean canSuperStrength;
 private boolean isFrozen;
 private String currentGun = "PulseRifle";

 private int rpgAmmo;
 private int freezeAmmo;
// private int mouseX, mouseY;
 private ArrayList<String> powerups = new ArrayList<String>();
 private ArrayList<String> guns = new ArrayList<String>();
 
 
 //int mouseX, mouseY;

 BufferedImageLoader loader = new BufferedImageLoader(); 
 private BufferedImage playerImg = loader.loadBuffImg("Assets/characters/example.png"); 
 private SoundEffect dashSFX = new SoundEffect("Assets/sounds/dash.wav");
 
    
 Handler handler;

 
//Player Constructor class
 public Player(int x, int y, ID id, Handler handler){
  super(x, y, id);
  this.handler = handler;
 }

 //Updates Player Movement

 public void update(boolean[] keys, Camera camera, int mouseX, int mouseY) {
    frameCount++;

    vx = 0;
    vy = 0;

    if(isFrozen){
        playerFreezeTime ++;
        speed = 0;
    }
    else{
        speed = 6;
    }

    if(playerFreezeTime >= 40){
        System.err.println("playerFreezeTime:" + playerFreezeTime);
        isFrozen = false;
        playerFreezeTime = 0;
    }

    
    
   //FIX SUPERSPEED SO ITS TRUE ON CLICK AND COUNTS DOWN ON CLICK
    if(powerups.contains("SuperSpeed") && keys[KeyEvent.VK_SHIFT] && !isFrozen){
        superSpeedTime ++;
        canSuperSpeed = true;
        if(superSpeedTime >= 250){
            powerups.remove("SuperSpeed");
            canSuperSpeed = false;
            superSpeedTime = 0;
        }
       // System.err.println("I can speed");
        if (keys[KeyEvent.VK_W]) vy = -SUPERSPEED;
        if (keys[KeyEvent.VK_S]) vy =  SUPERSPEED;
        if (keys[KeyEvent.VK_A]) vx = -SUPERSPEED;
        if (keys[KeyEvent.VK_D]) vx =  SUPERSPEED;
    }
     else{
        if (keys[KeyEvent.VK_W]) vy = -speed;
        if (keys[KeyEvent.VK_S]) vy =  speed;
        if (keys[KeyEvent.VK_A]) vx = -speed;
        if (keys[KeyEvent.VK_D]) vx =  speed;
        
    }
    move(keys);

    if (!canDash && frameCount >= 60 && !isFrozen) {
        canDash = true;
    }

    // ONLY dash when shift is pressed
    if (canDash && keys[KeyEvent.VK_CONTROL] && powerups.contains("Dash")) {
        dash(camera, mouseX, mouseY);
        canDash = false;
        frameCount = 0;
    }

    if(guns.contains("RPG") && keys[KeyEvent.VK_R]){
        currentGun = "RPG";
    }

    if(guns.contains("Tranquilizer") && keys[KeyEvent.VK_F]){
        currentGun = "Tranquilizer";
    }

    if(keys[KeyEvent.VK_E]){
        currentGun = "PulseRifle";
    }

    if(rpgAmmo == 0 && guns.contains("RPG")){
        guns.remove("RPG");
        currentGun = "PulseRifle";
    }

    if(freezeAmmo == 0 && guns.contains("Tranquilizer")){
        guns.remove("Tranquilizer");
        currentGun = "PulseRifle";
    }
    
    if(powerups.contains("SuperStrength")){
        canSuperStrength = true;
        superStrengthTime ++;
        if(superStrengthTime >= 1000){
            powerups.remove("SuperStrength");
            canSuperStrength = false;
            superStrengthTime = 0;
        }
    }
    

    
}

 

 //Logic For Player's Movemnt
 public void move(boolean []keys){
    
    x += vx;
    if(checkWallCollision()){
        x -= vx;
    }

    y += vy;
    if(checkWallCollision()){
        y -= vy;
    }

   
  
 }

 public void dash(Camera camera, int mouseX, int mouseY) {
     dashSFX.play();
//    double cameraX = (double)(x + width / 2 - Main.WIDTH / 2) * 0.1f;
//     camera.setX((int)cameraX);

//     double cameraY = (double)(x + width / 2 - Main.WIDTH / 2) * 0.1f;
//     camera.setX((int)cameraX);
       
    int wmx = mouseX + camera.getX();
    int wmy = mouseY + camera.getY();

    double dx = wmx - x;
    double dy = wmy - y;
    double hyp = Math.sqrt(dx * dx + dy * dy);
    if (hyp == 0) return;

    double dashX = (dx / hyp) * speed * 30;
    double dashY = (dy / hyp) * speed * 30;

    x += dashX;
    if (checkWallCollision()) x -= dashX;

    y += dashY;
    if (checkWallCollision()) y -= dashY;

    //if player dashes into enemy, deals damage
    Enemy enemy = checkEnemyCollision();

    if(enemy != null){
        if(enemy.getId() == ID.Enemy){
          enemy.getHurt(dashDamage);
        }
      }
}
  


  //Logic for PLayer SHooting
  public void shoot(int mx, int my, Player player, Camera camera){
 
  int wmx = mx + camera.getX();
  int wmy = my + camera.getY();

  int px = (int)(player.getX() + player.getWidth()/2) ;
  int py = (int)(player.getY() + player.getHeight()/2);
    // Spawn bullet a little in front of the player so SuperSpeed doesn't overlap it
    double dx = wmx - px;
    double dy = wmy - py;
    double hyp = Math.sqrt(dx*dx + dy*dy);
    int spawnX = px;
    int spawnY = py;
    if(hyp != 0){
        double offset = player.getWidth() / 2.0 + 8; // place bullet just outside player's bounds
        spawnX = (int)(px + (dx / hyp) * offset);
        spawnY = (int)(py + (dy / hyp) * offset);
    }

    handler.addObject(new Bullets(spawnX, spawnY, wmx, wmy, ID.Bullet, handler, player));
 }


 //Check if we collided with wall
 private boolean checkWallCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Block){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
                return true;
            }
        }
    }
    return false;
}

private Enemy checkEnemyCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Enemy){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
                return (Enemy) obj;
            }
        }
    }
    return null;
}

public Crate checkCrateCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Crate){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
                return (Crate) obj;
            }
        }
    }
    return null;
}

public void addPowerup(String powerup){
    powerups.add(powerup);
  }

public  void getHurt(int damage){
    health -= damage;
  }

 //Gets Rect
  @Override
 public Rectangle getRect(){
  return new Rectangle((int)x, (int)y, width, height);
 }



 //Draw our player
  public void draw(Graphics g) {
    AffineTransform rot = new AffineTransform();
    rot.rotate(angle,25, 28);       // 75,84 is the center of my Image, this is the point of rotation.
    AffineTransformOp rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR);
    Graphics2D g2D = (Graphics2D)g;
    g2D.drawImage(playerImg,rotOp, (int) x, (int) y);                                                       // NEAREST_NEIGHBOR is fastest but lowest quality
 
    // g.drawImage(playerImg,(int)x,(int)y, null);
 
 
}


 //Gets Width
 public  int getWidth() {
  return width;
}
 
 //Gets Height
 public  int getHeight() {
  return height;
 }

 //Gets Gun Delay
 public int getGunDelay() {
    return gunDelay;
}

 //Sets Gun Delay
 public void setGunDelay(int gunDelay) {
    this.gunDelay = gunDelay;
 }

 //Sets angle for rotation
 public void setAngle(double angle){
    this.angle = angle;
 }

//Gets how much ammo player has
public int getAmmoCount() {
   return ammoCount;
}

//Sets the ammo for player
public void setAmmoCount(int ammoCount) {
    this.ammoCount = ammoCount;
}

public int getHealth() {
    return health;
}

public void setHealth(int health) {
    this.health = health;
}

public int getMaxHealth() {
    return maxHealth;
}

public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
}

public ArrayList<String> getGuns() {
    return guns;
}

public void addGuns(String gun) {
    guns.add(gun);
}

    public int getRpgAmmo() {
        return rpgAmmo;
    }

    public void setRpgAmmo(int rpgAmmo) {
        this.rpgAmmo = rpgAmmo;
    }

    public ArrayList<String> getPowerups() {
        return powerups;
    }

    public void setPowerups(ArrayList<String> powerups) {
        this.powerups = powerups;
    }

    public boolean getCanDash(ArrayList powerups) {
        if(powerups.contains("Dash")) {
            return true;
        }
        return false;
    }

    public boolean isCanSuperSpeed() {
        return canSuperSpeed;
    }


    public int getSuperSpeedTime() {
        return superSpeedTime;
    }

    public void setSuperStrengthTime(int superStrengthTime) {
        this.superStrengthTime = superStrengthTime;
    }

    public int getSuperStrengthTime() {
        return superStrengthTime;
    }

    public boolean isCanSuperStrength() {
        return canSuperStrength;
    }

    public void setFreezeAmmo(int freezeAmmo) {
        this.freezeAmmo = freezeAmmo;
    }

    public int getFreezeAmmo() {
        return freezeAmmo;
    }

    public String getCurrentGun() {
        return currentGun;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }
    

}