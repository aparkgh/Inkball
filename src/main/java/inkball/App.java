package inkball;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.MouseEvent;

public class App extends PApplet {

    public String lastText = "";
    public boolean test;
    public int levelWinTime;
    private JSONObject config;
    public static final int CELLSIZE = 32;
    public static final int TOPBAR = 64;
    public static final int WIDTH = 576;
    public static final int HEIGHT = 640;
    public static final int FPS = 30;
    public static boolean paused;
    public static boolean timesup;
    public int greyscore;
    public int orangescore;
    public int bluescore;
    public int greenscore;
    public int yellowscore;
    public int score;
    private int scoretracker;
    public float timer;
    private int ballTrackOffset;
    private int ballTrackOffsetTracker;
    public List<List<Tile>> tiles;
    private ArrayList<Tile> timerTiles = new ArrayList<>();
    private ArrayList<Tile> spawnerTiles = new ArrayList<>();
    private List<List<PVector>> lines = new ArrayList<>();
    private List<PVector> currentLineBeingDrawn;
    public static List<Hole> holes = new ArrayList<>();
    public List<Ball> balls; // List to store all balls
    public boolean isDrawing;
    private double ballTicking;
    private long lastUpdated;
    private int ballInterval = 0; // Ball spawn interval
    public double scoreincrease;
    public double scoredecrease;
    public ArrayList<String> ballArray = new ArrayList<>();
    public ArrayList<String> ballsleft = new ArrayList<>();
    public int level = 1; // what level is the game on
    private int trackerThingy = 0;
    private char symbolnext;
    private char symbolbefore;
    private int numafter;
    private int numbefore;
    private PVector startPos;
    public PImage greyball;
    public PImage orangeball;
    public PImage blueball;
    public PImage greenball;
    public PImage yellowball;
    public boolean levelwin; // SHOULD START AS FALSE!
    public static final int GRID_WIDTH = 18;
    public static final int GRID_HEIGHT = 18;
    public int timerblockStartTimer = millis();
    public int timerblockSequenceTimer;
    public int timerblockRunCount;
    public boolean timerblockInSequence;
    public boolean transitioning;
    public boolean gameEnded;
    private Random random;
    private int currentTileIndex = 0;
    private List<Tile> outerTiles;
    private List<Tile> outerTiles2;
    private boolean isAnimating = false;

