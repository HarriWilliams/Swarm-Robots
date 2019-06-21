/*This class will create the blocks, they will avoid each other, stay as a group and match each
other's velocities.

Here we use a combination of vectors to affect the Block's motion. Each rule will produce a vector,
moving the block. We're using vectors as it allows for smoother motion and application of forces.
*/

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;

class Block {
    //Inheritance variable, allows us to use Processing methods
    private PApplet P;
    //Size of the block
    private int block_radius;
    //Maximum speed of the Block
    private float maxSpeed;
    //Magnitude step of any new force
    private float maxForce;
    //Vectors that defines the Block's location and velocity
    private PVector location;
    private PVector velocity;
    //If the block is in range of the goal and its position in the shape
    private int in_range = 0;
    int shape_location = 0;
    //Estimation of how many blocks have marked the goal
    int estimated_number_of_blocks = 0;
    //The goal is the position of the mouse, blocks will have to find it
    private PVector goal;

    Block(PApplet inheritance, float xPos, float yPos) {
        this.P = inheritance;
        this.block_radius = 10;
        this.maxSpeed = (float) 2.5;
        this.maxForce = (float) 0.5;
        this.location = new PVector(xPos, yPos);
        //Giving each block an initial random velocity
        this.velocity = PVector.random2D();
    }

    int display(ArrayList<Block> blocks, ArrayList<Obstacle> obstacles) {
        /*Draws the block, allows it to move across the screen (like Pac-Man), and updates the
        block's vectors. Also when the block moves close enough to the goal, it will mark it by
        returning a value to the swarm class. This is like when ants use pheromones to deduce how
        many other ants are in the same location. The blocks are storing information within the
        environment itself, not globally.*/
        draw();
        update(blocks, obstacles);
        marking();
        return in_range;
    }

    private void draw() {
        P.ellipseMode(PConstants.RADIUS);
        P.fill(0, 162, 255);
        P.textSize(14);
        P.text(shape_location, location.x, location.y + 6);
        //Drawing the block itself
        P.noFill();
        P.strokeWeight(2);
        P.ellipse(location.x, location.y, block_radius, block_radius);
    }

    private void update(ArrayList<Block> blocks, ArrayList<Obstacle> obstacles) {
        //The block's goal, i.e. the mouse
        goal = new PVector(P.mouseX, P.mouseY);
        //Takes all the rules the blocks follow as separate forces and adds them to the velocity vector
        PVector all_forces = new PVector(0, 0);
        all_forces.add(separation(blocks));
        all_forces.add(obstacle_avoidance(obstacles));
		//FINITE STATE MACHINE
        //When the block has not found the goal, LOCOMOTION MODE
        if (in_range == 0) {
            all_forces.add(goal_seeking());
            all_forces.add(cohesion(blocks));
            all_forces.add(alignment(blocks));
        }
        //When the block has found the goal, CLUSTERING MODE
        else {
            all_forces.add(cluster());
        }
        //Adding some artificial noise to the blocks to make their movements more natural and chaotic
        PVector noise = PVector.random2D();
        noise.div(3);
        all_forces.add(noise);
        velocity.add(all_forces);
        velocity.limit(maxSpeed);
        location.add(velocity);
    }

    private void marking() {
        //When the block finds the goal, they mark it
        float distance_from_goal = PVector.dist(this.location, goal);
        if (distance_from_goal > 0 && distance_from_goal <= block_radius * 10) {
            in_range = 1;
        } else {
            in_range = 0;
            shape_location = 0;
        }
    }

    private PVector goal_seeking() {
        //The blocks will search for the goal, when they find it they will move towards it
        PVector goal_force = new PVector(0, 0);
        float distance = PVector.dist(this.location, goal);
        if (distance > 0 && distance < block_radius * 10) {
            //Subtracting vectors gets you a new one that points from current location to goal
            goal_force = PVector.sub(goal, this.location);
            goal_force.setMag(maxForce);
            goal_force.limit(maxSpeed);
        }
        return goal_force;
    }

