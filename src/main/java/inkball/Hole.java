package inkball;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Hole {
    public PVector position;
    private float x;            // X position of the hole
    private float y;            // Y position of the hole
    private PImage sprite;      // Image for the hole
    private String colour;
    public static final int SIZE = App.CELLSIZE; // Size of the hole

    /**
     * <p>
     * This method instantialises the Hole object based on certain parameters.
     * </p>
     * @param x is the x value in which the hole will be initialised to
     * @param y is the y value in which the hole will be initialised to
     * @param colour is the colour in which the hole will be initialised to
     * @param app is the PApplet app instance that controls the game and it's logic and graphics
     */

    public Hole(float x, float y, String colour, PApplet app) {
        this.x = x; // Set x position
        this.y = y; // Set y position
        this.position = new PVector(x + SIZE, y + SIZE);
        this.colour = colour;
        if (this.colour.equals("grey")) {
          this.sprite = app.loadImage("src/main/resources/inkball/hole0.png");
      } else if (this.colour.equals("orange")) {
          this.sprite = app.loadImage("src/main/resources/inkball/hole1.png");
      } else if (this.colour.equals("blue")) {
          this.sprite = app.loadImage("src/main/resources/inkball/hole2.png");
      } else if (this.colour.equals("green")) {
          this.sprite = app.loadImage("src/main/resources/inkball/hole3.png");
      } else if (this.colour.equals("yellow")) {
          this.sprite = app.loadImage("src/main/resources/inkball/hole4.png");
      }
    }

    /**
     * <p>
     * This method draws each sprite but ensures that the sprite is drawn appropriately where the centre of the hole is.
     * </p>
     */
    public void draw(PApplet app) {
        app.image(sprite, x, y); // Draw the hole image
    }

    /**
     * <p>
     * This is a simple getter method for the x position of the hole
     * </p>
     * @return returns the x position of the hole
     */
    public float getX() {
        return this.x; // Return the x position
    }

    /**
     * <p>
     * This is a simple getter method for the y position of the hole
     * </p>
     * @return returns the y position of the hole
     */
    public float getY() {
        return this.y; // Return the y position
    }

    /**
     * <p>
     * This is a simple getter method for the colour of the hole
     * </p>
     * @return returns the colour of the hole
     */
    public String getColour() {
        return this.colour;
    }
    /**
     * <p>
     * This is a simple getter method for the position of the hole
     * </p>
     * @return returns the position of the hole
     */
    public PVector getPosition() {
        return this.position;
    }
    
    /**
     * <p>
     * This method checks if the ball is in the hole by comparing the distance between the ball and the hole
     * to the half the size of a tile (32/2 = 16 pixels)
     * </p>
     * @param ball
     * @return
     */
    public boolean isBallInHole(Ball ball) {
        float distance = PApplet.dist(ball.getPosition().x, ball.getPosition().y, x + SIZE, y + SIZE);
        return distance < (SIZE/2); // Check if the ball is within the radius of the hole
    }
}
