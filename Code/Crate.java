import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.ArrayList;

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
    private ArrayList<String> powerUP = new ArrayList<>(Arrays.asList("Dash", "SuperSpeed"));
    private ArrayList<String> guns = new ArrayList<>(Arrays.asList("RPG"));
    private ArrayList<String> refresh = new ArrayList<>(Arrays.asList("HP","Ammo"));
     private ArrayList<String> lootbox = new ArrayList<String>();
      


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
        //Removes RPG from lootpool if we already have it
        if(player.getGuns().contains("RPG")){
            guns.remove("RPG");
        }
        if(player.getGuns().contains("RPG") == false && guns.contains("RPG") == false){
            guns.add("RPG");
        }

        //Removes dash from lootpool if we already have it
        if(player.getPowerups().contains("Dash")){
            powerUP.remove("Dash");
        }

        if(player.getPowerups().contains("SuperSpeed")){
            powerUP.remove("SuperSpeed");
        }
        if(player.getPowerups().contains("SuperSpeed") == false && powerUP.contains("SuperSpeed")){
            powerUP.add("SuperSpeed");
        }

        if(player.getHealth() == player.getMaxHealth()){
            refresh.remove("HP");
        }
        else if(player.getHealth()<= player.getMaxHealth() && refresh.contains("HP") == false){
            refresh.add("HP");
        }
        lootbox.clear();


        //Guranteed ammo if you don't have none
        if(player.getAmmoCount() == 0){
            player.setAmmoCount(player.getAmmoCount() + ammoInCrate); 
        }else{
        //1 in 4 chance to get a gun

        if(Util.randint(0, 4) == 2 && guns != null){
            lootbox.addAll(guns);
        }

        //If not
        else{
            //1 in 3 chance to get a powerup
            if(Util.randint(0, 7) == 0 && powerUP != null){
            lootbox.addAll(powerUP);
            }
            //if not 2/3 chance to get a refresh
            else{
                lootbox.addAll(refresh);
            }
        }
        
        // Make sure lootbox is not empty
        if(lootbox.isEmpty()){
            player.setAmmoCount(player.getAmmoCount() + ammoInCrate);
           // System.err.println("Pitty Ammo");
            handler.removeObject(this);
            return;
        }
        
        int randNum = Util.randint(0, lootbox.size()-1);
        
            System.err.println("Treasure: " + lootbox.get(randNum));
            if(lootbox.get(randNum).equals("Ammo")){
                player.setAmmoCount(player.getAmmoCount() + ammoInCrate); 
                
            }
            if(lootbox.get(randNum).equals("HP")){
                if(player.getHealth() <= player.getMaxHealth() - healthInCrate){
                    player.setHealth(player.getHealth() + healthInCrate);
                    }
                    else{
                    player.setHealth(player.getHealth() + (player.getMaxHealth() - player.getHealth()));
                    }
            
            }
            if(lootbox.get(randNum).equals("Dash")){
                player.addPowerup("Dash");
                
            }
            if(lootbox.get(randNum).equals("SuperSpeed") ){
                player.addPowerup("SuperSpeed");
                
            }
            if(lootbox.get(randNum).equals("RPG")){
                player.addGuns("RPG");
                player.setRpgAmmo(3);
                
            }
            
            
        


        // if(Util.randint(0,1) == 0){
        //     //player.setAmmoCount(player.getAmmoCount() + ammoInCrate);
        // }
        // else{
        //                             //Use get maxHealth if making health cap biggert
        //     if(player.getHealth() <= player.getMaxHealth() - healthInCrate){
        //      player.setHealth(player.getHealth() + healthInCrate);
        //     }
        //     else{
        //       player.setHealth(player.getHealth() + (player.getMaxHealth() - player.getHealth()));
        //     }
        // }
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


