import java.awt.*;

public class Bullets extends GameObject{
  
  private int width = 5, height = 8;
  
  public Bullets(int x, int y, ID id){
    super(x, y, id);
    vx = MouseInfo.getPointerInfo().getLocation().x - x;
    vy = MouseInfo.getPointerInfo().getLocation().y - y;
  }

  @Override
  public Rectangle getRect() {
    return new Rectangle(x, y, width, height);
  }

  @Override
  public void update(boolean[] keys) {
    move();
  }

  public void move() {
    x += vx;
    y += vy;
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(Color.GREEN);
    g.fillOval(x, y, width, height);
  }
  
}
