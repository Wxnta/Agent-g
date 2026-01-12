//HashTable.java
//Mateen Bakare
//Our program created a hashtable that quickly searches in 0(1) notation(most of the time).


import java.io.*;
import java.util.*;

class HashTable<T> {
 private ArrayList<LinkedList<T>> table; //Creates an arrayList of linkedList(of a certain value)
 private int items;
 private double maxLoad = 0.7;
 public static boolean DEBUG = false;
 
 //HashTable Constructor
 public HashTable(){
  table = new ArrayList<LinkedList<T>>(); // creates the new hashtable
  items = 0;
  for(int i=0; i< 10; i++){ //Add 10 empty spots to the table(array list)
   table.add(null);
  }
 }
 
 //Resizes the Hashtable if it gets above maxLoad
 private void resize(){
   //create a new Linked list to store old values
  ArrayList<LinkedList<T>>old = table; 
  //table is now a new linked list
  table = new ArrayList<LinkedList<T>>();

  //adds an empty new list with 10x the value now
  for(int i=0; i< old.size() * 10; i++){
   table.add(null);
  }
  
  items = 0;//reset how many things are in it
  
  for(LinkedList<T>lst : old){ //For every linked list in the old table
   if(lst != null){//if there is something in the linked list
    for(T val : lst){ //for every linked list connected , we add it
     add(val); 
    }
   }
  }
 }
 
 //Adds things into the hashtable
 public void add(T val){
  int hash = Math.abs(val.hashCode()); //turns the values's hashcode into a normal number(NOT NEGATIVE)
  int pos = hash % table.size(); //gets the final number of hash chode(where we store position)
  
  LinkedList<T>lst = table.get(pos); //creates a linked list at the new table position
  //If there is noting in the arrayList
  if(lst == null){
   lst = new LinkedList<T>(); //I create a new linked list
   
   table.set(pos, lst); /// I put the linked list in the correct position
  }
  lst.add(val); //Add the real value into it
  items++; //keeps track of how many item in the arrayList
  // if load > 70%, resize
  double load = (double)items / table.size(); //calculate the percentage filled
  if(load > maxLoad){
   resize(); //Resize it
  }
   
 }
 
 //Removes your specified item from the linked list
 public void remove (T val){
   //Finds the position of what we want to remove
   int hash = Math.abs(val.hashCode()); 
   int pos = hash % table.size(); 
    
   LinkedList<T>lst = table.get(pos);
   if(lst == null)return;
     
   //removes
   lst.remove(val);
   items --;
   
 }
 
 //Checks if object is in hashtable reutrns true orfalse
 public boolean contains(T val){
   //Finds position using hashcode
   int hash = Math.abs(val.hashCode()); 
   int pos = hash % table.size(); 
   
   LinkedList<T>lst = table.get(pos);
   //returns false if the linked list is empty
   if(lst == null)return false;
   //if the linked list contains the value it returns true
   if(lst.contains(val)) return true;
   return false;
 }
 
 //Returns the item/spots percentage
 public double getLoad(){
   return (double)items / table.size(); //calculate the percentage filled
 }
 
 //Let's us change at what point we want to resize
 public void setMaxLoad(double percent){
   //if its outside the boundary don't do anything
   if(percent < 0.1 || percent > 0.8)return;
   //if the load is over the maxLoad we resize
   if(getLoad() > percent){
     maxLoad = percent;
     resize();
   }
   
 }
 
 //Allows us to specify our load
 public void setLoad(double percent){
   if(percent < 0.1 || percent > 0.8)return;
   
   if(percent > maxLoad) return;
   double newSize = items/percent;
   //System.out.println(newSize);
   
   //create a new Linked list to store old values
   ArrayList<LinkedList<T>>old = table; 
   //table is now a new linked list
   table = new ArrayList<LinkedList<T>>();
   
   //adds an empty new list with 10x the value now
   for(int i=0; i< newSize; i++){
     table.add(null);
   }
   
   items = 0;//reset how many things are in it
   
   for(LinkedList<T>lst : old){ //For every linked list in the old table
     if(lst != null){//if there is something in the linked list
       for(T val : lst){ //for every linked list connected , we add it
         add(val); 
       }
     }
   }

 }
 
 //Returns an array list tbat includes all the objects in the hashtable
 public ArrayList<T> toArray(){
   //creates the arrayList
   ArrayList<T> aList = new ArrayList<T>();
   
   //Goes through all linked list in the table
   for(LinkedList<T>lst : table){ 
     if(lst != null){
       for(T val : lst){ 
         aList.add(val); 
       }
     }
   }
   
   return aList;
   
 }
 
 //Checks for the people with the exact same hashCode
 public T samePos(int hash){
   int pos = hash % table.size(); 
   LinkedList<T>lst = table.get(pos);
   
   if(lst != null){
     //Loops through the linked list to find if something has the exact same hashcode
     for(T val : lst){
       if(val.hashCode() == hash){
         return val;
       }
     }
   }
   return null;
 }
 
 //Returns the string version of the hashtable
 @Override
 public String toString(){
  String ans = "";
  for(LinkedList<T>lst : table){
   if(lst != null){
    for(T val : lst){
     ans += ", " + val; 
    }
   }
  }
  if(ans != ""){
   ans = ans.substring(2);
  }
  return "/" + ans + "\\";
 }
}

