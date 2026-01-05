import java.awt.*;

public class Bullets extends GameObject{
  
  private int width = 5, height = 8;
  private int speed = 10;
  
  public Bullets(int x, int y,int targetX, int targetY, ID id){
    super(x, y, id);
    double dx = targetX-x;
    double dy = targetY-y;
    double hyp = Math.sqrt((dx*dx)+(dy*dy));
    vx = (dx/hyp) * speed;
    vy = (dy/hyp) * speed;
  }
  @Override
  public Rectangle getRect() {
    return new Rectangle((int)x, (int)y, width, height);
  }

  @Override
  public void update(boolean[] keys) {
    x += vx;
    x += vy;
  }

  public void move() {
    
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(Color.GREEN);
    g.fillOval((int)x,(int) y, width, height);
  }
  
}
