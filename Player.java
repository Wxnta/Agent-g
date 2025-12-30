import java.awt.*;
import java.awt.event.*;

public class Player{
 private int x,y;
 private int speed;
 private static final int width = 50, height = 50;

 //private boolean moving;
 //public static final int LEFT = 0, RIGHT = 1, WAIT = 2;
  
 public Player(int xx, int yy){
  x = xx;
  y = yy;
  speed = 10;
 }

 public void move(boolean []keys){
  if(keys[KeyEvent.VK_D]  && x < 1200 - width){
   x += speed;
   //dir = RIGHT;
   //frame++;
  }
  else if(keys[KeyEvent.VK_A]&& x > 0 ){
   x -= speed;
   //dir = LEFT;
  // frame++;
  }
  
  if(keys[KeyEvent.VK_W] && y > 0){
   y -= speed;
   //dir = UP;
   //frame++;
  }
  else if(keys[KeyEvent.VK_S] && y < 800 - height){
   y += speed;
   //dir = DOWNN;
  // frame++;
  }
 }

 public void draw(Graphics g){  
  g.setColor(Color.RED);
  g.fillRect(x, y, width, height);
 }
}