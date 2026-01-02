import java.awt.image.BufferedImage;
public class Camera {
    private int x, y;
    private BufferedImage lvl;
    public Camera(int x, int y, BufferedImage lvl) {
        this.x = x;
        this.y = y;
        this.lvl = lvl;

    }

    public void update(Player player) {
      //lock coordinates to player centerA
        this.x = player.getX() + player.getWidth() /2 - Main.WIDTH / 2;
        this.y = player.getY() + player.getHeight() / 2 - Main.HEIGHT / 2;
        int lvlWidth = lvl.getWidth() * 32;
        int lvlHeight = lvl.getHeight() * 32;

        //CLAMP CAMERA TO LEVEL BOUNDS
        if(x <=0) x = 0;
        if(x >= lvlWidth - Main.WIDTH) x = lvlWidth - Main.WIDTH;
        if(y <=0) y = 0;
        System.out.println(y);
        if(y >= 350) y =  350;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}