/*The Swarm class will be used to contain a list of Block and Obstacle objects. It will also act as
the "communication hub" where the information left in the environment by the blocks can be accessed
by other blocks through this class.*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

class Swarm {
    //We are storing each block/obstacle in an array list
    private ArrayList<Block> blocks;
    private ArrayList<Obstacle> obstacles;
    private Random rand = new Random();

    Swarm() {
        //Constructor for the array lists, swarm object takes no initializer
        this.blocks = new ArrayList<>();
        this.obstacles = new ArrayList<>();
    }

    void display() {
        int count = 0;
        int[] array = new int[blocks.size()];
        //Here we are calling the display method for the blocks/obstacles
        for (Block b : blocks) {
            //Block has marked the goal
            int return_value = b.display(blocks, obstacles);
            if (return_value == 1) {
                array[count] = return_value;
                //The block determines if it was first or second or third etc etc
                b.shape_location = Arrays.stream(array).sum() - 1;
            }
            count++;
        }
        /*The blocks mark the goal when they get in range of it. Blocks are ordered with the oldest
        blocks being the first position in the shape. This is done by the for loop itself allowing
        the older blocks to claim a spot in the shape first. Might fix this, might not. Still
        works, right? Doing it like this allows the blocks to not always get the right number of
        total blocks in the system right! Makes for more interesting emergent behaviours.*/
        for (Block b : blocks) {
            b.estimated_number_of_blocks = Arrays.stream(array).sum();
        }
        for (Obstacle o : obstacles) {
            o.display();
        }
    }

    int size() {
        //Used in Simulator class to add or remove blocks
        return (blocks.size());
    }

    void add_blocks(Block b) {
        //This method will add blocks into the block array list
        blocks.add(b);
    }

    void remove_blocks() {
        //This method will remove a random block from the block array list
        int random = rand.nextInt(blocks.size());
        blocks.remove(blocks.get(random));
    }

    void add_obstacles(Obstacle o) {
        //Same as the add_blocks method
        obstacles.add(o);
    }
}