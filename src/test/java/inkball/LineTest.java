package inkball;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;

public class LineTest {

    static App app;

    @BeforeAll
    public static void setup() {
        app = new App();
        PApplet.runSketch(new String[]{"App"}, app);
    }

    @Test
    public void testMousePressedDrawLine() {
        // Set mouse position for the left mouse button press
        app.mouseX = 100;
        app.mouseY = 150;
        app.mouseButton = PConstants.LEFT; // Set the left mouse button state

        // Simulate mouse press
        app.mousePressed(); // Call your app's mousePressed method directly

        // Check that a line has been created
        System.out.println("Lines List after draw: " + app.getLinesList());
        assertEquals(1, app.getLinesList().size(), "Should have one line drawn.");
        assertEquals(new PVector(100, 150), app.getLinesList().get(0).get(0), "The point should be at (100, 150).");
    }

    @Test
    public void testMousePressedRemoveLine() {
        // Prepare the initial line
        List<PVector> existingLine = new ArrayList<>();
        existingLine.add(new PVector(50, 50));
        existingLine.add(new PVector(150, 150));
        app.getLinesList().add(existingLine); // Add the line to the list

        // Set mouse position for the right mouse button click
        app.mouseX = 100;
        app.mouseY = 100;
        app.mouseButton = PConstants.RIGHT; // Set the right mouse button state

        // Simulate right mouse button click
        app.mousePressed(); // Call your app's mousePressed method directly

        // Check that the line has been removed
        System.out.println("Lines List after right-click remove: " + app.getLinesList());
        assertTrue(app.getLinesList().isEmpty(), "The line should be removed after right-clicking near it.");
    }

    @Test
    public void testMousePressedRemoveLineWithControl() {
        // Prepare the initial line
        List<PVector> existingLine = new ArrayList<>();
        existingLine.add(new PVector(50, 50));
        existingLine.add(new PVector(150, 150));
        app.getLinesList().add(existingLine); // Add the line to the list

        // Set mouse position for the left mouse button click with Control key down
        app.mouseX = 100;
        app.mouseY = 100;
        app.mouseButton = PConstants.LEFT; // Set the left mouse button state

        // Simulate left mouse button click with Control key down
        // You might want to adjust this depending on how you check for Control key presses in your app
        app.mousePressed(); // Call your app's mousePressed method directly

        // Check that the line has been removed
        System.out.println("Lines List after control-click remove: " + app.getLinesList());
        assertTrue(app.getLinesList().isEmpty(), "The line should be removed after left-clicking with Control key down.");
    }
}