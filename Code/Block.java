import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle; 
//This class handles the environment(barriers)
public class Block extends GameObject{
  
  private int width = 32, height = 32, health = 30;
  private Handler handler;
  
  //Constructor class for Block
  public Block(int x, int y, ID id){
    super(x, y, id);
  }

  //Gets hitbox
  @Override
  public Rectangle getRect() {
    return new Rectangle((int)x,(int) y, width, height);
  }

  public  void getHurt(int damage){
    health -= damage;
  }

  public int getHealth() {
      return health;
  }  

  

  



  // @Override
  // public void update(boolean[] keys){
  //   if(health<=0){
  //     System.out.println("bye bye");
  //     handler.removeObject(this);
  //     // System.out.println("bye bye");
  //   }
  // }



  


  //Draws block on screen
  @Override
  public void draw(Graphics g) {
    g.setColor(Color.BLACK);
    if(health<=0){
      width = 0;
      height = 0;
    }
    g.fillRect((int)x, (int)y, width, height);
  }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }




  
}