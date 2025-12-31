import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends GameObject{

 private int speed = 5;
 private static final int width = 50, height = 50;
 Handler handler;

 //private boolean moving;
 //public static final int LEFT = 0, RIGHT = 1, WAIT = 2;
  
 public Player(int x, int y, String type, Handler handler){
  super(x, y, type);
  this.handler = handler;
 }

 @Override
 public void update(boolean []keys){
  move(keys);
 }

 public void move(boolean []keys){
   
  if(keys[KeyEvent.VK_D]  && x < 1200 - width){
    x += 5;
  }
  else if(keys[KeyEvent.VK_A]&& x > 0 ){
  
   x -= 5;
  
  }
  
  if(keys[KeyEvent.VK_W] && y > 0){
   y -= 5;
  
  }
  else if(keys[KeyEvent.VK_S] && y < 800 - height){
   y += 5;
   
  }
 }
  @Override
 public Rectangle getRect(){
  return null;
 }


 @Override
 public void draw(Graphics g){  
  g.setColor(Color.RED);
  g.fillRect(x, y, width, height);
 }
}