/* ----------------------------------------------------------------------------------
 * Sprite.java
 * Mr. McKenzie
 * December 15 2022
 *
 * use BaseFrame for Sprite
*/


import java.awt.*;
import java.awt.image.BufferedImage;

class Main extends BaseFrame{
 private Handler handler;
 private Player player;
 private  BufferedImage lvl;
 
 public Main(){
  super("Main", 1200,800);
  handler = new Handler();

  player = new Player(500,400,"player", handler); 
  handler.addObject(player);
  BufferedImageLoader loader = new BufferedImageLoader();
  lvl = loader.loadBuffImg("../Assets/LvlImages/lvl1Image.png");
 } 
 

 public void update(){
  handler.update(keys);
 }
 
 @Override
 public void draw(Graphics g){
  if(player==null)return;
  g.setColor(Color.WHITE);
  g.fillRect(0,0,1200,800);
  handler.draw(g);
 }
 
 public static void main(String[] args) {
  new Main();
    } 
}




