/* Enemy.java
   This class is meant to handle all enemy functions: movement, damage, drawing
   Authors: Ayham Genawi, Mateen Bakare
   */


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;

public class Enemy extends GameObject{
  // variables
  private int speed, counter, index;
  private int width = 40, height = 40;
  private int health = 100;
  private int damage = 10;
  // private Font fnt20 = Font.createFont(Font.TRUETYPE_FONT, new File("Assets/fonts/gameFont.otf")).deriveFont(20f);
  // private int boolean drawDamageIndicator = false;
  public static final int X = 1;
  public static final int Y = 2;
  private int frameCount = 0;
  private int playerGraceTime = 100; // frames of invincibility after hitting player
  private  int shooterEnemyBuffer = 0;
  private boolean canDamage = true;
  private int wave;
  private String type;
  private boolean attackingPlayer = false;
  private boolean breakDown = false;
  private Camera camera;
  private boolean isFrozen;
  private int frozenTime = 0;

  private Player player;
  private BufferedImageLoader loader = new BufferedImageLoader(); 
  // private BufferedImage enemyImg = loader.loadBuffImg("Assets/characters/Base Zombie/0.png"); 

  Handler handler;
  //Constructer for enemy
  public Enemy(int x, int y, ID id, Handler handler, Player player, int wave, String type, Camera camera){
    super(x, y, id);
    this.handler = handler;
    this.player = player;
    this.wave = wave;
    this.type = type;
    this.camera = camera;
  }

  //updates movement and if it exists

  public void update(){
    if(frameCount < playerGraceTime){
      frameCount ++;
    }
    //If enemy is frozen
    if(isFrozen){
      frozenTime ++;
    }
    if(isFrozen && frozenTime >= 250){
      isFrozen = false;
      frozenTime = 0;
    }

    Player player = checkPlayerCollision();
    if(health<=0){
      handler.removeObject(this);
    }

    move();
    //Spawns in wave 3 and shoots at us around every second(+ delay so they don't shoot all at exact time)
    if(type.equals("shootingEnemy") && wave>=3){
      shooterEnemyBuffer ++;
      if(shooterEnemyBuffer >= 50 && Util.randint(1, 20) == 1){
        shoot(camera);
        shooterEnemyBuffer = 0;
      }

    }


    if(frameCount % playerGraceTime == 0 && canDamage == false){
        canDamage = true;
         
  }

     
     
      if(canDamage){
       
        if(player != null && !isFrozen){
          if(player.getId() == ID.Player){ // is this usless?
            attackingPlayer = true;
            player.getHurt(damage);
            counter = 0;
            frameCount = 0;
            canDamage = false;

          }
          // frameCount = 0;
          // canDamage = false;
        }
        if (breakDown){
          
          // System.out.println("im breaking down");
          ArrayList<Block> blocks = checkBlockCollision();
          // System.out.println(block);
          if(blocks.size() > 0){
            attackingPlayer = true;
            for(Block block : blocks) {
              block.getHurt(damage);
              // System.out.println("Ouch!!!");
              if(block.getHealth() <= 0){
                handler.removeObject(block);
              }              

            }
            // block.getHurt(DAMAGE);
            // System.out.println("Ouch!!!");
            // if(block.getHealth() <= 0){
            //   handler.removeObject(block);
            // }
            counter = 0;
            frameCount = 0;
            canDamage = false;            
          }

          else{
            breakDown = false; // 
          }

          // frameCount = 0;
          // canDamage = false;
        }
       
    }

    if(wave == 2){
        damage = 12;
    }
    else if(wave == 3){
        damage = 15;
    }
    else if(wave == 4){
        damage = 17;
    }
    else if(wave == 5){
        damage = 20;
    }
    //  move();


  
    }

  public void shoot(Camera camera){
 
  int wmx = (int) player.x; //+ camera.getX();
  int wmy =  (int) player.y;// + camera.getY();
  int ex = (int)(x + width/2) ;
  int ey = (int)(y + height/2);
    // Spawn bullet a little in front of the player so SuperSpeed doesn't overlap it
  double dx = wmx - ex;
  double dy = wmy - ey;
  double hyp = Math.sqrt(dx*dx + dy*dy);
    // int spawnX = px;
    // int spawnY = py;
    // if(hyp != 0){
    //     double offset = player.getWidth() / 2.0 + 8; // place bullet just outside player's bounds
    //     spawnX = (int)(px + (dx / hyp) * offset);
    //     spawnY = (int)(py + (dy / hyp) * offset);
    // }

    handler.addObject(new Bullets(ex, ey, wmx, wmy, ID.Bullet, handler, this));
 }


 



