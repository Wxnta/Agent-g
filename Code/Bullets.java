import java.awt.*;
import javax.swing.ImageIcon;
//Bullets
//Controls Movement, Dameage and Type of Bullet(From diff Guns)
public class Bullets extends GameObject{
  
  private int width = 20, height = 20;
  private double  speed = 10;
  Handler handler;
  private int damage = 20; //How much damage our bullets

  private Image bulletImg = new ImageIcon("Assets/weapons/shoot/9.png").getImage(); 
  
  //Bullets Constructor class
  public Bullets(int x, int y,int targetX, int targetY, ID id, Handler handler){
    super(x, y, id);
    //Calculate distance to Mouse Target
    double dx = targetX-x;
    double dy = targetY-y;
    //Calculate the line
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
      
      //System.out.println("Enemy Hit: ");
      Enemy enemy = checkEnemyCollision();
      
      if(enemy != null){
        if(enemy.getId() == ID.Enemy){
          enemy.getHurt(damage);
          //System.out.println(enemy.getHealth());
          handler.removeObject(this);
        }
      }

      // System.out.println("Enemy health: "+ enemy.getHealth());
      
  }

  public int getDamage() {
    return damage;
  }
  public void setDamage(int damage) {
    this.damage = damage;
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
  
}
