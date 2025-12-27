/* ----------------------------------------------------------------------------------
 * Sprite.java
 * Mr. McKenzie
 * December 15 2022
 *
 * use BaseFrame for Sprite
*/


import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

class Sprite extends BaseFrame{
 Player player;
 
 public Sprite(){
  super("Sprite", 800,600);
  player = new Player(500,400); 
 } 
 
 public void move(){
  player.move(keys);
 }
 
 @Override
 public void draw(Graphics g){
  if(player==null)return;
  g.setColor(Color.WHITE);
  g.fillRect(0,0,800,600);
  player.draw(g); 
 }
 
 public static void main(String[] args) {
  new Sprite();
    } 
}




