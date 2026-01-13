import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;

public class Crate extends GameObject{

    private Handler handler;
    BufferedImageLoader loader = new BufferedImageLoader(); 
    private BufferedImage crateImg = loader.loadBuffImg("Assets/item/chest1.png"); 
    private Player player;
    private int ammoInCrate = Util.randint(10,20);
    private int healthInCrate = Util.randint(5,15);
    private int width = 64, height = 64;
    private HashTable<Crate> crateTable = new HashTable<Crate>();
    // private String[] powerUP = {"Dash","Speed Boost","Increase Damage","Increase Fire Rate"};
    private String[] powerUP = {"Dash", "SuperSpeed", "Wall"};
    private String[] guns = {"RPG"};
      


    public Crate(int x, int y, ID id, Handler handler, Player player) {
        super(x, y, id);
        this.handler = handler;
        this.player = player;

    }

    public void update() {

    }

    public Rectangle getRect(){
        return new Rectangle((int)x, (int)y, width, height);
    }

    public void giveLoot(){
        if(Util.randint(0,1) == 0){
            player.setAmmoCount(player.getAmmoCount() + ammoInCrate);
        }
        else{
                                    //Use get maxHealth if making health cap biggert
            if(player.getHealth() <= player.getMaxHealth() - healthInCrate){
             player.setHealth(player.getHealth() + healthInCrate);
            }
            else{
              player.setHealth(player.getHealth() + (player.getMaxHealth() - player.getHealth()));
            }
        }
        
        handler.removeObject(this);
    }

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

private HashTable<Crate> checkCrateCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Crate && obj != this){
            if(this.getRect().intersects(obj.getRect())){
                return crateTable;
            }
        }
    }
    return null;
}

    @Override

public void draw(Graphics g){
    g.drawImage(crateImg, (int) x, (int) y, null);
  }

  @Override
  public int hashCode(){
    return ((int)x * 1000 + (int)y);
  }
  

}


