import java.awt.Graphics;
import java.awt.Rectangle;

//I needed help online to understand how to make a base class for game objects
//It's abstract because it is meant to be extended by other classes
public abstract class GameObject {
  //protected because it is inherited by other classes
  protected double x,y,vx,vy;
  protected String type;
  protected ID id;
  
    //Constructor for GameObject
  public GameObject(int x, int y, ID id){
    this.x = x;
    this.y = y;
    this.id = id;
  }

  public abstract Rectangle getRect();

  public void update(boolean []keys){
    
  }

  public void draw(Graphics g) {

   }

  public ID getId() {
    return id;
  }

  public void setId(ID id) {
    this.id = id;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getVx() {
    return vx;
  }

  public void setVx(double vx) {
    this.vx = vx;
  }

  public double getVy() {
    return vy;
  }

  public void setVy(double vy) {
    this.vy = vy;
  }

  
}