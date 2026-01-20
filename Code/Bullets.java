import java.awt.*;
import javax.swing.*;
//Bullets
//Controls Movement, Dameage and Type of Bullet(From diff Guns)
public class Bullets extends GameObject{
  
  private int width = 20, height = 20;
  private double  speed = 10;
  Handler handler;
  private int damage = 20; //How much damage our bullets
  // private int superStrengthTime = 0;
  private Player player;
  private Enemy shootingEnemy;
  String type;

  //For RPG
  private boolean isExploded = false; //checks if bullets hits a wall, for it to stay there
  
  private Explosion explosion;

  


  private Image bulletImg ; 
  
  
  //Bullets Constructor class

  public Bullets(int x, int y,int targetX, int targetY, ID id, Handler handler, Player player){
    super(x, y, id);
    //Calculate distance to Mouse Target
    double dx = targetX-x;
    double dy = targetY-y;
    //Calculate the line
    double hyp = Math.sqrt((dx*dx)+(dy*dy));
    vx = (dx/hyp) * speed ;
    vy = (dy/hyp)  * speed;
    this.handler = handler;
    this.player = player;
    type = "Player";
  }


  public Bullets(int x, int y,int targetX, int targetY, ID id, Handler handler, Enemy enemy){
    super(x, y, id);
    //Calculate distance to Mouse Target
    double dx = targetX-x;
    double dy = targetY-y;
    //Calculate the line
    double hyp = Math.sqrt((dx*dx)+(dy*dy));
    vx = (dx/hyp) * speed ;
    vy = (dy/hyp)  * speed;
    this.handler = handler;
    shootingEnemy = enemy;
    type = "shootingEnemy";
  }  


  //Gets bullet hitbox
  @Override
  public Rectangle getRect() {
    return new Rectangle((int)x, (int)y, width, height);
  }

  //Handles the movement, deletes it if it hits a wall or enemies
  @Override
  public void update() {
      move();
    
      
      if(checkWallCollision()){

         if(type.equals("Player")){
        if(player.getCurrentGun().equals("RPG")){
          
            
          explosion = new Explosion ((int)x-100,(int)y-100,id.Explosion, handler);
          handler.addObject(explosion);
          
          player.setRpgAmmo(player.getRpgAmmo() - 1);
            
          
  
    }
        handler.removeObject(this);
        
 
      }
      
}

  if(type.equals("Player")){
       if(player.getCurrentGun().equals("RPG")){
       Enemy enemy = checkEnemyCollision();
        
        if(enemy != null){

            explosion = new Explosion ((int)x-100,(int)y-100,id.Explosion, handler);
            handler.addObject(explosion);
          
           player.setRpgAmmo(player.getRpgAmmo() - 1);

    }
  }
  }

    

      // if(player.getPowerups().contains("SuperStrength")){
      //   superStrengthTime ++;
      //   System.err.println(superStrengthTime);
      //   player.setSuperStrengthTime(superStrengthTime);
      //   if(superStrengthTime >= 1000){
      //     player.getPowerups().remove("SuperStrength");
      //   }
      // }
      
      //Damage enemy
      if(type.equals("Player")){

        Enemy enemy = checkEnemyCollision();
        
        if(enemy != null){
          if(player.getCurrentGun().equals("Tranquilizer")){
            if(enemy.getId() == ID.Enemy){
              enemy.setIsFrozen(true);
            enemy.getHurt(damage/2);
            //Removes bullets
            player.setFreezeAmmo(player.getFreezeAmmo() - 1);
            handler.removeObject(this);
          }
          
        }
          if(enemy.getId() == ID.Enemy){
            enemy.getHurt(damage);
            //Removes bullets
            handler.removeObject(this);
          }
        }

        

        if(player.getPowerups().contains("SuperStrength")){
          damage = 40;
        }
        else{
          damage = 20;
        }
    }
    else{
      Player player = checkPlayerCollision();
        
      if(player != null){
        if(player.getId() == ID.Player){
          player.getHurt(20);
          //Removes enemy
          handler.removeObject(this);
        }
      } 
      
      Enemy friendEnemy = checkEnemyCollision();
        
        if(friendEnemy != null){
          if(friendEnemy.getId() == ID.Enemy){
            friendEnemy.getHurt(5);
            //Removes enemy
            handler.removeObject(this);
          }
        }
    }
      
      //System.out.println("Enemy Hit: ");


      // System.out.println("Enemy health: "+ enemy.getHealth());
      
  }
  
  // Gets damage, the bullets does
  public int getDamage() {
 
    return damage;
  }       


  // Sets the damage, the bullets does
  public void setDamage(int damage) {
    this.damage = damage;
  }


  //Checks if bullets collide with enemy


  // Handle bullets movement
  public void move() {
    if(!isExploded){
     x += vx;
     y += vy;
    }
    else{
      x += 0;
     y += 0;
    }
  }



  //Draws the bullets on screen
  @Override
  public void draw(Graphics g) {
    if(type.equals("Player")){
      bulletImg  = new ImageIcon("Assets/weapons/shoot/1.png").getImage();
    }
    else{
      bulletImg  = new ImageIcon("Assets/weapons/shoot/9.png").getImage();
    }
    g.drawImage(bulletImg,(int)x,(int)y, null);

  }

  //Check for the wall collision
  private boolean checkWallCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Block){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
                return true;
            }
        }
    }
    return false;
}


//Checks if bullets collide with enemy
private Enemy checkEnemyCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Enemy){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
               if(type.equals("shootingEnemy") && shootingEnemy != obj){
                return (Enemy) obj;
               }
               if(type.equals("Player")){
                return (Enemy) obj;
               }
            }
        }
    }
    return null;
}

private Player checkPlayerCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Player){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
                return (Player) obj;
            }
        }
    }
    return null;
}


}


 

