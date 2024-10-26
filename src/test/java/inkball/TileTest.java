package inkball;

import processing.core.PApplet;
import processing.core.PImage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

public class TileTest {

    static App app;

    @BeforeAll
    public static void setup() {
        app = new App();
        app.loop();
        PApplet.runSketch(new String[] {"App"}, app);
    }

    @Test
    public void getOgColour() {
        app.setup();
        app.draw();
        String expectedColour = "green";
        Tile tile = new Tile(50, 50, expectedColour, app);

        assertEquals(expectedColour, tile.getOgColour(), "should return green");
    }

    @Test
    public void getColour() {
        app.setup();
        app.draw();
        String expectedColour = "green";
        Tile tile = new Tile(50, 50, expectedColour, app);

        assertEquals(expectedColour, tile.getColour(), "should return green");
    }

    @Test
    public void becomeVisible1() {
        app.setup();
        app.draw();
        Tile tile = new Tile(50, 50, "timer", app);
        tile.setTimerStep(1);
        tile.becomeVisible();
        PImage result = tile.getSprite();
        PImage expected = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(32 * 7 + 7, 1, 32, 32);
        result.loadPixels();
        expected.loadPixels();

        for (int i = 0; i < expected.pixels.length; i++) {
          assertEquals(expected.pixels[i], result.pixels[i]);
      }
    }

    @Test
    public void becomeVisible2() {
        app.setup();
        app.draw();
        Tile tile = new Tile(50, 50, "timer", app);
        tile.setTimerStep(2);
        tile.becomeVisible();
        PImage result = tile.getSprite();
        PImage expected = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(32 * 8 + 8, 1, 32, 32);
        result.loadPixels();
        expected.loadPixels();

        for (int i = 0; i < expected.pixels.length; i++) {
          assertEquals(expected.pixels[i], result.pixels[i]);
      }
    }

    @Test
    public void becomeInvisible4() {
        app.setup();
        app.draw();
        Tile tile = new Tile(50, 50, "timer", app);
        tile.setTimerStep(4);
        tile.becomeInvisible();
        PImage result = tile.getSprite();
        PImage expected = app.loadImage("src/main/resources/inkball/inkball_spritesheet.png").get(32 * 8 + 8, 1, 32, 32);
        result.loadPixels();
        expected.loadPixels();

        for (int i = 0; i < expected.pixels.length; i++) {
          assertEquals(expected.pixels[i], result.pixels[i]);
      }
    }

        @Test
      public void printBallsYellow() {
        app.setup(); // Ensure setup is called to initialize resources
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("yellow");
    
        PImage expected = app.yellowball; // Assuming greyball is accessible here
    
        expected.loadPixels();
        result.loadPixels();
        for (int i = 0; i < expected.pixels.length; i++) {
            assertEquals(expected.pixels[i], result.pixels[i]);
        }
    }


    // @Test
    // public void timer1() {
    

    //     // Call the method to get the grey ball image
    //     PImage result = app.printBalls("grey");
    
    //     PImage expected = app.greyball; // Assuming greyball is accessible here
    
    //     expected.loadPixels();
    //     result.loadPixels();
    //     for (int i = 0; i < expected.pixels.length; i++) {
    //         assertEquals(expected.pixels[i], result.pixels[i], "Pixels should match at index " + i);
    //     }
    // }
    // @Test
    // public void testTileTransitioning() {
    //     app.setup();
    //     app.timerblockInSequence = true; // Start transitioning
    //     app.timerblockSequenceTimer = millis(); // Reset timer
        
    //     // Call draw to process transitions
    //     app.draw();
        
    //     // Check that tiles become invisible or visible based on transition state
    //     for (Tile tile : app.timerTiles) {
    //         assertTrue(tile.isVisible() || tile.isInvisible(), "Tiles should transition correctly.");
    //     }
    // }
    
}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
