//Here we draw the GUI to be used within the program

import processing.core.PApplet;
import processing.core.PConstants;

import java.util.Arrays;

class GUI {
    //Allows us to display number of blocks within the simulator
    private int block_amount;
    //Inheritance variable
    private PApplet p;
    //The centre of the GUI in the x-axis, since everything is centered we don't need to change this variable
    private int centreX = 1050;
    //Array used to record the actions of the user
    private boolean[] block_selection_array = new boolean[9];
    //This variable will be used in the simulator class to control the number of blocks present
    int user_block_selection;
    //Height and width of each box
    private int box_height = 90;
    private int box_width = 300;

    GUI(PApplet inheritance) {
        this.p = inheritance;
        this.block_selection_array[0] = true;
        this.user_block_selection = 1;
    }

    void display(int b_amount) {
        //We need to update these variables first in order to keep simulator_status updated
        this.block_amount = b_amount;
        //The GUI is drawn here
        draw_block_boxes();
        highlight_block_selection();
        //
        dot();
        minus_sign();
        triangle();
        square();
        pentagon();
        hexagon();
        fork();
        octagon();
        plus_sign();
        //
        simulator_status();
        frame_rate();
    }

    private void draw_block_boxes() {
        /*Draws a row of boxes next to the simulator. We treat the height of the simulator as it's
        width in the block class, even though the width of the window is larger than it's height.
        This ensures that the blocks stay within the simulator and do not wander into the GUI.*/
        //Outline for the boxes
        p.strokeWeight(0);
        p.stroke(0);
        p.rect(p.height, 0, box_width + 150, p.height);
        for (var y = 90; y <= p.height; y += box_height) {
            /*Boxes will be drawn from top to bottom next to the simulator, with each box being a
            certain height(box_height) and width (box_width). Simulator is 1000x1000 and the GUI
            stretches 300 pixels in x axis beyond that.*/
            p.strokeWeight(1);
            p.rect(p.height, y, box_width, box_height);
            if ((p.mouseX >= p.height) && (p.mouseY >= y) && (p.mouseY <= y + box_height)) {
                //If the mouse is within one of these boxes, we highlight it
                p.strokeWeight(2);
                p.rect(p.height, y, box_width, box_height);
                if (p.mousePressed) {
                    //Recording which box the player selected using the boolean array selection_array
                    int box_selected = (y / box_height) - 1;
                    Arrays.fill(block_selection_array, Boolean.FALSE);
                    block_selection_array[box_selected] = true;
                }
            }
        }
    }

    private void highlight_block_selection() {
        /*Highlighting the box the player selected in the draw_boxes method. By default the first
        box will be highlighted.*/
        p.strokeWeight(4);
        p.stroke(0, 162, 255);
        for (var k = 0; k < block_selection_array.length; k++) {
            if (block_selection_array[k]) {
                p.rect(p.height, 90 + (k * box_height), box_width, box_height);
                user_block_selection = k;
            }
        }
        p.stroke(0);
    }

    private void dot() {
        //Centre of first box, X =1050 Y = 140 (100 - 180)
        int box_order = 1;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.stroke(255, 100, 100);
        p.fill(255, 100, 100);
        p.ellipse(centreX, centreY, 5, 5);
        p.stroke(0);
        p.fill(0);
    }

    private void minus_sign() {
        //Centre of second box, X = 1050 Y = 220 (180 - 260)
        int box_order = 2;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.stroke(255, 179, 186);
        p.strokeWeight(7);
        p.line(centreX - 25, centreY, centreX + 25, centreY);
        p.stroke(0);
    }

    private void triangle() {
        //Centre of third box, X = 1050 Y = 300 (260 - 340)
        int box_order = 3;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.strokeWeight(2);
        p.stroke(255, 223, 186);
        p.fill(255, 223, 186);
        p.beginShape(PConstants.TRIANGLES);
        p.vertex(centreX - 25, centreY + 25);
        p.vertex(centreX + 25, centreY + 25);
        p.vertex(centreX, centreY - 25);
        p.endShape();
        p.stroke(0);
        p.fill(0);
    }

