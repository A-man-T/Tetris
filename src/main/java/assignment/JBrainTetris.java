package assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class JBrainTetris extends JTetris{

    private final Timer brainTimer;
    Brain dumb;
    private final int BRAINDELAY = 300;


    public static void main(String[] args) {
        createGUI(new JBrainTetris());
    }

    @Override
    public void updateTimer(){
        super.updateTimer();
        double value = ((double)speed.getValue())/speed.getMaximum();
        brainTimer.setDelay((int)(BRAINDELAY - value*BRAINDELAY));
    }

    @Override
    public void startGame() {
        super.startGame();
        brainTimer.start();
    }

    @Override
    public void stopGame() {
        super.stopGame();
        brainTimer.stop();
    }

    JBrainTetris() {

        dumb = new LameBrain();

        brainTimer = new javax.swing.Timer(BRAINDELAY, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    //System.out.println(dumb.nextMove(board));
                    tick(dumb.nextMove(board));
                    /*
                    for(int y=0;y<board.getHeight();y++){
                        for(int x=0; x<board.getWidth();x++) {
                            System.out.print(board.getGrid(x,y)+" ");
                        }
                        System.out.println();
                    }

                     */
            }
        });


    }

}