    private PVector obstacle_avoidance(ArrayList<Obstacle> obstacles) {
        //New vector that points away from nearby obstacles
        PVector avoid_obstacles = new PVector(0, 0);
        int count = 0;
        for (Obstacle o : obstacles) {
            //Calculating the force necessary to avoid nearby obstacles
            float distance = PVector.dist(this.location, o.location);
            if (distance > 0 && distance < o.obstacle_radius * 3) {
                PVector recovery = PVector.sub(this.location, o.location);
                recovery.normalize();
                /*We want the block to move further away from obstacles which are closer to it, so
                we give each obstacle a weight depending on their distance from the block.*/
                recovery.div(distance);
                avoid_obstacles.add(recovery);
                count++;
            }
        }
        if (count > 0) {
            avoid_obstacles.div(count);
            avoid_obstacles.setMag(maxForce);
            avoid_obstacles.limit(maxSpeed);
            //Giving this new vector a weight, which places it as a higher priority than all the other forces
            avoid_obstacles.mult(2);
        }
        return avoid_obstacles;
    }

    private PVector separation(ArrayList<Block> blocks) {
        //New vector that points away from nearby blocks
        PVector avoid_blocks = new PVector(0, 0);
        int count = 0;
        for (Block b : blocks) {
            //Calculating the force necessary to avoid nearby blocks
            float distance = PVector.dist(this.location, b.location);
            if (distance > 0 && distance < block_radius * 3) {
                PVector difference = PVector.sub(this.location, b.location);
                difference.normalize();
                //Giving the difference force a weight based on the distance away from blocks, closer blocks are higher priority
                difference.div(distance);
                avoid_blocks.add(difference);
                count++;
            }
        }
        if (count > 0) {
            avoid_blocks.div(count);
            avoid_blocks.setMag(maxForce);
            avoid_blocks.limit(maxSpeed);
            //We are artificially increasing the separation force in order to make this a higher priority than the other forces
            avoid_blocks.mult(2);
        }
        return avoid_blocks;
    }

    private PVector alignment(ArrayList<Block> blocks) {
        //New vector, the block's velocity will match all the other blocks within a certain distance
        PVector alignment_force = new PVector(0, 0);
        int count = 0;
        for (Block other : blocks) {
            float distance = PVector.dist(this.location, other.location);
            if (distance > 0 && distance < block_radius * 6) {
                alignment_force.add(other.velocity);
                count++;
            }
        }
        if (count > 0) {
            alignment_force.div(count);
            alignment_force.sub(this.velocity);
            alignment_force.setMag(maxForce);
            alignment_force.limit(maxSpeed);
        }
        return (alignment_force);
    }

    private PVector cohesion(ArrayList<Block> blocks) {
        //New vector, the blocks will move towards the average position of nearby blocks
        PVector cohesion_force = new PVector(0, 0);
        int count = 0;
        for (Block other : blocks) {
            float distance = PVector.dist(this.location, other.location);
            if (distance > 0 && distance < block_radius * 6) {
                cohesion_force.add(other.location);
                count++;
            }
        }
        if (count > 0) {
            cohesion_force.div(count);
            cohesion_force.sub(this.location);
            cohesion_force.setMag(maxForce);
            cohesion_force.limit(maxSpeed);
        }
        return (cohesion_force);
    }

    private int[][] polygon(int number_of_points) {
        /*This function can generate a set of equidistant points around a circle to form shapes.
        i.e. 4 points with equal distance from each other on a circle produces a square. This
        function will be used in the cluster method to produce shapes 2, 3, 4, 5, 6, 8.*/
        int radius = 50;
        int angle_between = 360 / number_of_points;
        int[][] points = new int[number_of_points][2];
        for (int x = 0; x < number_of_points; x++) {
            //
            double temp_x_pos = radius * Math.sin(Math.toRadians(angle_between * x));
            double temp_y_pos = radius - radius * (1 - Math.cos(Math.toRadians(angle_between * x)));
            //Rotating the points to make them more aesthetically pleasing.
            points[x][0] = (int) (goal.x + temp_x_pos * Math.cos(Math.toRadians((float) angle_between / 2)) - temp_y_pos * Math.sin(Math.toRadians((float) angle_between / 2)));
            points[x][1] = (int) (goal.y + temp_y_pos * Math.cos(Math.toRadians((float) angle_between / 2)) + temp_x_pos * Math.sin(Math.toRadians((float) angle_between / 2)));
        }
        return points;
    }

