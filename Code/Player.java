import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.image.*;
 

//Player Class
//Controls Players Movements an action
public class Player extends GameObject{

 private int speed = 5;
 private  int width = 50, height = 56;
 private double angle = 90;
 private int gunDelay = 20;
 int mouseX, mouseY;

 BufferedImageLoader loader = new BufferedImageLoader(); 
 private BufferedImage playerImg = loader.loadBuffImg("Assets/characters/example.png"); 
 
    
 Handler handler;

 
//Player Constructor class
 public Player(int x, int y, ID id, Handler handler){
  super(x, y, id);
  this.handler = handler;
 }

 //Updates Player Movement
 @Override
 public void update(boolean []keys){
    vx = 0;
    vy = 0;
    if(keys[KeyEvent.VK_W]) vy = -speed;
    if(keys[KeyEvent.VK_S]) vy =  speed;
    if(keys[KeyEvent.VK_A]) vx = -speed;
    if(keys[KeyEvent.VK_D]) vx =  speed;
    move(keys);
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
  
  //Logic for PLayer SHooting
  public void shoot(int mx, int my, Player player, Camera camera){
  int wmx = mx + camera.getX();
  int wmy = my + camera.getY();

  int px = (int)player.getX() + player.getWidth()/2;
  int py = (int)player.getY() + player.getHeight()/2;

  handler.addObject(new Bullets(px,py,wmx,wmy,ID.Bullet, handler));
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

 public void setAngle(double angle){
    this.angle = angle;
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
}