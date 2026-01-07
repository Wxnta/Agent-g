import java.awt.*;
import javax.swing.ImageIcon;

public class Bullets extends GameObject{
  
  private int width = 20, height = 20;
  private double  speed = 10;
  Handler handler;

  private Image bulletImg = new ImageIcon("Assets/weapons/shoot/9.png").getImage(); 
  
  public Bullets(int x, int y,int targetX, int targetY, ID id, Handler handler){
    super(x, y, id);
    double dx = targetX-x;
    double dy = targetY-y;
    double hyp = Math.sqrt((dx*dx)+(dy*dy));
    vx = (dx/hyp) * speed ;
    vy = (dy/hyp)  * speed;
    this.handler = handler;
  }
  @Override
  public Rectangle getRect() {
    return new Rectangle((int)x, (int)y, width, height);
  }

  @Override
  public void update(boolean[] keys) {
    x += vx;
        
    
    y += vy;
    

      if(checkWallCollision()){
        handler.removeObject(this);
      }
      
  }

  public void move() {
    
  }

  @Override
  public void draw(Graphics g) {
    g.drawImage(bulletImg,(int)x,(int)y, null);
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
  
}
