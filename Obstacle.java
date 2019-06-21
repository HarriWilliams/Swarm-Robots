//This class will create obstacles for the blocks to avoid

import processing.core.PApplet;
import processing.core.PVector;

class Obstacle {
    //Obstacles are similar to the Blocks, but they cannot move and are much larger
    private PApplet P;
    PVector location;
    int obstacle_radius;

    Obstacle(PApplet inheritance, float xPos, float yPos) {
        this.P = inheritance;
        this.location = new PVector(xPos, yPos);
        this.obstacle_radius = 15;
    }

    void display() {
        //The obstacles are drawn here
        P.fill(255, 0, 0);
        P.strokeWeight(0);
        P.ellipse(location.x, location.y, obstacle_radius, obstacle_radius);
        P.fill(255);
    }
}