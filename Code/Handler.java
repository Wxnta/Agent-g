import java.util.LinkedList;
// Manages our game objects tasks e.g updating for movement and drawing
//I needed help online to understand how to make a handler class to manage game objects
public class Handler {
    LinkedList<GameObject> object = new LinkedList<GameObject>();



    private boolean up = false, down = false, left = false, right = false;

    //Updates each object in the list
    public void update(boolean[] keys) {
      
    for (GameObject obj : object) {
        obj.update(keys);
    }
  }
  //Draws each object in the list
    public void draw(java.awt.Graphics g){
      
        for (GameObject obj : object) {
        obj.draw(g);
    }
    }


    //Adds a new object to the list
    public void addObject(GameObject newObject){
        this.object.add(newObject);
    }
    //Removes an object from the list
    public void removeObject(GameObject remObject){
        this.object.remove(remObject);
    }

        public boolean isUp() {
      return up;
    }

    public void setUp(boolean up) {
      this.up = up;
    }

    public boolean isDown() {
      return down;
    }

    public void setDown(boolean down) {
      this.down = down;
    }

    public boolean isLeft() {
      return left;
    }

    public void setLeft(boolean left) {
      this.left = left;
    }

    public boolean isRight() {
      return right;
    }

    public void setRight(boolean right) {
      this.right = right;
    }
}