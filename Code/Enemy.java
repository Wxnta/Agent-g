/* Enemy.java
   This class is meant to handle all enemy functions: movement, damage, drawing
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
  private int damage = 10;
  private int frameCount = 0;
  private int playerGraceTime = 100; // frames of invincibility after hitting player
  public boolean canDamage = true;
  private Player player;


  Handler handler;
  //Constructer for enemy
  public Enemy(int x, int y, ID id, Handler handler, Player player){
    super(x, y, id);
    this.handler = handler;
    this.player = player;
  }

  //updates movement and if it exists
  public void update(boolean[] keys){
    frameCount++;
    //System.out.println("canDamage is "+canDamage);
    //ystem.err.println("frameCount: "+frameCount);
    Player player = checkPlayerCollision();
    if(health<=0){
      handler.removeObject(this);
    }

    move();

    if(frameCount % playerGraceTime == 0 && canDamage == false){
        canDamage = true;
         
  }
      
      
      if(canDamage){
        
        if(player != null){
          if(player.getId() == ID.Player){
            player.getHurt(damage);
          }
          frameCount = 0;
          canDamage = false;
        }
        
    }
      
    
  }

  //Moves ememies towards player
 public void move(){
  double px = player.getX();
  double py = player.getY();

  //System.out.println("Player positions ("+px+","+py+") and enemy positions ("+x+","+y+")");
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
 
 //Check enemies collison with the wall
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

//Checks if enemy collide with player
private Player checkPlayerCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Player){
            if(this.getRect().intersects(obj.getRect())){
                return (Player) obj;
            }
        }
    }
    return null;
}





  //Function for harming the enemy if hit by weapon
  public  void getHurt(int damage){
    health -= damage;
    if(health <= 0){
      handler.removeObject(this);
    }
  }
  
  //Gets enemy's health
  public int getHealth(){
    return health;
  }

  //Gets enemy's hitbox
  @Override
  public Rectangle getRect(){
    return new Rectangle((int)x, (int) y, width, height);
  }

  //Draws enemy
  public void draw(Graphics g){
    g.setColor(Color.RED);
    g.fillRect((int)x, (int)y, width, height);
  }
}

