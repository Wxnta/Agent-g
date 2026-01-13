/* Enemy.java
   This class is meant to handle all enemy functions: movement, damage, drawing
   Authors: Ayham Genawi, Mateen Bakare
   */


import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.image.*;

public class Enemy extends GameObject{
  // variables
  private int speed, counter, index;
  private int width = 50, height = 50;
  private int health = 100;
  private int damage = 10;
  private int frameCount = 0;
  private int playerGraceTime = 100; // frames of invincibility after hitting player
  public boolean canDamage = true;
  private Player player;
  private BufferedImageLoader loader = new BufferedImageLoader(); 
  // private BufferedImage enemyImg = loader.loadBuffImg("Assets/characters/Base Zombie/0.png"); 

  Handler handler;
  //Constructer for enemy
  public Enemy(int x, int y, ID id, Handler handler, Player player){
    super(x, y, id);
    this.handler = handler;
    this.player = player;
  }

  //updates movement and if it exists
  public void update(){
    frameCount ++;

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
  double dx = px-x;
  double dy = py-y;
  double dist = Math.sqrt(dx*dx+dy*dy);
  speed = 2;
  double vx = dx/dist * speed;
  double vy = dy/dist * speed;

  x+=vx;
  y+=vy;
  if(checkWallCollision()){
    x-=vx;
    y-=vy;
  }
  

  // System.out.println("Player positions ("+px+","+py+") and enemy positions ("+x+","+y+")");
  // if(px > x){
  //   x +=  1/dist * speed;
  //   if(checkWallCollision()){
  //      x -= 1/dist * speed;
  //     }
  // }

  // if(px < x){
  //   x -= 1/dist *speed ;
  //   if(checkWallCollision()){
  //     x += 1/dist *speed ;
  //   }
  // }

  // if(py > y){
  //   y += 1/dist *speed;
  //   if(checkWallCollision()){
  //     y -= 1/dist *speed;
  //   }
  // }

  // if(py < y){
  //   y -= 1/dist *speed;
  //   if(checkWallCollision()){
  //     y += 1/dist *speed;
  //   }
  // }
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

  public  int getWidth() {
    return width;
  }
 
 //Gets Height
  public  int getHeight() {
    return height;
  }

  private double findAngle(){
  int pX = (int) player.getX();  // shoild i make these local variables?
  int pY = (int) player.getY();

  int ex = (int)getX() + getWidth()/2;
  int ey = (int)getY() + getHeight()/2;

  double radians = Math.atan2(pY-ey, pX-ex);


    // System.out.println(radians);
  //   double degrees = Math.toDegrees(radians);
  return radians;
  }

  

  //Draws enemy
  public void draw(Graphics g){
    counter++;  // handling zombie sprite animation
    if(counter<5){          
        index = 0;
    }
    else if(counter<10){
        index = 1;
    }      
    else if(counter<15){
        index = 2;
    }
    else if(counter<20){
        index = 3;
    }
    else if(counter<25){
        index = 4;
    } 
    else if(counter<30){          
        index = 5;
    }
    else if(counter<35){
        index = 6;
    }      
    else if(counter<40){
        index = 7;
    }
    else if(counter<45){
        index = 8;
    }
    else if(counter<50){
        index = 9;
    } 
    else if(counter<55){
        index = 10;
    }
    else if(counter<60){
        index = 11;
    } 
    else if(counter<65){          
        index = 12;
    }
    else if(counter<70){
        index = 13;
    }      
    else if(counter<75){
        index = 14;
    }
    else if(counter<80){
        index = 15;
    }
    else{
      counter = 0;
    }

    

    String path = "Assets/characters/BaseZombie/" + index + ".png";
    BufferedImage enemyImg = loader.loadBuffImg(path);          
    double angle = findAngle();
    AffineTransform rot = new AffineTransform();
    rot.rotate(angle,25, 25);       // 75,84 is the center of my Image, this is the point of rotation.
    AffineTransformOp rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR);
    Graphics2D g2D = (Graphics2D)g;
    g2D.drawImage(enemyImg,rotOp, (int) x, (int) y);   
    // g.setColor(Color.RED);
    // g.drawRect((int)x, (int)y, width, height);
  }
}

