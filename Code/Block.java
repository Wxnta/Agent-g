import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle; 
//This class handles the environment(barriers)
public class Block extends GameObject{
  
  private int width = 32, height = 32;
  
  //Constructor class for Block
  public Block(int x, int y, ID id){
    super(x, y, id);
  }

  //Gets hitbox
  @Override
  public Rectangle getRect() {
    return new Rectangle((int)x,(int) y, width, height);
  }


  //Draws block on screen
  @Override
  public void draw(Graphics g) {
    g.setColor(Color.BLACK);
    g.fillRect((int)x, (int)y, width, height);
  }


  
}