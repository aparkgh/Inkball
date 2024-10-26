package inkball;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class Ball extends PApplet{
    private PVector position;
    private PVector velocity;
    private PImage sprite;
    private PApplet app;
    private float squareLeft = 0; // boundaries where the tiles spawn, so that the ball bounces off the square even if there are empty tiles.
    private float squareRight = 576;
    private float squareTop = 64;
    private float squareBottom = 576+64;
    private float ballSize = 1; // multiplier for ball size
    private int ballRadius = 24/2;
    private String colour;

    /**
     * <p>
     * This method instantialises the Ball object. The only thing worth noting outside of param is that it uses a convertType
     * function which sets it to a certain sprite based on a string colour. Note that this method is also used when a ball
     * bounces on certain coloured walls.
     * </p>
     * @param position is the position of the ball. This position is a PVector so that it's x and y values can be manipulated.
     * @param velocity is the velocity of the ball. This position is a PVector so that it's x and y values can be manipulated.
     * @param colour is the colour of the ball. This detemines things like interactions with certain colour balls entering certain
     * coloured holes, and the ball's sprite.
     * @param app is the PApplet app instance that controls the game and it's logic and graphics
     */

    public Ball(PVector position, PVector velocity, String colour, PApplet app) {
        this.position = position;
        this.velocity = velocity;
        this.app = app;
        this.colour = colour;
        convertType(colour);
    }

    /**
     * <p>
     * The update function firstly adds the velocity to the ball on each frame after all calculations.
     * Then, two if conditionals check if the ball is touching the edge of the boundary of the grid (so
     * that even if no tiles exist, the ball would still bounce on the edges).
     * </p>
     * <p>
     * Then, for all tiles, it checks if there has been a collision with the tile. The way that collisions are detected is
     * with eight variables.
     * Four of these variables are the top, right, bottom and left values of the ball. The other variables are the same but for
     * the tile. Then, it checks if:
     * 1. The right of the ball is more right than the left of the wall and
     * 2. The left of the ball is more left than the right of the wall (meaning that at least some x value exists in the ball which
     * also exists in a wall)
     * 3. The bottom of the ball is lower than the top of the wall and
     * 4. The top of the ball is higher than the bottom of the wall (meaning that at least some y value exists in the ball which
     * also exists in a wall)
     * This means that there must be a x and a y value where the ball intersects the wall, and using this I determine these to be the
     * conditions to determine whether a collision between a ball and a wall occurs.
     * </p>
     * <p>
     * However before performing the necessary velocity changes, I check for adjacent walls. This is because sometimes in one frame
     * the ball is doing fine but then suddenly in the next frame, the ball enters two walls at once some glitchy collisions may occur.
     * To avoid this I do a check - For example if the ball bounces on the top of a wall, I check if there is a tile above that tile
     * and if so I invalidate the collision. The reason why I do this is because if a ball bounces on an edge which is touching another
     * tile, it is obviously because it has clipped inside two tiles at once. So, by checking if a tile with collision exists in one tile
     * towards the direction of the edge, we can nullify the collision and effectively remove these glitchy collisions by only considering
     * the edges that face the outside of the tiles (or if the tile in front does not have any collision). The way I check if there is
     * collision is with a method in Tile.java called hasCollision which does various checks for the tile before returning a boolean value
     * on it's collision status.
     * </p>
     * <p>
     * After all this mumbo jumbo the line collisions are then checked. For each line a 'checkLineCollision' method is run and performs it's
     * necessary actions based on whether there is a collision or not. Finally, the attraction forces are calculated, which is checked by
     * going through the positions of all the holes in the level and if the positions of a hole is less than 32 pixels than that of the position
     * of any ball. Any ball which happens to be in this proximity has a vector added to it's velocity which is equivalent to 0.05% of the vector
     * of the ball towards the centre of the hole. Additinoally it's size is proportionally reduced by a map function- it maps 0 to 32 representing
     * the distance from the ball to the hole from 0 to 1, meaning that ballSize is 1(maximum) at a distance of 32 pixels and greater and ballSize
     * is 0(minimum) at a distance of 0 pixels if the ball is inside the hole.
     * </p>
     * <p>
     * When a collision is detected between a ball and a wall, it stops checking for collisions as it sets collided to true and all rows and columns
     * are only checked if the bottom if conditional which states if collided is true doesn't run which contains a break, breaking out of the for loop.
     * </p>
     */

    public void update() {
        if (!App.paused && !App.timesup) {
            position.add(velocity);

            if (position.x - sprite.width / 2 < squareLeft) {
                position.x = squareLeft + sprite.width / 2;
                velocity.x *= -1;
            } else if (position.x + sprite.width / 2 > squareRight) {
                position.x = squareRight - sprite.width / 2;
                velocity.x *= -1;
            }

            if (position.y - sprite.height / 2 < squareTop) {
                position.y = squareTop + sprite.height / 2;
                velocity.y *= -1;
            } else if (position.y + sprite.height / 2 > squareBottom) {
                position.y = squareBottom - sprite.height / 2;
                velocity.y *= -1;
            }
            
            // Check collision with walls only if allowed to collide again
            // if (canCollideAgain) {
                List<List<Tile>> tiles = ((App) app).getTiles();
                
                boolean collided = false; // Flag to track if a collision occurred
    
                for (List<Tile> row : tiles) {
                    for (Tile tile : row) {
                        if (tile.hasCollision()) {
                            PVector wallPos = new PVector(tile.getX(), tile.getY());
    
                            // Calculate bounds of the ball and the wall
                            float ballLeft = position.x - ballRadius;
                            float ballRight = position.x + ballRadius;
                            float ballTop = position.y - ballRadius;
                            float ballBottom = position.y + ballRadius;
    
                            float wallLeft = wallPos.x;
                            float wallRight = wallPos.x + App.CELLSIZE;
                            float wallTop = wallPos.y;
                            float wallBottom = wallPos.y + App.CELLSIZE;
    
                            // Check for collision
                            if (ballRight >= wallLeft && ballLeft <= wallRight &&
                                ballBottom >= wallTop && ballTop <= wallBottom) {
    
                                // Ensure it's not the same wall as last collision
                                // if (tile != lastCollisionWall) {
                                    // Determine overlap amounts
                                    float overlapLeft = wallRight - ballLeft;
                                    float overlapRight = ballRight - wallLeft;
                                    float overlapTop = wallBottom - ballTop;
                                    float overlapBottom = ballBottom - wallTop;
    
                                    // returns the smallest value of the overlap.
                                    float minOverlapX = Math.min(overlapLeft, overlapRight);
                                    float minOverlapY = Math.min(overlapTop, overlapBottom);
    
                                    // Inside the collision detection loop, after calculating the overlaps:

                                    if (minOverlapX < minOverlapY) {
                                        if (overlapLeft < overlapRight) {
                                            // System.out.println("right");
                                            if (!isAdjacentWall(tile, "right")) {
                                                // System.out.println("hit right side of wall");
                                                if (velocity.x < 0) velocity.x *= -1; // Reverse velocity
                                                // velocity.x *= -1;
                                            } else {
                                                continue;
                                            }
                                        } else {
                                            // System.out.println("left");
                                            // check if there's a wall to the right
                                            if (!isAdjacentWall(tile, "left")) {
                                                // System.out.println("hit left side of wall");
                                                if (velocity.x > 0) velocity.x *= -1; // Reverse velocity
                                                // velocity.x *= -1;
                                            } else {
                                                continue;
                                            }
                                        }
                                    } else {
                                        // Collided on Y-axis (top/bottom)
                                        if (overlapTop < overlapBottom) {

                                            // check if there's a wall above
                                            if (!isAdjacentWall(tile, "bottom")) {
                                            // if (tile.hasCollision()) {
                                                // System.out.println("hit bottom of wall");
                                                // position.y += overlapTop - 1; // Move ball outside of collision
                                                if (velocity.y < 0) velocity.y *= -1; // Reverse velocity
                                                // velocity.y *= -1;
                                            } else {
                                                continue;
                                            }
                                        } else {

                                            // check if there's a wall below
                                            if (!isAdjacentWall(tile, "top")) {
                                            // if (tile.hasCollision()) {
                                                // System.out.println("hit top of wall");
                                                // position.y -= overlapBottom + 1; // Move ball outside of collision
                                                if (velocity.y > 0) velocity.y *= -1; // Reverse velocity
                                                // velocity.y *= -1;
                                            } else {
                                                continue;
                                            }
                                        }
                                    }
                                
                                    if(!tile.getColour().equals(this.getColour()) && !tile.getColour().equals("grey")) {
                                        this.convertType(tile.getColour());
                                    }
                                    collided = true;
                                    break;
                            }
                        }
                    }
                    if (collided) {
                        break;
                    }
                }
            checkLineCollision(((App) app).getLinesList());
    
                for (Hole hole : App.holes) {
                    PVector holePosition = hole.getPosition();
                    float currentDistance = PVector.dist(this.position, holePosition);  // Calculate distance to the hole

                    if (currentDistance <= 32) {
                        ballSize = map(currentDistance, 0, 32, 0, 1);
                        PVector attraction = PVector.sub(holePosition, this.position);  // Attraction vector toward the hole
                        attraction.mult(0.005f);  // Scale down the attraction force
                        velocity.add(attraction);  // Add the attraction to the velocity
                    }
                }
        }
    }

    /**
     * <p>
     * This method draws each sprite but ensures that the sprite is drawn appropriately where the centre of the ball is. Additionally,
     * the ball is drawn by ballSize, changed in update when the position is close to the position of a hole.
     * </p>
     */
    public void draw() {
        // Draw the ball at its current position
        app.image(sprite, position.x - sprite.width / 2, position.y - sprite.height / 2, sprite.width * ballSize, sprite.height * ballSize);
    }

    /**
     * <p>
     * This method checks for line collisions. First, it loops through every line in the list and through every point that
     * makes up each segment of each line. For each segment, the distance between position and p1, and the distance between
     * position and p2 is calculated, and the distance between p1 and p2.
     * 
     * To check if the ball is colliding with the line, it compares the sum of the distances between the ball and the line's
     * endpoints (distanceP1Ball + distanceP2Ball) to the distance between the two points of the line (distanceP1P2).
     * If a collision is detected, the ball is reflected using the reflectBall function (below) and the line that is reflected
     * with is removed, and the function returns to prevent multiple collisions in one frame.
     * </p>
     * @param allLinesList is the list of all currently active lines in the program.
     */
    private void checkLineCollision(List<List<PVector>> allLinesList) {
        for (int lineIndex = 0; lineIndex < allLinesList.size(); lineIndex++) {
            List<PVector> line = allLinesList.get(lineIndex);
            for (int i = 0; i < line.size() - 1; i++) {
                PVector p1 = line.get(i);
                PVector p2 = line.get(i + 1);

                float distanceP1Ball = PVector.dist(position, p1);
                float distanceP2Ball = PVector.dist(position, p2);
                float distanceP1P2 = PVector.dist(p1, p2);

                if (distanceP1Ball + distanceP2Ball < distanceP1P2 + sprite.width / 2) {
                    reflectBall(p1, p2);
                    allLinesList.remove(lineIndex);
                    return;
                }
            }
        }
    }



    /**
     * <p>
     * This method is for when a ball is reflected off a line segment. First, normal vectors are calculated.
     * 
     * 1. The horizontal and vertical differences between the two points are calculated and stored.
     * 2. The normal vectors to the line segment are calculated based on the differences in positions of the
     * two points p1 and p2 by constructing vectors based on inverting one of the two differences.
     * 3. The length of the line segment is calculated based on the quadratic formula c^2 = sqrt(a^2 + b^2)
     * 4. The normal vectors are normalised based on the length
     * 5. The midpoint of the normal vector closer to the ball is calculated by first calculating the midpoint
     * formula, and then applying the midpoint position to whether if the distance of the midpoints of either
     * n1 or n2 is closer 
     * 6. The velocity is reflected based upon the chosen normal vector
     * </p>
     * @param p1 is an endpoint of the line segment.
     * @param p2 is the other endpoint of the line segment.
     */
    public void reflectBall(PVector p1, PVector p2) {
        // Step 2: Calculate normal vectors
        float horizontalDifference = p2.x - p1.x;
        float verticalDifference = p2.y - p1.y;

        PVector normalVector1 = new PVector(-verticalDifference, horizontalDifference);  // First normal vector
        PVector normalVector2 = new PVector(verticalDifference, -horizontalDifference);  // Second normal vector

        // Step 3: Normalize the vectors
        float length = PApplet.sqrt(horizontalDifference * horizontalDifference + verticalDifference * verticalDifference);

        PVector n1 = normalVector1.div(length);
        PVector n2 = normalVector2.div(length);

        // Step 4: Choose the normal vector closer to the ball
        PVector midPoint = PVector.lerp(p1, p2, 0.5f); // Midpoint of the line segment
        PVector n = (PVector.dist(midPoint.add(n1), position) < PVector.dist(midPoint.add(n2), position)) ? n1 : n2;

        // Step 5: Reflect the ball's velocity using the chosen normal vector
        velocity = velocity.sub(n.mult(2 * velocity.dot(n)));
    }

    /**
     * <p>
     * This is a simple getter method for the position of the ball
     * </p>
     * @return returns the position of the ball
     */
    public PVector getPosition() {
        return position;
    }

    /**
     * This function checks if there's a wall adjacent to the current tile in the specified direction. It is to 
     * invalidate falsey collisions such as if a ball touches the edges of two walls on the sides that they are touching,
     * which obviously are false collisions and must be thrown out to avoid glitchy ball behaviour.
     * <p>
     * The current X and Y values are extracted from currentTile. Then a copy of the 2d list tiles is made equal to the tiles
     * list in App.java. Based on the direction parameter, a direction is chosen for the program to then attempt to check if
     * there exists a spot there to avoid a nullpointerexception, then checks if there is a tile, and if it has collision.
     * </p>
     * @param currentTile is the original tile that you wish to find out if there exists a tile with collision in a certain
     * direction based on the parameter direction
     * @param direction is the direction away from the original tile if you want to see if a tile with collision exists
     * @return returns true if there exists a tile in the direction which also happens to have collision, and false if otherwise.
     * It also happens to return true if no wall is found.
     */
    private boolean isAdjacentWall(Tile currentTile, String direction) {
        
        int currentX = currentTile.getX();
        int currentY = currentTile.getY();
        
        List<List<Tile>> tiles = ((App) app).getTiles();
        switch (direction) {
            case "left":
                if (currentX - App.CELLSIZE >= 0) {
                    try {
                        Tile leftTile = tiles.get((currentX - App.CELLSIZE) / App.CELLSIZE).get(currentY / App.CELLSIZE + 2);
                        return leftTile.hasCollision();
                    } catch (Exception IndexOutOfBoundsException) {
                        return false;
                    }
                }
                break;
            case "right":
                if (currentX + App.CELLSIZE < App.GRID_WIDTH * App.CELLSIZE) {
                    try {
                        Tile rightTile = tiles.get((currentX + App.CELLSIZE) / App.CELLSIZE).get(currentY / App.CELLSIZE + 2);
                        return rightTile.hasCollision();
                    } catch (Exception IndexOutOfBoundsException) {
                        return false;
                    }
                }
                break;
            case "top":
                if (currentY - App.CELLSIZE >= 0) {
                    try {
                        Tile topTile = tiles.get(currentX / App.CELLSIZE).get((currentY - App.CELLSIZE) / App.CELLSIZE + 2);
                        return topTile.hasCollision();
                    } catch (Exception IndexOutOfBoundsException) {
                        return false;
                    }
                }
                break;
            case "bottom":
                if (currentY + App.CELLSIZE < App.GRID_HEIGHT * App.CELLSIZE) {
                    try {
                        Tile bottomTile = tiles.get(currentX / App.CELLSIZE).get((currentY + App.CELLSIZE) / App.CELLSIZE + 2);
                        return bottomTile.hasCollision();
                    } catch (Exception IndexOutOfBoundsException) {
                        return false;
                    }
                }
                return false;
        }
        
        return true;
    }

    /**
     * <p>
     * This is a simple getter method for the colour of the ball
     * </p>
     * @return returns the colour of the ball
     */
    public String getColour() {
        return this.colour;
    }

    /**
     * <p>
     * This is a simple getter method for the velocity of the ball
     * </p>
     * @return returns the velocity of the ball
     */
    public PVector getVelocity() {
        return velocity;
    }

    /**
     * <p>
     * This is a method used to set the colour of a ball. Based on the colour parameter, the colour of the ball is
     * set based on the colour of certain sprites which are set to match the condition of the color.equals() condition.
     * </p>
     * @param colour is the colour in which you want to set the ball's colour.
     */
    private void convertType(String colour) {
        if (colour.equals("grey")) {
            this.sprite = app.loadImage("src/main/resources/inkball/ball0.png");
            this.colour = "grey";
        } else if (colour.equals("orange")) {
            this.sprite = app.loadImage("src/main/resources/inkball/ball1.png");
            this.colour = "orange";
        } else if (colour.equals("blue")) {
            this.sprite = app.loadImage("src/main/resources/inkball/ball2.png");
            this.colour = "blue";
        } else if (colour.equals("green")) {
            this.sprite = app.loadImage("src/main/resources/inkball/ball3.png");
            this.colour = "green";
        } else if (colour.equals("yellow")) {
            this.sprite = app.loadImage("src/main/resources/inkball/ball4.png");
            this.colour = "yellow";
        }
    }

    /**
     * <p>
     * This method is used to set the position of a ball, used for testing.
     * </p>
     * @param x is the x value of the ball's position you want to set as.
     * @param y is the y value of the ball's position you want to set as.
     */
    public void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }
}

