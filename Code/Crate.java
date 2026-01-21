
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.ImageIcon;

public class Crate extends GameObject{

    private Handler handler;
    BufferedImageLoader loader = new BufferedImageLoader(); 
    private BufferedImage crateImg = loader.loadBuffImg("Assets/item/chest1.png"); 
    private Image openCrateGIF = new ImageIcon("Assets/item/chestOpen.GIF").getImage();
    private Player player;
    private int ammoInCrate = Util.randint(10,20);
    private int healthInCrate = Util.randint(5,15);
    private int width = 64, height = 64;
    private HashTable<Crate> crateTable = new HashTable<Crate>();
    private int screenTime = 0;
    private boolean hitboxDisabled = false;
    private String lootMsg;
     private Font fnt20;
    // private String[] powerUP = {"Dash","Speed Boost","Increase Damage","Increase Fire Rate"};
    private ArrayList<String> powerUP = new ArrayList<>(Arrays.asList("Dash", "SuperSpeed","SuperStrength"));
    private ArrayList<String> guns = new ArrayList<>(Arrays.asList("RPG", "Tranquilizer"));
    private ArrayList<String> refresh = new ArrayList<>(Arrays.asList("HP","Ammo"));
     private ArrayList<String> lootbox = new ArrayList<String>();
      


    public Crate(int x, int y, ID id, Handler handler, Player player, int wave) {
        super(x, y, id);
        this.handler = handler;
        this.player = player;

    }

    public void update() {
        if(hitboxDisabled){
            screenTime++;
        }
        if(screenTime == 20){
            handler.removeObject(this);
        }
        
    }

    public Rectangle getRect(){
        if(hitboxDisabled) return null;
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

        if(player.getGuns().contains("Tranquilizer")){
            guns.remove("Tranquilizer");
        }
        if(player.getGuns().contains("Tranquilizer") == false && guns.contains("Tranquilizer") == false){
            guns.add("Tranquilizer");
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

        if(player.getPowerups().contains("SuperStrength")){
            powerUP.remove("SuperStrength");
        }
        if(player.getPowerups().contains("SuperStrength") == false && powerUP.contains("SuperStrength")){
            powerUP.add("SuperStrength");
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
            lootMsg = "+" + ammoInCrate + " Ammo ";
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
            lootMsg = "+" + ammoInCrate + " Ammo ";
            
            return;
        }
        
        int randNum = Util.randint(0, lootbox.size()-1);
        
            System.err.println("Treasure: " + lootbox.get(randNum));
            if(lootbox.get(randNum).equals("Ammo")){
                player.setAmmoCount(player.getAmmoCount() + ammoInCrate); 
                lootMsg = "+" + ammoInCrate + " Ammo ";
            }
            if(lootbox.get(randNum).equals("HP")){
                if(player.getHealth() <= player.getMaxHealth() - healthInCrate){
                    lootMsg = "+"  + healthInCrate + " HP";
                    player.setHealth(player.getHealth() + healthInCrate);
                    }
                    else{
                        lootMsg = "+"  + (player.getMaxHealth() - player.getHealth()) + " HP";
                    player.setHealth(player.getHealth() + (player.getMaxHealth() - player.getHealth()));
                    }
            
            }
            if(lootbox.get(randNum).equals("Dash")){
                player.addPowerup("Dash");
                lootMsg = "Dash Acquired";
            }
            if(lootbox.get(randNum).equals("SuperSpeed") ){
                player.addPowerup("SuperSpeed");
                lootMsg = "SuperSpeed Acquired";
                
            }
            if(lootbox.get(randNum).equals("SuperStrength") ){
                player.addPowerup("SuperStrength");
                lootMsg = "X2 Strength Acquired";
                
            }
            if(lootbox.get(randNum).equals("RPG")){
                player.addGuns("RPG");
                lootMsg = "+3 RPG Bullets";
                player.setRpgAmmo(3);
                
            }
            if(lootbox.get(randNum).equals("Tranquilizer")){
                player.addGuns("Tranquilizer");
                lootMsg = "+5 Tranquilizer Bullets";
                player.setFreezeAmmo(5);
                
            }
        }   
        
    }

    


private HashTable<Crate> checkCrateCollision(){
    for(GameObject obj : handler.object){
        if(obj.getId() == ID.Crate && obj != this){
            Rectangle myRect = this.getRect();
            Rectangle objRect = obj.getRect();
            if(myRect != null && objRect != null && myRect.intersects(objRect)){
                return crateTable;
            }
        }
    }
    return null;
}

    @Override

public void draw(Graphics g){
    if(!hitboxDisabled){
    g.drawImage(crateImg, (int) x, (int) y, null);
    }
    else{
        g.drawImage(openCrateGIF, (int) x, (int) y, null);
        g.setColor(Color.GREEN);
        //g.setFont();
        if(y > 100){
        g.drawString(lootMsg, (int)x+10, (int)y);
        }
        else{
            g.drawString(lootMsg, (int)x+10, (int)y+70);
        }
    }
  }

  @Override
  public int hashCode(){
    return ((int)x * 1000 + (int)y);
  }


    public void setHitboxDisabled(boolean hitboxDisabled) {
        this.hitboxDisabled = hitboxDisabled;
    }
  

}

