package bfroehlich.fishy;

import java.awt.Rectangle;
import java.util.Iterator;

public class Engine implements Runnable {
	
	public static int TICK_LENGTH = 7;
	
	private Board board;
	private Manager manager;
	
	private boolean going;
	private Thread spirit;
	
	public Engine(Board board, Manager manager) {
		this.board = board;
		this.manager = manager;
	}

	public void start() {
        board.requestFocusInWindow();
        
        this.going = true;
        
        if(spirit == null || !spirit.isAlive()) {
        	this.spirit = new Thread(this);
        	this.spirit.start();
        }
    }

    public void stop() {
        this.going = false;
    }
    
    public void toggleGoing() {
    	if(going) {
    		stop();
    	}
    	else {
    		start();
    	}
    }

    public void run() {
        while (this.going) {
        	tick();
        }
    }
    
    private void tick() {
    	long time = System.currentTimeMillis();
    	board.repaint();
    	try {
    		manager.tick(time);
    		long timePassed = (System.currentTimeMillis() - time);
            if(timePassed < TICK_LENGTH) {
            	Thread.sleep(TICK_LENGTH - timePassed);
            }
            board.requestFocusInWindow();
            board.repaint();
    	} catch (InterruptedException ie) {
            System.err.println("World: sleep() interrupted: "
                    + ie.getMessage());
        }
    }
}
