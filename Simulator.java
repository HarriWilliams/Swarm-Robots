/*Author - Harri Williams created on 11/02/2019
Version 0.9 - Boids model integrated, Clustering integrated, GUI integrated

References/Inspiration:

https://processing.org/tutorials/ (accessed 11/02/2019)
Tutorials on how to use Processing language

https://processing.org/examples/accelerationwithvectors.html (accessed 11/02/2019)
Details how to use vectors in Processing

https://happycoding.io/tutorials/java/processing-in-java (accessed 11/02/2019)
Details how to use Processing with Intellij

http://www.vergenet.net/~conrad/boids/pseudocode.html (accessed 12/02/2019)
Pseudo-code describing the Boids algorithm.

https://math.stackexchange.com/questions/332743/calculating-the-coordinates-of-a-point-on-a-circles-circumference-from-the-radiu (accessed 30/03/2019)
Formulas that describe automatic shape generation in the block class.*/

import processing.core.PApplet;

class Simulator extends PApplet {
    //Initialising the swarm and the GUI objects
    private Swarm swarm;
    private GUI gui;
    //Amount of blocks initially
    private int swarm_size = 0;
    //
    private int[] block_amounts = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};

    public void settings() {
        //Setting up the size of the window
        size(1200, 900);
        //Creating the GUI and the swarm (blocks and obstacles)e
        swarm = new Swarm();
        gui = new GUI(this);
        //Creating the obstacles here because we don't need to update the amount of obstacles
        int obstacle_amount = 45;
        for (var k = 0; k <= obstacle_amount; k++) {
            swarm.add_obstacles(new Obstacle(this, 0, (float) (height / obstacle_amount) * k)); //Top of screen
            swarm.add_obstacles(new Obstacle(this, 900, (float) (height / obstacle_amount) * k)); //Bottom of screen
            swarm.add_obstacles(new Obstacle(this, (float) (height / obstacle_amount) * k, 0)); //Left of screen
            swarm.add_obstacles(new Obstacle(this, (float) (height / obstacle_amount) * k, height)); //Right of screen
        }
        for (var j = 0; j < 10; j++){
            swarm.add_obstacles(new Obstacle(this, (float) (height / 10) * j, (float) (height / 10) * j));
        }
    }

    public void draw() {
        //Here we draw the background for the window, remove the cursor and insert the blocks
        background(255);
        //Updating the amount of blocks depending on users choice
        swarm_update();
        //Initialising the blocks and GUI
        swarm.display();
        gui.display(swarm_size);
    }

    private void swarm_update() {
        /*The user_selection variable related to what box the user selected in the GUI. We then
        update the number of blocks depending on the choice the user made.*/
        swarm_size = block_amounts[gui.user_block_selection];
        int difference = swarm_size - swarm.size();
        if (difference > 0) {
            for (var i = 0; i < difference; i++) {
                swarm.add_blocks(new Block(this, (float) height / 2, (float) height / 2));
            }
        } else if (difference < 0) {
            for (var j = 0; j > difference; j--) {
                swarm.remove_blocks();
            }
        }
    }

    public static void main(String[] args) {
        /*We are extending the PApplet class into our simulator class (simulator is now an instance
        of the PApplet class), now we can pass our simulator class back into PApplet.*/
        String[] appletArgs = {"Simulator"};
        Simulator sim = new Simulator();
        PApplet.runSketch(appletArgs, sim);
    }
}