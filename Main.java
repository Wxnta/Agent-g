/* ----------------------------------------------------------------------------------
 * Sprite.java
 * Mr. McKenzie
 * December 15 2022
 *
 * use BaseFrame for Sprite
*/


import java.awt.*;

class Main extends BaseFrame{
 Player player;
 
 public Main(){
  super("Main", 1200,800);
  player = new Player(500,400); 
 } 
 
 public void move(){
  player.move(keys);
 }
 
 @Override
 public void draw(Graphics g){
  if(player==null)return;
  g.setColor(Color.WHITE);
  g.fillRect(0,0,1200,800);
  player.draw(g); 
 }
 
 public static void main(String[] args) {
  new Main();
    } 
}