  //Moves ememies towards player
 public void move(){
  double px = player.getX();
  double py = player.getY();
  double dx = px-x;
  double dy = py-y;
  double dist = Math.sqrt(dx*dx+dy*dy);
  if (dist == 0) return;

  speed = Math.min(wave + 1, 6);


  double vx = dx/dist * speed;
  double vy = dy/dist * speed;

// handles zombie movement 
  double startX = x;
  double startY = y;
  if(isFrozen){
    return;
  }
  if(!checkWallCollision(vx, 0) && !collideOthers(vx, 0))  {
    // System.out.println(collideOthers());
    x+=vx;
    // if(!checkWallCollision(vx, 0) & !collideOthers(0, vy) ){   // makes recovery fasrer
    //   y+=vy;
    // }
  }

  if(!checkWallCollision(0, vy)  && !collideOthers(0, vy)){
    y+=vy;

    // if(checkWallCollision(vx, 0)){
    //   y+=vy;
    // }
  }

  ArrayList<Block> collidingBlocks = checkBlockCollision();

  if(type.equals("attackingEnemy")){

  

  if (collidingBlocks.size()>0 && canDamage) {
     breakDown = true;
   }
   else {
    breakDown = false;
}


 }
}

 private boolean touches (Rectangle a, Rectangle b){
    if(a.x <= b.x+ b.width && a.x + a.width >= b.x && a.y <= b.y + b.height && a.y + a.height >= b.y){
      return true;
    }
    else{
      return false;
    }
}
 

 private boolean collideOthers(double vX, double vY){
  for(int i = 0; i< ((handler.object).size()) ; i++){
    GameObject obj = ((handler.object).get(i));
     if(obj.getId() == ID.Enemy){
      Rectangle enemyRect = obj.getRect();
      if(obj == this){
        continue;
      }
      // Rectangle enemyTwoRect = ((handler.object).get(i)).getRect();
      Rectangle currentEnemy = new Rectangle((int)(x+vX), (int)(y+vY), width, height);
      if(currentEnemy.intersects(enemyRect)){
        return true;
      }
    }
  }
  return false;
}




      /* 
      if(!enemyRect.equals(enemyTwoRect)){
        System.out.println("im getting here!");
        if(enemyRect.intersects(enemyTwoRect)){
          return true;
        }
      }
    }
  }
  return false;
}

*/

 
 //Check enemies collison with the wall, may merge with block potentially ~ Ayham Genawi , 1/17/2026
  private boolean checkWallCollision(double vX, double vY){
    for(GameObject obj : handler.object){
      Rectangle enRect = new Rectangle((int)(x+vX), (int)(y+vY), width, height);

        if(obj.getId() == ID.Block ){
          Rectangle blRect = obj.getRect();
          if(enRect.intersects(blRect)){
            return true;
          }
        }
      }
      return false;
    }
         


//   private boolean checkWallRightCollision(){
//     for(GameObject obj : handler.object){
//         if(obj.getId() == ID.Block ){
//             if((getX()+getWidth()) == obj.getX()){
//                 return true;
//             }
//         }
//     }
//     return false;
// }


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


// returns block enemy is coliding with
private ArrayList<Block> checkBlockCollision(){
    ArrayList<Block> collidingBlocks = new ArrayList<Block>();
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Block){
            if(touches(this.getRect(), obj.getRect())){
              collidingBlocks.add((Block)obj);

                // return (Block) obj;
            }
        }
    }
    return collidingBlocks;
}


  //Function for harming the enemy if hit by weapon
  public  void getHurt(int damage){
    health -= damage;
    // drawDamageIndicator = true;
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
    if(!isFrozen){
      counter++;  // handling zombie sprite animation
    }
    
    if(type.equals("attackingEnemy")){  
      if(!attackingPlayer){
      // index = (counter / 5 - 1) % 15;
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
    }

    else{
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
        else{
          counter = 0;
          attackingPlayer = false;
          // canDamage = false;
        }
      }    
  }

  else{
    index = (counter/5) % 20;

  }

    

    String path;
    if(type.equals("attackingEnemy")){
      if(attackingPlayer){
        path = "Assets/characters/BaseZombie/attack/" + index + ".png";
      }
      else{
        path = "Assets/characters/BaseZombie/move/" + index + ".png";
      }
  }

  else{
    path = "Assets/characters/ShootZombie/" + index + ".png";
  }
    

    BufferedImage enemyImg = loader.loadBuffImg(path);          
    double angle = findAngle();
    AffineTransform rot = new AffineTransform();
    
    rot.rotate(angle,width/2.0, height/2.0);    
     
    AffineTransformOp rotOp = new AffineTransformOp(rot, AffineTransformOp.TYPE_BILINEAR);
    Graphics2D g2D = (Graphics2D)g;
    g2D.drawImage(enemyImg,rotOp, (int) x, (int) y);   
    // g.setColor(Color.RED);
    // g.drawRect((int)x, (int)y, width, height);
    if(health<100){

    
    g.setColor(Color.RED);
    g.fillRect((int)x, (int)(y - 10), (int)((width)), 5);
    if(health > 40){
      g.setColor(Color.GREEN);
    }
    else{
      g.setColor(Color.YELLOW);
    }

    g.fillRect((int)x, (int)(y - 10), (int)((width * ((double)health / 100))), 5);
    g.setColor(Color.RED);
    g.drawRect((int)x, (int)(y - 10), (int)((width)), 5);
    // g.setFont(font);
  }
}

    public String getType() {
        return type;
    }

    public void setIsFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }
}




