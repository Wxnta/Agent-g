import java.awt.Graphics;
import java.awt.Rectangle;

//I needed help online to understand how to make a base class for game objects
//It's abstract because it is meant to be extended by other classes
public abstract class GameObject {
  protected int x,y,vx,vy;
  protected String type;
  
    //Constructor for GameObject
  public GameObject(int x, int y, String type){
    this.x = x;
    this.y = y;
    this.type = type;
  }

  public abstract Rectangle getRect();

  public void update(boolean []keys){
    
  }

  public void draw(Graphics g) {

   }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getVx() {
    return vx;
  }

  public void setVx(int vx) {
    this.vx = vx;
  }

  public int getVy() {
    return vy;
  }

  public void setVy(int vy) {
    this.vy = vy;
  }

  
}