    public App() {
        this.tiles = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.holes = new ArrayList<>();
        this.balls = new ArrayList<>();
        this.random = new Random();
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * <p>
     * Initialises all the intial values including frameRate, how fast a ball is spawned, a variable used to track time and
     * the necessary data for the game which isn't individual to the level.
     * </p>
     * <p>
     * The game data which is individual to the level is loaded through a function called levelData which loads the data for each level
     * based on the 'level' variable.
     * </p>
     */

    @Override
    public void setup() {
        frameRate(FPS);
        ballTicking = ballInterval;
        lastUpdated = millis();
        config = loadJSONObject("config.json");
        
        levelData();
        JSONObject score_increase_from_hole_capture = config.getJSONObject("score_increase_from_hole_capture");
        greyscore = score_increase_from_hole_capture.getInt("grey");
        orangescore = score_increase_from_hole_capture.getInt("orange");
        bluescore = score_increase_from_hole_capture.getInt("blue");
        greenscore = score_increase_from_hole_capture.getInt("green");
        yellowscore = score_increase_from_hole_capture.getInt("yellow");
        greyball = loadImage("src/main/resources/inkball/ball0.png");
        orangeball = loadImage("src/main/resources/inkball/ball1.png");
        blueball = loadImage("src/main/resources/inkball/ball2.png");
        greenball = loadImage("src/main/resources/inkball/ball3.png");
        yellowball = loadImage("src/main/resources/inkball/ball4.png");
    }

    /**
     * <p>
     * Initialises the tiles of whichever level is loaded in levelLayout by row and column. It goes through the text file and loads each
     * tile with it's corresponding character and position in the text file. Note that it does not draw the tiles, it only loads the data
     * from the string onto an array.
     * </p>
     * @param levelLayout is a string which is derived from a level file loaded inside levelData() which is chosen from the variable 'level'.
     */

    private void initTiles(String[] levelLayout) {
        tiles = new ArrayList<>();
        holes.clear();
    
        for (int row = 0; row < levelLayout.length; row++) {
            List<Tile> tileRow = new ArrayList<>();
            String line = levelLayout[row];
            for (int col = 0; col < line.length(); col++) {
                numafter = col + 1;
                numbefore = col - 1;
                char symbol = line.charAt(col);
                if (numafter < line.length()) {
                    symbolnext = line.charAt(numafter);
                }
                if (numbefore > 0) {
                    symbolbefore = line.charAt(numbefore);
                }
                Tile tile;
    
                switch (symbol) {
                    case 'X':
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "grey", this);
                        tileRow.add(tile);
                        break;
                    case '0':
                        if (symbolbefore != 'B' && symbolbefore != 'H') {
                            tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "grey", this);
                            tileRow.add(tile);
                            break;
                        }
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                    case '1':
                        if (symbolbefore != 'B' && symbolbefore != 'H') {
                            tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "orange", this);
                            tileRow.add(tile);
                            break;
                        }
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                    case '2':
                        if (symbolbefore != 'B' && symbolbefore != 'H') {
                            tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "blue", this);
                            tileRow.add(tile);
                            break;
                        }
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                    case '3':
                        if (symbolbefore != 'B' && symbolbefore != 'H') {
                            tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "green", this);
                            tileRow.add(tile);
                            break;
                        }
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                        break;
                    case '4':
                        if (symbolbefore != 'B' && symbolbefore != 'H') {
                            tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "yellow", this);
                            tileRow.add(tile);
                            break;
                        }
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                        break;
                    case 'S':
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "entry", this);
                        tileRow.add(tile);
                        spawnerTiles.add(tile);  // Store the entry point tile
                        break;
                    case 'B':
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                        if (symbolnext == '0') {
                            spawnBall("grey", false, CELLSIZE * col, TOPBAR + CELLSIZE * row);
                        } else if (symbolnext == '1') {
                            spawnBall("orange", false, CELLSIZE * col, TOPBAR + CELLSIZE * row);
                        } else if (symbolnext == '2') {
                            spawnBall("blue", false, CELLSIZE * col, TOPBAR + CELLSIZE * row);
                        } else if (symbolnext == '3') {
                            spawnBall("green", false, CELLSIZE * col, TOPBAR + CELLSIZE * row);
                        } else if (symbolnext == '4') {
                            spawnBall("yellow", false, CELLSIZE * col, TOPBAR + CELLSIZE * row);
                        }
                        break;
                    case 'T':
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "timer", this);
                        tileRow.add(tile);
                        timerTiles.add(tile);
                        break;
                    case 'H':
                        if (symbolnext == '0') {
                            Hole hole = new Hole(CELLSIZE * col, TOPBAR + CELLSIZE * row, "grey", this);
                            spawnHole(hole);
                        } else if (symbolnext == '1') {
                            Hole hole = new Hole(CELLSIZE * col, TOPBAR + CELLSIZE * row, "orange", this);
                            spawnHole(hole);
                        } else if (symbolnext == '2') {
                            Hole hole = new Hole(CELLSIZE * col, TOPBAR + CELLSIZE * row, "blue", this);
                            spawnHole(hole);
                        } else if (symbolnext == '3') {
                            Hole hole = new Hole(CELLSIZE * col, TOPBAR + CELLSIZE * row, "green", this);
                            spawnHole(hole);
                        } else if (symbolnext == '4') {
                            Hole hole = new Hole(CELLSIZE * col, TOPBAR + CELLSIZE * row, "yellow", this);
                            spawnHole(hole);
                        }
                        break;
                    case ' ':
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                        break;
                    default:
                        tile = new Tile(CELLSIZE * col, TOPBAR + CELLSIZE * row, "empty", this);
                        tileRow.add(tile);
                        break;
                }

                
            }
            tiles.add(tileRow);
        }
    }
    
    /**
     * In this program, the draw function repeatedly runs until the program is terminated.
     * 
     * <p>
     * In the draw function, all the tiles are drawn from the array set from the initTiles method. The holes are drawn after, and then the logic
     * for the clock cycles for the timer tiles added (extension) is then implemented.
     * </p>
     * <p>
     * After this, the program checks if a ball is inside a hole and does the necessary score calculates and penalties (if appropriate) and checks if
     * the level is complete. Afterwards, all lines from the lines Arraylist are drawn and the timer is decremented as long as the timer is above 0.
     * </p>
     * <p>
     * The top bar sprites are then loaded, including various grey rectangles filling the top bar and the ball track with the ball spirtes.
     * This includes the text such as when it is paused or simply the timer or score display.
     * </p>
     * <p>
     * Finally, it goes through what the program needs to do when the player 'wins' including the tile animation, the score transferring
     * over to the timer, levelData being ran again with the new level variable (so that the data for the inividual level would be correct to the
     * corresponding level), etc.
     * </p>
     */

    @Override
    public void draw() {
        int currentTime = millis();
        background(216,212,212);
        // Draw regular tiles
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.draw(this);
            }
        }

        for (Hole hole : holes) { // draw holes
            hole.draw(this);
        }

        long storedTime = millis(); // storedTime = current time

        if (!paused && !levelwin && !timesup) { // status checks
            if (storedTime - lastUpdated >= 100) { 
                if (ballTrackOffsetTracker > 0) {
                    ballTrackOffset--;
                    ballTrackOffsetTracker--;
                }
                if (ballTicking > 0.1) {
                    ballTicking -= 0.1;
                } else {
                    if (millis() > 1000 && ballArray.size() > trackerThingy) {
                        // System.out.println("ballArray size: " + ballArray.size());
                        // System.out.println("trackerThingy: " + trackerThingy);
                        spawnBall(ballArray.get(trackerThingy), true, 0, 0);
                        trackerThingy++;
                        ballTrackOffsetTracker = 32;
                    }
                    ballTicking = ballInterval;
                }
                lastUpdated = storedTime;
            }
            if (currentTime - timerblockStartTimer >= 5000 && !timerblockInSequence) { // make this 5 seconds!
                timerblockInSequence = true;
                timerblockSequenceTimer = currentTime;
                timerblockRunCount = 0;
            }
        
            if (timerblockInSequence) { // within the 5 seconds!
                if (millis() - timerblockSequenceTimer >= 200) {  // 0.2-second delay
                        if (transitioning) { // transitioning determines whether the block is turning invisible or not. default it is true as timerstep default = 5
                            for (Tile tile : timerTiles) {
                                tile.becomeInvisible(); 
                            }
                        } else {
                            for (Tile tile : timerTiles) {
                                tile.becomeVisible();
                            }
                        }
                    timerblockRunCount++;
                    timerblockSequenceTimer = currentTime;
        
                    if (timerblockRunCount >= 5) {
                        transitioning = !transitioning;
                        timerblockInSequence = false;
                        timerblockStartTimer = currentTime;
                    }
                }
            }
        }

        for (int i = balls.size() - 1; i >= 0; i--) {
            Ball ball = balls.get(i);
            ball.update();
            ball.draw();
            for (Object tile : holes) {
                Hole hole = (Hole) tile; // cast it to Hole type
                if (hole.isBallInHole(ball)) {
                    if (ball.getColour().equals("grey") || hole.getColour().equals(ball.getColour())) { // yay it matches
                        if (hole.getColour().equals("grey")) {
                            score += greyscore * scoreincrease;
                        } else if (hole.getColour().equals("orange")) {
                            score += orangescore * scoreincrease;
                        } else if (hole.getColour().equals("blue")) {
                            score += bluescore * scoreincrease;
                        } else if (hole.getColour().equals("green")) {
                            score += greenscore * scoreincrease;
                        } else if (hole.getColour().equals("yellow")) {
                            score += yellowscore * scoreincrease;
                        }
                    } else {
                        if (hole.getColour().equals("grey")) {
                            score += greyscore * scoreincrease;
                        } else { // it does not match sad
                            if (hole.getColour().equals("orange")) {
                            score -= orangescore * scoredecrease;
                            } else if (hole.getColour().equals("blue")) {
                            score -= bluescore * scoredecrease;
                            } else if (hole.getColour().equals("green")) {
                                score -= greenscore * scoredecrease;
                            } else if (hole.getColour().equals("yellow")) {
                                score -= yellowscore * scoredecrease;
                        }
                        ballArray.add(ball.getColour());
                        ballsleft.add(ball.getColour());
                    }
                    }
                    balls.remove(i);
                    // System.out.println(balls.size());
                    if (balls.size() == 0 && ballArray.size() <= trackerThingy) {
                        if (level < 3) {
                            levelwin = true;
                            scoretracker = score;
                        } else {
                            gameEnded = true;
                        }
                        levelWinTime = millis();
                        level += 1;
                    }
                    break;
                }
            }   
        }

        // Draw lines
        stroke(0);
        strokeWeight(10);
        for (List<PVector> line : lines) {
            for (int i = 0; i < line.size() - 1; i++) {
                PVector p1 = line.get(i);
                PVector p2 = line.get(i + 1);
                line(p1.x, p1.y, p2.x, p2.y);
            }
        }

        if (!paused) {
            if (timer > 0) {
                timer -= 1 / 30.0;
            } else {
                timer = 0;
                timesup = true;
            }
        }
        
        noStroke();
        fill(0); // fill black
        rect(10, 10, 165, 35);
        fill(216,212,212);

        for (int i = 0; i < ballsleft.size(); i++) {
            image(printBalls(ballsleft.get(i)), 15 + App.CELLSIZE * i + ballTrackOffset, 15);
        }

        rect(0, 0, 10, App.CELLSIZE * 2);
        rect(10, 0, App.WIDTH-10, 10);
        rect(175, 10, App.WIDTH-175, App.CELLSIZE * 2 - 10);
        rect(10, 45, 165, 19);
        fill(0);
        textSize(20); // Set text size
        text("Score: " + score, width-150, 25); // draw score text
        text("Time: " + (int) timer, width - 150, 55); // draw timer text
        text(String.format("%.1f", ballTicking), 200, 45); // draw timer text

        if (paused) {
            textAlign(CENTER);
            lastText = "*** PAUSED ***";
            // System.out.println(lastText);
            text(lastText, width / 2, 30); // draw paused text
            textAlign(LEFT);
        }

        if (timesup && !levelwin && !paused) {
            textAlign(CENTER);
            lastText = "=== TIME'S UP ===";
            // System.out.println(lastText);
            text(lastText, width / 2, 30); // draw paused text
            textAlign(LEFT);
        }

        if (gameEnded && !paused) {
            textAlign(CENTER);
            lastText = "=== ENDED ===";
            // System.out.println(lastText);
            text(lastText, width / 2, 30); // draw paused text
            textAlign(LEFT);
        }

        if (levelwin && !paused) {
            trackerThingy = 0;
            ballTrackOffsetTracker = 32;
            lastText = "You WON!";
            // System.out.println(lastText);
            text(lastText, width/2, 25); // Draw score text

            tileAnimation(); // tile animation
            if (timer > 0) {
                timer--;
                score++;
            }

            if (millis() - levelWinTime >= 3000 || test) {
                if (timer > 0) {
                    score += timer;
                }
                levelwin = false;
                resetTiles();
                balls.clear();
                lines.clear();
                levelData();
            }
        }
    }

    /**
     * <p>
     * This method simply adds a hole to the array. The reason for using a method instead of using holes.add(hole) is in case further
     * implementation is needed as well as well as reducing repeated code.
     * </p>
     * @param hole is the hole parsed to be added to the holes array (holes).
     */

    public void spawnHole(Hole hole) {
        holes.add(hole);
    }

    
    /**
     * <p>
     * spawnBall is a method which spawns the actual ball, which is necessary due to the fact that a ball can be spawned either from the level
     * config file or it can be spawned from a spawner.
     * </p>
     * <p>
     * So, an if statement determines if it is from a spawner, and if it is, it determines a random spawner from the level and chooses that spawner
     * as the spawn location of that ball. If not, then it uses the x and y from the parameters given.
     * </p>
     * @param colour is the colour of the ball to be spawned. This is parsed into the Ball class which changes it's sprite baed on the colour of the ball.
     * @param fromSpawner is a boolean which determines if the ball is from a spawner or not. If it is not from a config file, then it is from a spawner.
     * @param x is the x value of a ball that will be utilised as the x value of the spawn location if it the ball is not from a spawner.
     * @param y is identical to the y value in that it is also an integer however it is for the y value of the spawn location of the ball
     */

    public void spawnBall(String colour, Boolean fromSpawner, int x, int y) {
        if (fromSpawner) {
            int randomNum = (int) (Math.random() * spawnerTiles.size());
            startPos = new PVector(spawnerTiles.get(randomNum).getX() + App.CELLSIZE/2, spawnerTiles.get(randomNum).getY() + App.CELLSIZE/2);
        } else {
            startPos = new PVector(x + App.CELLSIZE/2, y + App.CELLSIZE/2 + 32/2);
        }

        float velocityX = random.nextBoolean() ? 2 : -2;
        float velocityY = random.nextBoolean() ? 2 : -2;
        PVector velocity = new PVector(velocityX, velocityY);

        Ball ball = new Ball(startPos, velocity, colour, this);
        balls.add(ball);
    }

    /**
     * <p>
     * mousePressed has two if statements.
     * </p>
     * <p>
     * The first if conditional states that if the left mouse button is clicked and time isn't up, then the isDrawing boolean is set to true
     * and the current line segment that is being drawn is added to an arraylist of current lines being drawn, then adds the finished line to
     * the array of all lines.
     * </p>
     * <p>
     * The second if conditional determines that if the user right clicks or ctrl + left clicks, then a line in close proximity to line is removed.
     * More retails in the removeLine() javadoc notes
     * </p>
     * @param e is any mouse event that occurs.
     */

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == LEFT && !timesup) {
            // Start drawing a new line on left-click
            isDrawing = true;
            currentLineBeingDrawn = new ArrayList<>();
            currentLineBeingDrawn.add(new PVector(mouseX, mouseY));
            lines.add(currentLineBeingDrawn);
        }
        if (e.getButton() == RIGHT || (e.getButton() == LEFT && e.isControlDown())) {
            removeLine();
        }
    }
    
    /**
     * <p>
     * This function calculates the length of a line segment from p1 to p2 and finds
     * a vector that points from p1 to p2.
     * </p>
     * <p>
     * t calculates the projection of point (the first argument) onto the line defined by p1 and p2. It utilises the dot product
     * to project the vector from p1 to point onto the line segment vector.
     * </p>
     * <p>
     * t is constrained as if t is 0, the projection is at p1 and if t is 1, the projection is at p2 ensuring the function is
     * within the segment.
     * </p>
     * <p>
     * The coordinates of the projection point on the segment is then determined by multiplying the segment vector by t and
     * adding it p1 which would be the closest point on the segment to point. This distance is then returned if it is less than
     * 5 pixel.
     * </p>
     * @param point is the original point being checked.
     * @param p1 is an endpoint of a vector. 
     * @param p2 is the other endpoint of the vector.
     * @return is a boolean which determines whether, after projecting a point onto a vector determined by p1 and p2, determines whether
     * the distance between the original point and the projection of the point is less than 5 pixels.
     */

    private boolean isPointNearLineSegment(PVector point, PVector p1, PVector p2) {
        float segmentLength = PVector.dist(p1, p2);
    
        PVector segment = PVector.sub(p2, p1);
        float t = PVector.dot(PVector.sub(point, p1), segment) / segmentLength;
        t = PApplet.constrain(t, 0, 1);
    
        PVector projection = PVector.add(p1, segment.mult(t));
        return PVector.dist(point, projection) < 5;
    }
    
     /**
     * <p>
     * This function determines what to do if spacebar is pressed while the level is still going or if r is pressed.
     * </p>
     * <p>
     * If spacebar is pressed while the level is still playing out, it toggles a boolean which determines whether the game is paused or not.
     * If the game is paused, The velocity of balls stop updating and lines are unable to be drawn. All time-based events are stopped until
     * this if statement runs again which toggles the paused state to false in which the game's physics can update again (yay)
     * </p>
     * <p>
     * If r is pressed, it clears all balls and lines and resets the level data to the beginning, meaning all original balls have been refreshed.
     * Additionally, score is reset back to scoretracker. Scoretracker is the same as score except it only updates when you win a level.
     * The reason for this is obvious as we don't want to the player to accrue infinite score in a cheesy way
     * </p>
     */

    @Override
    public void keyPressed() {
        if (key == ' ' && !timesup) {
            paused = !paused; // Toggle pause state
        }
        if (key == 'r') {
            if (gameEnded) {
                gameEnded = false;
                level = 1;
            }
            balls.clear();
            lines.clear();
            score = scoretracker;
            levelData();
        }
    }

    /**
     * <p>
     * This method checks if the left mouse button is being held, and if so it adds the vector from the last mouseX mouseY value to the
     * current mouseX mouseY values, and adds it to the 'currentLineBeingDrawn' line segment as long as 'isDrawing' is true (because we
     * want all segments to be in one 'line' line object)
     * </p>
     * 
     * @param e is any mouse event.
     */

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDrawing && e.getButton() == LEFT) {
            currentLineBeingDrawn.add(new PVector(mouseX, mouseY));
        }
    }

    /**
     * <p>
     * This method checks if the left mouse button is released, and given that if it is released we can assume that the left mouse button
     * had been held earlier meaning isDrawing would be true. We want each time the mouse to be dragged to create a new 'line' object and
     * so we set isDrawing to false to make sure that when the mouse is pressed again a new line object is created.
     * <p>
     * 
     * @param e is any mouse event.
     */

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == LEFT) {
            isDrawing = false;
        }
    }

    /**
     * The main function for the inkball program.
     * <p>
     * This method uses PApplet to run 'inkball.App'.
     * </p>
     * 
     * @param args command line arguments in program which are unused
     */

    public static void main(String[] args) {
        PApplet.main("inkball.App");
    }

    /**
     * <p>
     * This is a getter method which retrives the list in which all tiles are kept.
     * </p>
     */

    public List<List<Tile>> getTiles() {
        return tiles;
    }

    /**
     * <p>
     * This is a getter method which retrives the list in which all player drawn lines are kept.
     * </p>
     */

    public List<List<PVector>> getLinesList() {
        return lines;
    }

    /**
     * <p>
     * This is a method which returns the PImage of a ball which is determined 'ball'.
     * </p>
     * @param ball is a string which corresponds to the colours of the ball.
     */

    public PImage printBalls(String ball) {
        if (ball.equals("grey")) {
            return greyball;
        } else if (ball.equals("orange")) {
            return orangeball;
        } else if (ball.equals("blue")) {
            return blueball;
        } else if (ball.equals("green")) {
            return greenball;
        } else if (ball.equals("yellow")) {
            return yellowball;
        } else return null;
    }

    /**
     * <p>
     * This is a method which returns the list of tiles in an order which lists tiles in a clockwise snaking list starting from the top left tile
     * to the tile under it. It is used for the tile animation as it cycles through each tile and requires it to be in a certain order (which this
     * function puts it in)
     * </p>
     */

    private List<Tile> getOuterTiles() { // took a bit of tweaking but it is the array of the outside tiles for the tile animation 
        List<Tile> outerTiles = new ArrayList<>();
    
        // top row
        for (int x = 0; x < GRID_WIDTH-1; x++) {
            outerTiles.add(tiles.get(0).get(x));
        }

        // right column
        for (int y = 0; y < GRID_HEIGHT - 1; y++) {
            try {
                outerTiles.add(tiles.get(y).get(18));
                continue;
            }
            catch (Exception ArrayIndexOutOFBoundsException) {
            }
            try {
                outerTiles.add(tiles.get(y).get(17));
                continue;
            }
            catch (Exception ArrayIndexOutOFBoundsException) {
            }
        }
        // bottom row
        for (int x = GRID_WIDTH - 1; x > 0; x--) {
            outerTiles.add(tiles.get(GRID_HEIGHT - 1).get(x));
        }
        // left column
        for (int y = GRID_HEIGHT - 1; y > 0; y--) {
            outerTiles.add(tiles.get(y).get(0));
        }
    
        return outerTiles;
    }

    /**
     * <p>
     * This is the same method as getOuterTiles1 except now it starts in the bottom left tile. Note that it is still in a clockwise direction, just starts
     * in the opposite corner.
     * </p>
     */

    private List<Tile> getOuterTiles2() {
        List<Tile> outerTiles = new ArrayList<>();
    
        // bottom row
        for (int x = GRID_WIDTH - 1; x > 0; x--) {
            outerTiles.add(tiles.get(GRID_HEIGHT - 1).get(x));
        }
        // left column
        for (int y = GRID_HEIGHT - 1; y > 0; y--) {
            outerTiles.add(tiles.get(y).get(0));
        }
    
        // top row
        for (int x = 0; x < GRID_WIDTH-1; x++) {
            outerTiles.add(tiles.get(0).get(x));
        }

        // right column
        for (int y = 0; y < GRID_HEIGHT - 1; y++) {
            try {
                outerTiles.add(tiles.get(y).get(18));
                continue;
            }
            catch (Exception ArrayIndexOutOFBoundsException) {
            }
            try {
                outerTiles.add(tiles.get(y).get(17));
                continue;
            }
            catch (Exception ArrayIndexOutOFBoundsException) {
            }
        }
        return outerTiles;
    }

    /**
     * <p>
     * tileAnimation is a bit of a complex function but first it initialises the outerTiles and outerTiles2 lists for the animation and
     * sets the index of which tile to animate as 0. a variable called isAnimating is set to true meaning that this loop only runs once.
     * This is important for not only effiency but also to ensure that currentTileIndex is only set to 0 initially.
     * </p>
     * <p>
     * Then, if the tile index is less than the size of the array (both arrays are same size, only less than because zero-indexing) and
     * then two tiles currentTile and currentTile2 are set to yellow, then currentTileIndex is incremented. The next time the game runs, 
     * the next tiles animate (because currentTileIndex is incremented and it acceses the index of the arrays using currentTileIndex) and
     * the animation continues until the tile index currentTileIndex is equal or greater than the size of the array outerTiles.
     * </p>
     * <p>
     * if the tile index is equal or greater than the size of the array, then isAnimating is set to false so that the next animation
     * can be played again, and the last tile of the arrays are set to their original colour that they started with (tile.OgColour).
     * </p>
     */

    private void tileAnimation() {
        if (!isAnimating) {
            outerTiles = getOuterTiles();
            outerTiles2 = getOuterTiles2();
            currentTileIndex = 0;
            isAnimating = true;
        }

        if (currentTileIndex < outerTiles.size()) {
            Tile currentTile = outerTiles.get(currentTileIndex);
            Tile currentTile2 = outerTiles2.get(currentTileIndex);
            currentTile.setSprite("yellow");
            currentTile2.setSprite("yellow");

            if (currentTileIndex > 0) {
                Tile previousTile = outerTiles.get(currentTileIndex - 1);
                Tile previousTile2 = outerTiles2.get(currentTileIndex - 1);
                previousTile.setSprite(previousTile.getOgColour());
                previousTile2.setSprite(previousTile2.getOgColour());
            }

            currentTileIndex++;
        } else {
            Tile lastTile = outerTiles.get(currentTileIndex - 1);
            Tile lastTile2 = outerTiles2.get(currentTileIndex - 1);
            lastTile.setSprite(lastTile.getOgColour());
            lastTile2.setSprite(lastTile.getOgColour());
            isAnimating = false;
        }
    }

    /**
     * <p>
     * This function resets all tiles to the sprites that they were initialised with.
     * </p>
     */
    private void resetTiles() {
        for (List<Tile> row : tiles) {
            for (Tile tile : row) {
                tile.setSprite(tile.getOgColour());  // Assuming there's a method to get the original sprite
            }
        }
    }

    /**
     * <p>
     * This function loads all the data which is dependent on the individual levels based on a 'level' variable.
     * </p>
     */

    private void levelData() {
        timesup = false;
        JSONArray levels = config.getJSONArray("levels");
        String[] levelLayout = loadStrings(levels.getJSONObject(level-1).getString("layout"));
        timer = levels.getJSONObject(level-1).getInt("time");
        // timer = 3;

        ballInterval = levels.getJSONObject(level-1).getInt("spawn_interval");
        JSONArray ballsArray = levels.getJSONObject(level-1).getJSONArray("balls");
        scoreincrease = levels.getJSONObject(level-1).getDouble("score_increase_from_hole_capture_modifier");  
        scoredecrease = levels.getJSONObject(level-1).getDouble("score_decrease_from_wrong_hole_modifier");
        initTiles(levelLayout);
        for (int i = 0; i < ballsArray.size(); i++) {
            String ball = ballsArray.getString(i);
            ballArray.add(ball);
            ballsleft.add(ball);
        }
    }

    /**
     * <p>
     * This is a getter method to return the list of all balls currently in the program.
     * </p>
     */
    public List<Ball> getBalls() {
        return balls;
    }
    /**
     * <p>
     * This is a getter method to return the list of all holes currently in the program.
     * </p>
     */
    public List<Hole> getHoles() {
        return holes;
    }

    /**
     * <p>
     * This method cycles through all lines and instantialises PVectors for two points being the start and end of a line.
     * Then, the method uses isPointNearLineSegment, a function which determines whether a point is 5 units or less than a line,
     * and uses it to determine whether the mouseX and mouseY point is less than 5 units than each of the lines being
     * cycled. The first line to be checked is removed from the list and the function returns immediately doing so.
     * </p>
     */
    private void removeLine() {
    
        // Iterate through each line in the list
        for (int lineIndex = 0; lineIndex < lines.size(); lineIndex++) {
            List<PVector> line = lines.get(lineIndex);

            // Check each segment of the line
            for (int i = 0; i < line.size() - 1; i++) {
                PVector p1 = line.get(i);  // Starting point of the segment
                PVector p2 = line.get(i + 1);  // Ending point of the segment

                // Check if the mouse is near the line segment
                if (isPointNearLineSegment(new PVector(mouseX, mouseY), p1, p2)) {
                    lines.remove(lineIndex);
                    return;  // Exit after removing one line
                }
            }
        }
    }
}


