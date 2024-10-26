package inkball;

import processing.core.PImage;
import processing.core.PApplet;

public class Tile {
  private int x;
  private int y;
  private PImage sprite;
  private PApplet app;
  public int timerstep;
  private String colour;
  private String ogcolour;
  private int CELLSIZE = 32;

  /**
   * <p>
   * This method instantialises the Tile object based on certain parameters.
   * </p>
   * @param x is the x value in which the tile will be initialised to
   * @param y is the y value in which the tile will be initialised to
   * @param colour is the type of tile in which the tile will be initialised to
   * @param app is the PApplet app instance that controls the game and it's logic and graphics
   */
  public Tile(int x, int y, String colour, PApplet app) {
    this.x = x;
    this.y = y;
    this.app = app;
    this.colour = colour;
    this.ogcolour = colour;
    setSprite(colour);
  }

  /**
   * <p>
   * This method draws each sprite but ensures that the sprite is drawn appropriately where the centre of the hole is.
   * </p>
   */
  public void draw(PApplet app) {
    app.image(this.sprite, this.x, this.y);
  }

  /**
   * <p>
   * This is a simple getter method for the x position of the tile
   * </p>
   * @return returns the x position of the tile
   */
  public int getX() {
    return this.x;
  }

  /**
   * <p>
   * This is a simple getter method for the y position of the tile
   * </p>
   * @return returns the y position of the tile
   */
  public int getY() {
    return this.y;
  }
  /**
   * <p>
   * This is a simple getter method for the type of tile (colour)
   * </p>
   * @return returns the colour of the hole
   */
  public String getColour() {
    return this.colour;
  }

  /**
   * <p>
   * This is a simple getter method for the original colour of the tile
   * </p>
   * @return returns the ogcolour of the tile
   */
  public String getOgColour() {
    return this.ogcolour;
  }

  /**
   * <p>
   * This is a simple getter method for the sprite of the tile
   * </p>
   * @return returns the sprite of the tile
   */
  public PImage getSprite() {
    return this.sprite;
  }
  /**
   * <p>
   * This is a simple getter method for the timerstep of the tile
   * </p>
   * @return returns the timerstep of the tile
   */
  public void setTimerStep(int num) {
    timerstep = num;
  }
  /**
   * <p>
   * This is a method used to set the colour of a tile. Based on the colour parameter, the colour of the tile is
   * set based on the colour of certain sprites which are set to match the condition of the color.equals() condition.
   * </p>
   * @param colour is the colour in which you want to set the tile's colour.
   */
  public void setSprite(String colour) {
    this.colour = colour;
    // System.out.println(this.colour);
    if (this.colour.equals("grey")) {
      this.sprite = app.loadImage("src/main/resources/inkball/wall0.png");
    } else if (this.colour.equals("orange")) {
      this.sprite = app.loadImage("src/main/resources/inkball/wall1.png");
    } else if (this.colour.equals("blue")) {
      this.sprite = app.loadImage("src/main/resources/inkball/wall2.png");
    } else if (this.colour.equals("green")) {
      this.sprite = app.loadImage("src/main/resources/inkball/wall3.png");
    } else if (this.colour.equals("yellow")) {
      this.sprite = app.loadImage("src/main/resources/inkball/wall4.png");
    } else if (this.colour.equals("entry")) {
      this.sprite = app.loadImage("src/main/resources/inkball/entrypoint.png");
    } else if (this.colour.equals("empty")) {
      this.sprite = app.loadImage("src/main/resources/inkball/tile.png");
    } else if (this.colour.equals("hole")) {
      this.sprite = app.loadImage("src/main/resources/inkball/hole0.png");
    } else if (this.colour.equals("timer")) {
      this.sprite = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(CELLSIZE * 6 + 6, 1, CELLSIZE, CELLSIZE);
    } else {
      return;
    }
  }

  /**
   * <p>
   * This method increments timerstep by 1 and updates it's sprite.
   * </p>
   */
  public void becomeVisible() {
    if (this.colour.equals("timer")) {
      if (timerstep < 5) timerstep ++;
      timerSpriteUpdate();
    }
  }

  /**
   * <p>
   * This method decrements timerstep by 1 and updates it's sprite.
   * </p>
   */
  public void becomeInvisible() {
    if (this.colour.equals("timer")) {
      if (timerstep > 1) timerstep--;
      timerSpriteUpdate();
    }
  }

  /**
   * <p>
   * This method loads the appropriate sprite based on which stage of timerstep the program is at.
   * </p>
   */
  private void timerSpriteUpdate() {
    if (timerstep == 1) this.sprite = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(CELLSIZE * 6 + 6, 1, CELLSIZE, CELLSIZE);
    if (timerstep == 2) this.sprite = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(CELLSIZE * 7 + 7, 1, CELLSIZE, CELLSIZE);
    if (timerstep == 3) this.sprite = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(CELLSIZE * 8 + 8, 1, CELLSIZE, CELLSIZE);
    if (timerstep == 4) this.sprite = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(CELLSIZE * 9 + 9, 1, CELLSIZE, CELLSIZE);
    if (timerstep == 5) this.sprite = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(CELLSIZE * 10 + 10, 1, CELLSIZE, CELLSIZE);
  }

  /**
   * <p>
   * This method returns if the tile has collision. More specifically, if it passes these checks:
   * 1. If the tile is a timer it's timerstep is 5
   * 2. The type of the tile has to not be empty
   * 3. The type of the tile has to not be a hole
   * 4. The type of tile has to not be an entrypoint (spawner tile)
   * </p>
   * @return
   */
  public boolean hasCollision() {
    if ((this.colour.equals("timer") && timerstep < 5) || this.colour.equals("empty") || this.colour.equals("hole") || this.colour.equals("entry")) {
      return false;
    } else {
      return true;
    }
  }
}