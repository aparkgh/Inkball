package inkball;

import java.lang.Math;

public class LineSegment {
    public double x1, y1, x2, y2;

    /**
     * <p>
     * Creates a line segment. The first two points are x1, y1, and the last two points are x2, y2
     * </p>
     * @param x1 x position of the first point
     * @param y1 y position of the first point
     * @param x2 x position of the sceond point
     * @param y2 y position of the second point
     */
    public LineSegment(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * <p>
     * Calculates the distance to the point using the point-line distance formula.
     * First it calculates the diffence between the two points, horizontally and vertically.
     * Then it takes the square of the length of the line segment (squared magnitude).
     * </p>
     * <p>
     * u acts as the projection factor- how far along the line segment the closest point to (x, y) is.
     * The numerator takes the dot product giving the length of the projection and divides by the norm
     * to give it a unit scale.
     * </p>
     * <p>
     * We then make sure that u is between 0 and 1 as we know that the closest point has to be between
     * 0 and 1 (on the segment).
     * </p>
     * <p>
     * The x and y differences between the point (x,y) and the closest point is on the line segment
     * is calculated and returned (pythagoras theorem)
     * </p>
     * @param x is the x value of the point
     * @param y is the y value of the point
     * @return is the distance between the point (x,y) and the closest point on the line segment.
     */
    public double distanceToPoint(double x, double y) {
        double horizontalDifference = this.x2 - this.x1;
        double verticalDifference = this.y2 - this.y1;
        double squaredthing = horizontalDifference * horizontalDifference + verticalDifference * verticalDifference;
        double u = ((x - this.x1) * horizontalDifference + (y - this.y1) * verticalDifference) / squaredthing;

        u = Math.max(Math.min(u, 1), 0);

        double horizontalDifference2 = this.x1 + u * horizontalDifference - x;
        double verticalDifference2 = this.y1 + u * verticalDifference - y;
        
        return Math.sqrt(horizontalDifference2 * horizontalDifference2 + verticalDifference2 * verticalDifference2);
    }

    /**
     * <p>
     * This method calculates the normal vector of a line segment.
     * The horizontal and vertical components from the line segments are calculated, and the
     * distance between these two points are calculated with pythagoras' theorem.
     * </p>
     * <p>
     * Then two normal vectors are calculated by inverting one of the differences. Note that this is
     * divided by length to normalise the vectors. Then, the midpoints of these vectors are calculated.
     * </p>
     * <p>
     * The distances between the point and the ends of the two normal vector points are calculated and
     * the function returns whichever one happens to be closer.
     * </p>
     * @param x is the x value of the point
     * @param y is the y value of the point
     * @return is the closest normal - whichever two normal points are closer to (x,y).
     */
    public double[] getNormal(double x, double y) {
        // Calculate the normal vector for the line segment
        double horizontalDifference = this.x2 - this.x1;
        double verticalDifference = this.y2 - this.y1;
        double length = Math.sqrt(horizontalDifference * horizontalDifference + verticalDifference * verticalDifference);
        
        double[] normalVector1 = { -verticalDifference / length, horizontalDifference / length };
        double[] normalVector2 = { verticalDifference / length, -horizontalDifference / length };

        double midpointX = (this.x1 + this.x2) / 2;
        double midpointY = (this.y1 + this.y2) / 2;
        double dist1 = Math.sqrt((midpointX + normalVector1[0] - x) * (midpointX + normalVector1[0] - x) +
                                 (midpointY + normalVector1[1] - y) * (midpointY + normalVector1[1] - y));
        double dist2 = Math.sqrt((midpointX + normalVector2[0] - x) * (midpointX + normalVector2[0] - x) +
                                 (midpointY + normalVector2[1] - y) * (midpointY + normalVector2[1] - y));
                                 
        return dist1 < dist2 ? normalVector1 : normalVector2;
    }
}
