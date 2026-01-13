import java.awt.*;
import javax.swing.ImageIcon;
//Bullets
//Controls Movement, Dameage and Type of Bullet(From diff Guns)
public class Bullets extends GameObject{
  
  private int width = 20, height = 20;
  private double  speed = 10;
  Handler handler;
  private int damage = 20; //How much damage our bullets
  Player player;

  //For RPG
  private boolean isExploded = false; //checks if bullets hits a wall, for it to stay there
  
  private Explosion explosion;

  

  private Image bulletImg = new ImageIcon("Assets/weapons/shoot/9.png").getImage(); 
  
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
         
        if(player.getGuns().contains("RPG")){
          
         explosion = new Explosion ((int)x-100,(int)y-100,id.Explosion, handler);
         handler.addObject(explosion);
          
         
      }
        handler.removeObject(this);
        
         
        
      }
      
      //Damage enemy
      Enemy enemy = checkEnemyCollision();
      
      if(enemy != null){
        if(enemy.getId() == ID.Enemy){
          enemy.getHurt(damage);
          //Removes enemy
          handler.removeObject(this);
        }
      }
      
  }
  
  // Gets damage, the bullets does
  public int getDamage() {
    return damage;
  }       

  // Sets the damage, the bullets does
  public void setDamage(int damage) {
    this.damage = damage;
  }

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
   
    g.drawImage(bulletImg,(int)x,(int)y, null);

  }

  //Check for the wall collision
  private boolean checkWallCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Block){
            if(this.getRect().intersects(obj.getRect())){
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
            if(this.getRect().intersects(obj.getRect())){
                return (Enemy) obj;
            }
        }
    }
    return null;
}
  
}