    private int[][] cross(int number_of_points, int layer, int offset) {
        //
        int radius = 35;
        int angle_between = 360 / layer;
        int[][] points = new int[number_of_points][2];
        //
        points[0][0] = (int) goal.x;
        points[0][1] = (int) goal.y;
        for (int x = 1; x <= layer; x++) {
            //
            double x1 = radius * Math.sin(Math.toRadians(angle_between * (x - 1)));
            double y1 = radius - radius * (1 - Math.cos(Math.toRadians(angle_between * (x - 1))));
            points[x][0] = (int) (goal.x + (x1 * Math.cos(Math.toRadians((float) angle_between / 2 + offset)) - y1 * Math.sin(Math.toRadians((float) angle_between / 2 + offset))));
            points[x][1] = (int) (goal.y + (y1 * Math.cos(Math.toRadians((float) angle_between / 2 + offset)) + x1 * Math.sin(Math.toRadians((float) angle_between / 2 + offset))));
            //
            double x2 = (radius * 2) * Math.sin(Math.toRadians(angle_between * (x - 1)));
            double y2 = radius * 2 - (radius * 2) * (1 - Math.cos(Math.toRadians(angle_between * (x - 1))));
            points[x + layer][0] = (int) (goal.x + (x2 * Math.cos(Math.toRadians((float) angle_between / 2 + offset)) - y2 * Math.sin(Math.toRadians((float) angle_between / 2 + offset))));
            points[x + layer][1] = (int) (goal.y + (y2 * Math.cos(Math.toRadians((float) angle_between / 2 + offset)) + x2 * Math.sin(Math.toRadians((float) angle_between / 2 + offset))));
        }
        return points;
    }

    private PVector cluster() {
        //New vector, block will move towards it's position within the shape it thinks is trying to be made
        PVector position = new PVector(0, 0);
        switch (estimated_number_of_blocks) {
            case 1:
                //Point formed from 1 block
                float[] point = {goal.x, goal.y};
                position.x = point[0];
                position.y = point[1];
                break;
            case 2:
                //Line formed from 2 blocks
                int[][] line2 = polygon(estimated_number_of_blocks);
                position.x = line2[this.shape_location][0];
                position.y = line2[this.shape_location][1];
                break;
            case 3:
                //Triangle formed from 3 blocks
                int[][] triangle3 = polygon(estimated_number_of_blocks);
                position.x = triangle3[this.shape_location][0];
                position.y = triangle3[this.shape_location][1];
                break;
            case 4:
                //Square formed from 4 blocks
                int[][] square4 = polygon(estimated_number_of_blocks);
                position.x = square4[this.shape_location][0];
                position.y = square4[this.shape_location][1];
                break;
            case 5:
                //Pentagon formed from 5 blocks
                int[][] pentagon5 = polygon(estimated_number_of_blocks);
                position.x = pentagon5[this.shape_location][0];
                position.y = pentagon5[this.shape_location][1];
                break;
            case 6:
                //Hexagon formed from 6 blocks
                int[][] hexagon6 = polygon(estimated_number_of_blocks);
                position.x = hexagon6[this.shape_location][0];
                position.y = hexagon6[this.shape_location][1];
                break;
            case 7:
                //Fork formed from 7 blocks
                int[][] fork = cross(estimated_number_of_blocks, 3, 180);
                position.x = fork[this.shape_location][0];
                position.y = fork[this.shape_location][1];
                break;
            case 8:
                //Octagon formed from 8 blocks
                int[][] octagon8 = polygon(estimated_number_of_blocks);
                position.x = octagon8[shape_location][0];
                position.y = octagon8[shape_location][1];
                break;
            case 9:
                //Cross formed from 9 blocks
                int[][] cross = cross(estimated_number_of_blocks, 4, 45);
                position.x = cross[this.shape_location][0];
                position.y = cross[this.shape_location][1];
                break;
        }
        PVector cluster_force = PVector.sub(position, this.location);
        PVector force = PVector.sub(cluster_force, this.velocity);
        cluster_force.setMag(cluster_force.mag() / 20);
        force.limit(this.maxForce);
        return force;
    }
}