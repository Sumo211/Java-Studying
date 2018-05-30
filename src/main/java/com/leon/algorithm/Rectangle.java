package com.leon.algorithm;

/**
 * @see <a href="https://www.interviewcake.com/question/java/rectangular-love">Source</a>
 * What if there is no intersection?
 * What if one rectangle is entirely contained in the other?
 * What if the rectangles don't really intersect but share an edge?
 * Do some parts of your method seem very similar? Can they be refactored so you repeat yourself less?
 * The hard part isn't the time or space optimization—it's getting something that works and is readable.
 * To keep your thoughts clear and avoid bugs, take time to:
 * 1. Think up and draw out all the possible cases.
 * 2. Use very specific and descriptive variable names.
 * O(1) time and O(1) space
 */
class Rectangle {

    // coordinates of bottom left corner
    private int leftX;

    private int bottomY;

    // dimensions
    private int width;

    private int height;

    Rectangle(int leftX, int bottomY, int width, int height) {
        this.leftX = leftX;
        this.bottomY = bottomY;
        this.width = width;
        this.height = height;
    }

    int getLeftX() {
        return leftX;
    }

    int getBottomY() {
        return bottomY;
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    static Rectangle findRectangleOverlap(Rectangle rec1, Rectangle rec2) {
        // get the x and y overlap points and lengths
        RangeOverlap xOverlap = findRangeOverlap(rec1.getLeftX(), rec1.getWidth(), rec2.getLeftX(), rec2.getWidth());
        RangeOverlap yOverlap = findRangeOverlap(rec1.getBottomY(), rec1.getHeight(), rec2.getBottomY(), rec2.getHeight());

        // return "zero" rectangle if there is no overlap
        if (xOverlap.getLength() == 0 || yOverlap.getLength() == 0) {
            return new Rectangle(0, 0, 0, 0);
        }

        return new Rectangle(xOverlap.getStartPoint(), yOverlap.getStartPoint(), xOverlap.getLength(), yOverlap.getLength());
    }

    private static RangeOverlap findRangeOverlap(int point1, int length1, int point2, int length2) {
        // find the highest start point and lowest end point
        // the highest ("rightmost" or "upmost") start point is the start point of the overlap
        // the lowest end point is the end point of the overlap
        int highestStartPoint = Math.max(point1, point2);
        int lowestEndPoint = Math.min(point1 + length1, point2 + length2);

        // return empty overlap if there is no overlap
        if (highestStartPoint >= lowestEndPoint) {
            return new RangeOverlap(0, 0);
        }

        // compute the overlap length
        int overlapLength = lowestEndPoint - highestStartPoint;

        return new RangeOverlap(highestStartPoint, overlapLength);
    }

    private static class RangeOverlap {

        private int startPoint;

        private int length;

        RangeOverlap(int startPoint, int length) {
            this.startPoint = startPoint;
            this.length = length;
        }

        int getStartPoint() {
            return startPoint;
        }

        int getLength() {
            return length;
        }

    }

}
