package assignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class JBrainTetris extends JTetris{
    Brain dumb;
    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }

    JBrainTetris(){
        dumb = new GoodBrain();


        timer = new javax.swing.Timer(DELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick(dumb.nextMove(board));
            }
        });
    }
}
