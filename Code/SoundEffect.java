import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;

//Handles the sound effects
class SoundEffect {
  private Clip c;
  //gets the sfx
    public SoundEffect(String filename){
        setClip(filename);
    }
    public void setClip(String filename){
        try{
            File f = new File(filename);
            c = AudioSystem.getClip();
           c.open(AudioSystem.getAudioInputStream(f));
        } catch(Exception e){ System.out.println("error"); }
    }
    //plays the sfx in game
    public void play(){
        c.setFramePosition(0);
        c.start();
    }
    //stops the sfx in game
    public void stop(){
        c.stop();
    } 
}
