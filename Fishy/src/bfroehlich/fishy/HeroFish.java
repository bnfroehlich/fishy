package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;

public class HeroFish extends Fish {
	
	public static Dimension[][] rankSizes = {
		{new Dimension(30, 10), new Dimension(99, 33), new Dimension(175, 58)},
		{new Dimension(30, 15), new Dimension(100, 49), new Dimension(175, 87)}
	};
	
	private HashMap<Direction, Boolean> pressed;
	private Image blueFish;
	private Image leftFacingBlueFish;
	private Image clownFish;
	private Image leftFacingClownFish;
	
	private int myTummy;
	private int currentRank;
	private int level;
	
	private long lastUpdate;
	private int invincibilityMillis;
	
	private boolean hasShield;
	
	public HeroFish(Board board) {
		super(Manager.loadImage("bluefish.png", null), Manager.loadImage("leftfacingbluefish.png",null), rankSizes[0][0], board);
		pressed = new HashMap<Direction, Boolean>();
		pressed.put(Direction.NORTH, false);
		pressed.put(Direction.SOUTH, false);
		pressed.put(Direction.EAST, false);
		pressed.put(Direction.WEST, false);
		blueFish = Manager.loadImage("bluefish.png", null);
		leftFacingBlueFish = Manager.loadImage("leftfacingbluefish.png",null);
		clownFish = Manager.loadImage("clownfish.png", null);
		leftFacingClownFish = Manager.loadImage("leftfacingclownfish.png",null);
	}

	public void directionPressed(Direction direction) {
		pressed.put(direction, true);
	}

	public void directionReleased(Direction direction) {
		pressed.put(direction, false);
	}
	
	public void releaseAllDirections() {
		for(Direction direction : Direction.values()) {
			pressed.put(direction, false);
		}
	}
	
	public void update() {
		super.update();
		if(pressed.get(Direction.NORTH)) {
			ySpeed -= .05;
		}
		if(pressed.get(Direction.SOUTH)) {
			ySpeed += .05;
		}
		if(pressed.get(Direction.EAST)) {
			xSpeed += .05;
		}
		if(pressed.get(Direction.WEST)) {
			xSpeed -= .05;
		}
		long time = System.currentTimeMillis();
		if(invincibilityMillis > 0) {
			invincibilityMillis -= (time - lastUpdate);
			if(invincibilityMillis < 0) {
				invincibilityMillis = 0;
			}
		}
		lastUpdate = time;
	}
	
	public int getCurrentRank() {
		return currentRank;
	}
	
	public int getTummy() {
		return myTummy;
	}
	
	public void setTummy(int tummy) {
		this.myTummy = tummy;
	}
	
	public void setCurrentRank(int currentRank) {
		this.currentRank = currentRank;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	public boolean hasShield() {
		return hasShield;
	}
	
	public void setHasShield(boolean hasShield) {
		this.hasShield = hasShield;
	}
	
	public void setInvincibilityMillis(int seconds) {
		this.invincibilityMillis = seconds;
	}
	
	public boolean isInvincible() {
		return invincibilityMillis > 0;
	}
	
	public void youAteAFish(int deliciousness) {
		myTummy += deliciousness;
	}
	
	public void stop() {
		releaseAllDirections();
		setXSpeed(0);
		setYSpeed(0);
	}
	
	public void resetRank() {
		setCurrentRank(0);
		setTummy(0);
		dimensions = rankSizes[level][0];
	}
	
	public boolean rankUp() {
		if(currentRank == 0) {
			dimensions = rankSizes[level][1];
			currentRank = 1;
			myTummy = 0;
			return false;
		}
		else if(currentRank == 1) {
			dimensions = rankSizes[level][2];
			currentRank = 2;
			myTummy = 0;
			return false;
		}
		else {
			return true;
		}
	}
	
	public void incrementGirth() {
		dimensions = new Dimension(dimensions.width+5, (dimensions.width+5)/3);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		if(hasShield) {
			g.setColor(Color.GREEN);
			g.drawRect(0, 0, dimensions.width, dimensions.height);
		}
		else if(invincibilityMillis > 0) {
			g.setColor(Color.YELLOW);
			g.drawRect(0, 0, dimensions.width, dimensions.height);
		}
	}
}