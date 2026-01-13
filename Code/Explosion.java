import java.awt.*;
import javax.swing.*;

public class Explosion extends GameObject{
  private int width = 200;
  private int height = 200;
  private int damage = 50;
  private int explosionTimer = 0;
  private Image explosionGIF = new ImageIcon("Assets/weapons/impacts/explosion.GIF").getImage();
  Handler handler;
  public  Explosion(int x, int y, ID id, Handler handler){
    super(x, y, id);
    this.handler = handler;
  }

  public void update(){
    explosionTimer ++;
   //Damage enemy 1.5 seconds explosion
   if(explosionTimer == 75){
    handler.removeObject(this);
   }
   System.err.println(explosionTimer);
      Enemy enemy = checkEnemyCollision();
      
      if(enemy != null){
        //System.err.println("I hit a enemy");
        if(enemy.getId() == ID.Enemy){
          enemy.getHurt(damage);

        }
      }
  }

  //Checks if bullets collide with enemy
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

// Gets damage, the bullets does
  public int getDamage() {
    return damage;
  }       

  // Sets the damage, the bullets does
  public void setDamage(int damage) {
    this.damage = damage;
  }
  

  public Rectangle getRect(){
    return new Rectangle((int)x, (int)y, width, height);
  }

  public void draw(Graphics g){
    g.setColor(Color.RED);
   // g.fillRect((int)x,(int)y,width,height);
   g.drawImage(explosionGIF, (int)x, (int)y, width, height, null);
  }
}
