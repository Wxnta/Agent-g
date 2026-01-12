import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;
 

//Player Class
//Controls Players Movements an action
public class Player extends GameObject{

private int frameCount = 0;
 private int speed = 5;
 private int superSpeed = speed + 5;
 private  int width = 50, height = 56;
 private double angle = 0;
 private int gunDelay = 20;
 private int ammoCount = 25;
 private int health = 100;
 private int dashDamage = 50;
 private int superSpeedTime = 0;
 private boolean canDash= true;
// private int mouseX, mouseY;
 private ArrayList<String> powerups = new ArrayList<String>();
 
 
 //int mouseX, mouseY;

 BufferedImageLoader loader = new BufferedImageLoader(); 
 private BufferedImage playerImg = loader.loadBuffImg("Assets/characters/example.png"); 
 
    
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
    
    if(!powerups.contains("SuperSpeed")){

        if (keys[KeyEvent.VK_W]) vy = -speed;
        if (keys[KeyEvent.VK_S]) vy =  speed;
        if (keys[KeyEvent.VK_A]) vx = -speed;
        if (keys[KeyEvent.VK_D]) vx =  speed;
        
    }
    else{
        superSpeedTime ++;
        if(superSpeedTime % 250 == 0){
            powerups.remove("SuperSpeed");
        }
        System.err.println("I can speed");
        if (keys[KeyEvent.VK_W]) vy = -superSpeed;
        if (keys[KeyEvent.VK_S]) vy =  superSpeed;
        if (keys[KeyEvent.VK_A]) vx = -superSpeed;
        if (keys[KeyEvent.VK_D]) vx =  superSpeed;
    }
    move(keys);

    if (!canDash && frameCount >= 60) {
        canDash = true;
    }

    // ONLY dash when shift is pressed
    if (canDash && keys[KeyEvent.VK_SHIFT] && powerups.contains("Dash")) {
        dash(camera, mouseX, mouseY);
        canDash = false;
        frameCount = 0;
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

  int px = (int)player.getX() + player.getWidth()/2;
  int py = (int)player.getY() + player.getHeight()/2;

  handler.addObject(new Bullets(px,py,wmx,wmy,ID.Bullet, handler));
 }


 //Check if we collided with wall
 private boolean checkWallCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Block){
            if(this.getRect().intersects(obj.getRect())){
                return true;
            }
        }
    }
    return false;
}

private Enemy checkEnemyCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Enemy){
            if(this.getRect().intersects(obj.getRect())){
                return (Enemy) obj;
            }
        }
    }
    return null;
}

public Crate checkCrateCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Crate){
            if(this.getRect().intersects(obj.getRect())){
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
}