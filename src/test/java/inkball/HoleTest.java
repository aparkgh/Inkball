package inkball;

import processing.core.PApplet;
import processing.core.PVector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;

public class HoleTest {

    static Hole hole;
    static App app;

    @BeforeAll
    public static void setup() {
      app = new App();
      PApplet.runSketch(new String[] {"App"}, app);
      app.setup();
    }

    // @Test
    // public void testHolesUpdateAndDraw() {
    //     app.setup();

    //     app.draw();
        
    //     // Verify the ball's state after update
    //     // Assume the Ball class has a method to check its position
    //     assertFalse(app.getBalls().isEmpty(), "Balls should be updated and drawn.");
    // }
    @Test
    public void greyHole() {

        app.draw();

        app.spawnHole(new Hole(App.CELLSIZE * 10, App.CELLSIZE * 10, "grey", app));
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getHoles().isEmpty(), "Balls should be updated and drawn.");
        App.holes.clear();
    }

    @Test
    public void orangeHole() {

        app.draw();

        app.spawnHole(new Hole(App.CELLSIZE * 15, App.CELLSIZE * 15, "orange", app));
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getHoles().isEmpty(), "Balls should be updated and drawn.");
        App.holes.clear();
    }

    @Test
    public void blueHole() {

        app.draw();

        app.spawnHole(new Hole(App.CELLSIZE * 14, App.CELLSIZE * 14, "blue", app));
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getHoles().isEmpty(), "Balls should be updated and drawn.");
        App.holes.clear();
    }

    @Test
    public void greenHole() {

        app.draw();

        app.spawnHole(new Hole(App.CELLSIZE * 16, App.CELLSIZE * 16, "green", app));
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getHoles().isEmpty(), "Balls should be updated and drawn.");
        App.holes.clear();
    }

    @Test
    public void yellowHole() {

        app.draw();

        app.spawnHole(new Hole(App.CELLSIZE * 10, App.CELLSIZE * 10, "yellow", app));
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getHoles().isEmpty(), "Balls should be updated and drawn.");
        App.holes.clear();
    }

    @Test
    public void getX() {
        app.draw();

        // Create a new Hole object with a specific x-coordinate
        int expectedX = 100;
        Hole hole = new Hole(expectedX, 50, "grey", app);

        // Assert that getX() returns the expected x-coordinate
        assertEquals(expectedX, hole.getX(), "getX() should return the correct x-coordinate (100).");
    }

    @Test
    public void getY() {
        app.setup();
        app.draw();

        // Create a new Hole object with a specific x-coordinate
        int expectedY = 100;
        Hole hole = new Hole(50, expectedY, "grey", app);

        // Assert that getX() returns the expected x-coordinate
        assertEquals(expectedY, hole.getY(), "getY() should return the correct x-coordinate (100).");
    }

    @Test
    public void getColour() {
        app.draw();

        // Create a new Hole object with a specific x-coordinate
        String expectedColour = "green";
        Hole hole = new Hole(100, 100, expectedColour, app);

        // Assert that getX() returns the expected x-coordinate
        assertEquals(expectedColour, hole.getColour(), "getY() should return the correct x-coordinate (100).");
    }
}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport