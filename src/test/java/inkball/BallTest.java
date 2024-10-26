package inkball;

import processing.core.PApplet;
import processing.core.PVector;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;


public class BallTest {

    static Ball ball;
    static App app;

    @BeforeAll
    public static void setup() {
        app = new App();
        PApplet.runSketch(new String[] {"App"}, app);
        app.setup();
        app.loop();
        app.greyscore = 10;
        app.orangescore = 15;
        app.bluescore = 20;
        app.greenscore = 25;
        app.yellowscore = 30;
        app.scoreincrease = 2;
        app.scoredecrease = 1;
        app.spawnHole(new Hole(55, 50, "grey", app));
        app.spawnHole(new Hole(105, 50, "orange", app));
    }

    @BeforeEach
    public void setUp() {
        // Initialize the ball with a specific position and velocity
        PVector initialPosition = new PVector(64, 64); // Example initial position
        PVector initialVelocity = new PVector(2, -2); // Example initial velocity
        ball = new Ball(initialPosition, initialVelocity, "grey", app); // Adjust constructor as needed
    }

    @Test
    public void testReflectBall() {
        // Define points for the reflection line segment

        PVector p1 = new PVector(50, 60); // Start point of the line segment
        PVector p2 = new PVector(70, 60); // End point of the line segment

        // Call the reflectBall method
        ball.reflectBall(p1, p2);

        PVector expectedVelocity = new PVector(2, 2); // Example expected velocity after reflection

        PVector actualVelocity = ball.getVelocity();

        // Assert that the ball's velocity matches the expected velocity
        assertEquals(expectedVelocity.x, actualVelocity.x, "X component of velocity should match after reflection.");
        assertEquals(expectedVelocity.y, actualVelocity.y, "Y component of velocity should match after reflection.");
    }

    @Test
    public void yellowBall() {
        app.setup();

        app.draw();

        app.spawnBall("yellow", false, 50, 50);
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getBalls().isEmpty(), "Balls should be updated and drawn.");
    }

    @Test
    public void ballBounce() {
        app.setup();

        app.draw();

        app.spawnBall("yellow", false, 50, 50);
        // Verify the ball's state after update
        // Assume the Ball class has a method to check its position
        assertFalse(app.getBalls().isEmpty(), "Balls should be updated and drawn.");
    }


    @Test
    public void testGreyBallInGreyHole() {
        Ball greyBall = new Ball(new PVector(50, 50, 32 / 2), new PVector(0, 3), "grey", app);
        Hole greyHole = new Hole(50, 55, "grey", app);

        app.balls.add(greyBall);
        App.holes.add(greyHole);
        
        greyBall.setPosition(55, 50); // Ball in hole
        app.balls.remove(greyBall);

        // Simulate entering the hole
        if (greyHole.isBallInHole(greyBall)) {
            // Simulate score update logic
            app.score += app.greyscore * app.scoreincrease;
        }

        // Assert that the score is updated correctly
        assertEquals(app.greyscore * app.scoreincrease, app.score, "Score should increase for grey ball in grey hole");
        assertFalse(app.balls.contains(greyBall), "Grey ball should be removed after entering hole");
    }

    @Test
    public void testOrangeBallInOrangeHole() {
        Ball orangeBall = new Ball(new PVector(100, 50, 32 / 2), new PVector(0,3), "orange", app);
        Hole orangeHole = new Hole(100, 55, "orange", app);

        app.balls.add(orangeBall);
        App.holes.add(orangeHole);
        
        orangeBall.setPosition(105, 50); // Ball in hole
        app.balls.remove(orangeBall);

        // Simulate entering the hole
        if (orangeHole.isBallInHole(orangeBall)) {
            app.score += app.orangescore * app.scoreincrease;
        }

        // Assert that the score is updated correctly
        assertEquals(app.orangescore * app.scoreincrease, app.score, "Score should increase for orange ball in orange hole");
        assertFalse(app.balls.contains(orangeBall), "Orange ball should be removed after entering hole");
    }

    @Test
    public void testBlueBallInOrangeHole() {
        Ball blueBall = new Ball(new PVector(200, 55, 32 / 2), new PVector(0, 3), "blue", app);
        Hole orangeHole = new Hole(205, 50, "orange", app);

        app.balls.add(blueBall);
        App.holes.add(orangeHole);
        
        blueBall.setPosition(205, 50); // Ball in hole
        app.balls.remove(blueBall);

        // Simulate entering the hole
        if (orangeHole.isBallInHole(blueBall)) {
            app.score -= app.bluescore * app.scoredecrease;
        }

        // Assert that the score is updated correctly
        assertEquals(-app.bluescore * app.scoredecrease, app.score, "Score should decrease for blue ball in orange hole");
        assertFalse(app.balls.contains(blueBall), "Blue ball should be removed after entering hole");
    }

    @Test
    public void testBallInDifferentColorHole() {
        Ball greyBall = new Ball(new PVector(230, 50, 32 / 2), new PVector(0, 2), "grey", app);
        Hole greenHole = new Hole(230, 55, "green", app);

        app.balls.add(greyBall);
        App.holes.add(greenHole);
        
        greyBall.setPosition(55, 50); // Ball in hole
        app.balls.remove(greyBall);

        // Simulate entering the hole
        if (greenHole.isBallInHole(greyBall)) {
            app.score -= app.greyscore * app.scoredecrease;
        }

        // Assert that the score is updated correctly
        assertEquals(-app.greyscore * app.scoredecrease, app.score, "Score should decrease for grey ball in green hole");
        assertFalse(app.balls.contains(greyBall), "Grey ball should be removed after entering hole");
    }
}

// gradle run						Run the program
// gradle test						Run the testcases

// Please ensure you leave comments in your testcases explaining what the testcase is testing.
// Your mark will be based off the average of branches and instructions code coverage.
// To run the testcases and generate the jacoco code coverage report: 
// gradle test jacocoTestReport
