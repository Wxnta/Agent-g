
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends GameObject{
  private int speed;
  private int width = 50, height = 50;
  private int health = 100;

  Handler handler;

  public Enemy(int x, int y, ID id, Handler handler){
    super(x, y, id);
    this.handler = handler;
  }

  public void update(boolean[] keys){
    if(health<=0){
      handler.removeObject(this);
    }
  }

  public  void getHurt(int damage){
    health -= damage;
    if(health <= 0){
      handler.removeObject(this);
    }
  }

  public int getHealth(){
    return health;
  }

  @Override
  public Rectangle getRect(){
    return new Rectangle((int)x, (int) y, width, height);
  }

  public void draw(Graphics g){
    g.setColor(Color.RED);
    g.fillRect((int)x, (int)y, width, height);
  }
}