    private void square() {
        //Centre of fourth box, X = 1050 Y = 380 (340 - 420)
        int box_order = 4;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.strokeWeight(1);
        p.stroke(255, 255, 186);
        p.fill(255, 255, 186);
        p.beginShape();
        p.vertex(centreX - 25, centreY - 25);
        p.vertex(centreX - 25, centreY + 25);
        p.vertex(centreX + 25, centreY + 25);
        p.vertex(centreX + 25, centreY - 25);
        p.endShape(PConstants.CLOSE);
        p.stroke(0);
        p.fill(0);
    }

    private void pentagon() {
        //Centre of fifth box, X = 1050 Y = 460 (420 - 500)
        int box_order = 5;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.strokeWeight(1);
        p.stroke(186, 255, 201);
        p.fill(186, 255, 201);
        p.beginShape();
        p.vertex(centreX, centreY - 25);
        p.vertex(centreX + 23, centreY - 10);
        p.vertex(centreX + 15, centreY + 20);
        p.vertex(centreX - 15, centreY + 20);
        p.vertex(centreX - 23, centreY - 10);
        p.endShape(PConstants.CLOSE);
        p.stroke(0);
        p.fill(0);
    }

    private void hexagon() {
        //Centre of sixth box, X = 1050 Y = 540 (500 - 580)
        int box_order = 6;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.strokeWeight(1);
        p.stroke(186, 225, 255);
        p.fill(186, 225, 255);
        p.beginShape();
        p.vertex(centreX - 15, centreY - 25);
        p.vertex(centreX + 15, centreY - 25);
        p.vertex(centreX + 30, centreY);
        p.vertex(centreX + 15, centreY + 25);
        p.vertex(centreX - 15, centreY + 25);
        p.vertex(centreX - 30, centreY);
        p.endShape(PConstants.CLOSE);
        p.stroke(0);
        p.fill(0);
    }

    private void fork() {
        //Centre of seventh box, X = 1050 Y = 620 (580 - 660)
        int box_order = 7;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.strokeWeight(7);
        p.stroke(186, 195, 255);
        p.fill(186, 195, 255);
        p.beginShape();
        p.line(centreX, centreY, centreX, centreY + 25);
        p.line(centreX, centreY, centreX + 25, centreY - 15);
        p.line(centreX, centreY, centreX - 25, centreY - 15);
        p.endShape(PConstants.CLOSE);
        p.stroke(0);
        p.fill(0);
    }

    private void octagon() {
        //Centre of eighth box, X = 1050 Y = 700 (660 - 740)
        int box_order = 8;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.strokeWeight(1);
        p.stroke(186, 164, 255);
        p.fill(186, 164, 255);
        p.beginShape();
        p.vertex(centreX - 10, centreY - 25);
        p.vertex(centreX + 10, centreY - 25);
        p.vertex(centreX + 25, centreY - 10);
        p.vertex(centreX + 25, centreY + 10);
        p.vertex(centreX + 10, centreY + 25);
        p.vertex(centreX - 10, centreY + 25);
        p.vertex(centreX - 25, centreY + 10);
        p.vertex(centreX - 25, centreY - 10);
        p.endShape(PConstants.CLOSE);
        p.stroke(0);
        p.fill(0);
    }

    private void plus_sign() {
        //Centre of ninth box, X = 1050 Y = 780 (740 - 820)
        int box_order = 9;
        int centreY = 90 + box_order * box_height - box_height / 2;
        p.stroke(186, 123, 255);
        p.strokeWeight(7);
        p.line(centreX, centreY - 25, centreX, centreY + 25);
        p.line(centreX - 25, centreY, centreX + 25, centreY);
        p.stroke(0);
    }

    private void simulator_status() {
        p.fill(0);
        p.textSize(20);
        //Aligning the text to the center
        p.textAlign(PConstants.CENTER);
        //Prints basic information about state of the simulation
        p.text("Number of Blocks: " + block_amount, centreX, 30);
        p.text("X: " + p.mouseX, centreX, 60);
        p.text("Y: " + p.mouseY, centreX, 80);
    }

    private void frame_rate() {
        //Prints the frame rate in the top left corner
        p.textSize(12);
        p.fill(255, 170, 0);
        p.text("Frame rate: " + (int) p.frameRate, 70, 30);
    }
}