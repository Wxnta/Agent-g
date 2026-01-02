import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle; 

public class Block extends GameObject{
  
  private int width = 32, height = 32;
  
  public Block(int x, int y, ID id){
    super(x, y, id);
  }

  @Override
  public Rectangle getRect() {
    return new Rectangle(x, y, width, height);
  }

  @Override
  public void update(boolean[] keys) {
    // Blocks are static; no update needed
  }

  @Override
  public void draw(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect(x, y, width, height);
  }


  
}