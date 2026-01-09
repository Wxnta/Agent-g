/* Enemy.java
   This class is meant to handle all enemy functions
   Authors: Ayham Genawi, Mateen Bakare
   */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Enemy extends GameObject{
  // variables
  private int speed;
  private int width = 50, height = 50;
  private int health = 100;
  private Player player;


  Handler handler;

  public Enemy(int x, int y, ID id, Handler handler, Player player){
    super(x, y, id);
    this.handler = handler;
    this.player = player;
  }

  public void update(boolean[] keys){
    if(health<=0){
      handler.removeObject(this);
    }

    move();
    
  }

 public void move(){
  double px = player.getX();
  double py = player.getY();

  System.out.println("Player positions ("+px+","+py+") and enemy positions ("+x+","+y+")");
  if(px > x){
    x +=  1;
    if(checkWallCollision()){
       x -= 1;
      }
  }

  if(px < x){
    x -= 1;
    if(checkWallCollision()){
      x += 1;
    }
  }

  if(py > y){
    y += 1;
    if(checkWallCollision()){
      y -= 1;
    }
  }

  if(py < y){
    y -= 1;
    if(checkWallCollision()){
      y += 1;
    }
  }  







  

    
 }
 
  private boolean checkWallCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Block ){
            if(this.getRect().intersects(obj.getRect())){
                return true;
            }
        }
    }
    return false;
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
