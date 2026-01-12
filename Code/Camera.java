import java.awt.image.BufferedImage;
//This handles the players top down POV
public class Camera {
    private double x, y;
    private BufferedImage lvl;

    //Camera Constructor
    public Camera(double x, double y, BufferedImage lvl) {
        this.x = x;
        this.y = y;
        this.lvl = lvl;

    }

    //Update camera's movement
    public void update(Player player) {
      //lock coordinates to player centerA
        this.x = (player.getX() + player.getWidth() /2 - Main.WIDTH / 2) * 0.75f;
        this.y = (player.getY() + player.getHeight() / 2 - Main.HEIGHT / 2) * 0.75f;
        int lvlWidth = lvl.getWidth() * 32;
        int lvlHeight = lvl.getHeight() * 32;

        //CLAMP CAMERA TO LEVEL BOUNDS
        if(x <=0) x = 0;
        if(x >= lvlWidth - Main.WIDTH) x = lvlWidth - Main.WIDTH;
        if(y <=0) y = 0;
        
        if(y >= 350) y =  350;
    }

    //Gets camera X
    public int getX() {
        return (int)x;
    }

    //Gers camera y
    public int getY() {
        return (int)y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}