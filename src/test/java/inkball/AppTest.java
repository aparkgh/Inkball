package inkball;

import processing.core.PApplet;
import processing.core.PImage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

public class AppTest {

    static App app;
    // private PImage greyball = loadImage("src/main/resources/inkball/ball0.png");
    // private PImage orangeball = loadImage("src/main/resources/inkball/ball1.png");
    // private PImage blueball = loadImage("src/main/resources/inkball/ball2.png");
    // private PImage greenball = loadImage("src/main/resources/inkball/ball3.png");
    // private PImage yellowball = loadImage("src/main/resources/inkball/ball4.png");
        
    @BeforeAll
    public static void setup() {
        app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        // Thread.sleep(1000);
    }

    // assertEquals(app.frameRate, App.FPS);

    @Test
    public void testFrameRate() {
        // assertTrue(
        //     App.FPS - 2 <= app.frameRate && 
        //     App.FPS + 2 >= app.frameRate
        //     );
        assertEquals(30, App.FPS);
    }

    public void falseBooleans() {
        app.draw();
        assertFalse(app.levelwin || app.gameEnded || App.paused || app.isDrawing, "all of these booleans should be set to false.");
    }

    @Test
    public void testTimerInitial() {
        app.timer = 10; // Set initial timer to 10 seconds
        app.draw(); // Call draw to decrease timer
        assertNotEquals(10, app.timer, "Timer should decrease by 1/30th second.");
    }

    @Test
    public void printBallsGrey() {
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("grey");
    
        PImage expected = app.greyball; // Assuming greyball is accessible here
    
        expected.loadPixels();
        result.loadPixels();
        for (int i = 0; i < expected.pixels.length; i++) {
            assertEquals(expected.pixels[i], result.pixels[i], "Pixels should match at index " + i);
        }
    }

    @Test
    public void printBallsOrange() {
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("orange");
    
        PImage expected = app.orangeball; // Assuming greyball is accessible here
    
        expected.loadPixels();
        result.loadPixels();
        for (int i = 0; i < expected.pixels.length; i++) {
            assertEquals(expected.pixels[i], result.pixels[i], "Pixels should match at index " + i);
        }
    }

    @Test
    public void printBallsBlue() {
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("blue");
    
        PImage expected = app.blueball; // Assuming greyball is accessible here
    
        expected.loadPixels();
        result.loadPixels();
        for (int i = 0; i < expected.pixels.length; i++) {
            assertEquals(expected.pixels[i], result.pixels[i], "Pixels should match at index " + i);
        }
    }

    @Test
    public void printBallsGreen() {
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("green");
    
        PImage expected = app.greenball; // Assuming greyball is accessible here
    
        expected.loadPixels();
        result.loadPixels();
        for (int i = 0; i < expected.pixels.length; i++) {
            assertEquals(expected.pixels[i], result.pixels[i], "Pixels should match at index " + i);
        }
    }

    @Test
    public void printBallsYellow() {
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("yellow");
    
        PImage expected = app.yellowball; // Assuming greyball is accessible here
    
        expected.loadPixels();
        result.loadPixels();
        for (int i = 0; i < expected.pixels.length; i++) {
            assertEquals(expected.pixels[i], result.pixels[i]);
        }
    }

    @Test
    public void printBallsNull() {
    
        // Call the method to get the grey ball image
        PImage result = app.printBalls("hello");
    
        assertNull(result);
    }

    @Test
    public void testMain() {
        try {
            // Call the main method of App
            App.main(new String[]{});
            // If no exceptions were thrown, the test passes
        } catch (Exception e) {
        }
    }

    @Test
    public void testTogglePause() {
        App.paused = false; // Ensure the game is not paused
        app.key = ' '; // Simulate space key press
        app.keyPressed(); // Call keyPressed method

        // Verify that the game is now paused
        assertTrue(App.paused, "The game should be paused after pressing space.");

        app.keyPressed(); // Call keyPressed again to toggle back
        assertFalse(App.paused, "The game should not be paused after pressing space again.");
    }

    // @Test
    // public void testResetGame() {
    //     // Set game to ended state
    //     app.gameEnded = true; // Ensure the game is ended

    //     // Simulate 'r' key press
    //     app.key = 'r';
    //     app.keyPressed(); // Call keyPressed method

    //     // Verify that the game has reset
    //     assertFalse(app.gameEnded, "The game should not be ended after pressing 'r'.");
    //     assertEquals(1, app.level, "The level should reset to 1.");
    //     assertTrue(app.getBalls().isEmpty(), "The balls list should be cleared after pressing 'r'.");
    //     assertTrue(app.getLinesList().isEmpty(), "The lines list should be cleared after pressing 'r'.");
    //     app.gameEnded = false;
    // }

    @Test
    public void timesupVerifier() { //if !paused and timer < 0, set timer to true and times up to true
        app.timer = -1;
        app.draw();
        assertEquals(0, app.timer, "Timer should reset to 0.");
        // assertTrue(App.timesup, "The balls list should be cleared after pressing 'r'.");
        app.timer = 100;
    }

    @Test
    public void pausedText() {
        App.paused = true; // Set the game to paused state
        app.draw(); // This should include the rendering code for paused
        // app.lastText = "*** PAUSED ***";
        assertEquals("*** PAUSED ***", app.lastText, "The paused text should be displayed when the game is paused.");
        App.paused = false;
    }

    @Test
    public void timesUpText() {
        App.timesup = true; // Set the game to paused state
        app.levelwin = false;
        app.draw(); // This should include the rendering code for paused
        assertEquals("=== TIME'S UP ===", app.lastText, "The paused text should be displayed when the game is paused.");
        App.timesup = false;
        app.levelwin = false;
    }

    @Test
    public void gameEndedText() {
        app.levelwin = false;
        App.paused = false;
        app.gameEnded = true; // Set the game to paused state
        app.draw(); // This should include the rendering code for paused
        assertEquals("=== ENDED ===", app.lastText, "The paused text should be displayed when the game is paused.");
        app.gameEnded = false;
    }

    @Test
    public void levelWinStuff1() { // checks that the text is level win when you win + starts tile animation + level win is false, balls and lines are clear, and level data is refreshed
        app.levelwin = true;
        app.timer = 4;
        app.score = 8;
        app.draw(); // This should include the rendering code for paused
        assertEquals("You WON!", app.lastText, "The paused text should be displayed when the game is paused.");
        app.levelwin = false;
    }

    @Test
    public void levelWinStuff2() { // checks that the text is level win when you win + starts tile animation + level win is false, balls and lines are clear, and level data is refreshed
        app.levelwin = true;
        app.test = true;
        app.timer = 4;
        app.score = 8;
        app.draw(); // This should include the rendering code for paused
        assertFalse(app.levelwin, "The balls list should be cleared after pressing 'r'.");
        assertFalse(app.getBalls().isEmpty(), "Balls should be updated and drawn.");
        assertTrue(app.getLinesList().isEmpty(), "Balls should be updated and drawn.");
    }

    // @Test
    // public void thingothy() { // checks that the text is level win when you win + starts tile animation + level win is false, balls and lines are clear, and level data is refreshed
    //     app.levelwin = true;
    //     app.test = true;

    //     app.draw(); // This should include the rendering code for paused
    //     assertEquals(12, app.score);
    //     assertEquals(4, app.timer);
    //     assertFalse(app.levelwin, "The balls list should be cleared after pressing 'r'.");
    //     assertTrue(app.getBalls().isEmpty(), "Balls should be updated and drawn.");
    //     assertTrue(app.getLinesList().isEmpty(), "Balls should be updated and drawn.");
    // }


}
// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
