import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends GameObject{

 private int speed = 5;
 private  int width = 50, height = 56;
 int mouseX, mouseY;
private Image playerImg = new ImageIcon("Assets/characters/example.png").getImage(); 

    
 Handler handler;

 //private boolean moving;
 //public static final int LEFT = 0, RIGHT = 1, WAIT = 2;
  
 public Player(int x, int y, ID id, Handler handler){
  super(x, y, id);
  this.handler = handler;
 }

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

  public void shoot(int mx, int my, Player player, Camera camera){
  int wmx = mx + camera.getX();
  int wmy = my + camera.getY();

  System.out.println(wmx +", "+wmy);
  int px = (int)player.getX() + player.getWidth()/2;
  int py = (int)player.getY() + player.getHeight()/2;

  handler.addObject(new Bullets(px,py,wmx,wmy,ID.Bullet, handler));
 }

 public  int getWidth() {
  return width;
}

 public  int getHeight() {
  return height;
 }

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
  @Override
 public Rectangle getRect(){
  return new Rectangle((int)x, (int)y, width, height);
 }


 
 public void draw(Graphics g) {

    g.drawImage(playerImg,(int)x,(int)y, null);


}
}