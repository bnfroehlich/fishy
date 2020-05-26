package bfroehlich.fishy;

import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;

public class EvilFish extends Fish {
	
	private static HashMap<String, Image[]> images = new HashMap<String, Image[]>();
	
	private String name;
	private double mySpeed;
	
	public EvilFish(String name, Image image, Image leftFacingImage, Dimension size, Board board, Direction direction, double speed) {
		super(image, leftFacingImage, size, board);
		this.name = name;
		if(direction == Direction.EAST) {
			mySpeed = speed;
		}
		else if(direction == Direction.WEST) {
			mySpeed = -speed;
		}
	}
	
	public static EvilFish theSourceOfAllEvil(String name, Board board, Direction direction, double speed, int width) {
		name = name.toLowerCase();
		if(name.equals("")) {
			name = getEnemyBySize(width, false);
		}
		HashMap<String, Dimension> aspectRatios = new HashMap<String, Dimension>();
		aspectRatios.put("guppy", new Dimension(15, 5));
		aspectRatios.put("angelfish", new Dimension(80, 39));
		aspectRatios.put("clayfish", new Dimension(160, 59));
		aspectRatios.put("greenfish", new Dimension(15, 7));
		aspectRatios.put("greentamale", new Dimension(15, 5));
		aspectRatios.put("lobster", new Dimension(80, 60));
		aspectRatios.put("orangetamale", new Dimension(15, 5));
		aspectRatios.put("orca", new Dimension(150, 66));
		aspectRatios.put("pirhana", new Dimension(80, 51));
		aspectRatios.put("poisonfish", new Dimension(40, 15));
		aspectRatios.put("redfish", new Dimension(15, 7));
		aspectRatios.put("redtamale", new Dimension(15, 5));
		aspectRatios.put("roundfish", new Dimension(140, 82));
		aspectRatios.put("whale", new Dimension(160, 53));
		aspectRatios.put("squid", new Dimension(80, 30));
		aspectRatios.put("turtle", new Dimension(15, 8));
		aspectRatios.put("uglyfish", new Dimension(80, 31));
		aspectRatios.put("yellowfish", new Dimension(15, 7));
		aspectRatios.put("browntamale", new Dimension(15, 5));
		aspectRatios.put("bus", new Dimension(160, 47));
		Image[] myFace = images.get(name);
		if(myFace == null) {
			Image[] stuff = getImages(name);
			images.put(name, stuff);
			myFace = stuff;
		}
		Dimension s = aspectRatios.get(name);
		double ratio = ((double) width)/((double) s.width);
		Dimension size = new Dimension(width, (int) ((double) s.height*ratio));
		return new EvilFish(name, myFace[0], myFace[1], size, board, direction, speed);
	}
	
	public static Image[] getImages(String name) {
		Image[] stuff = {Manager.loadImage(""+name+".png", null), Manager.loadImage("leftfacing"+name+".png", null)};
		return stuff;
	}
	
	public static String getEnemyBySize(int width, boolean smallerThan) {
		int index = 0;
		if(width<20) index = 0;
		else if(width<30)  index = 1;
		else if(width<42.5) index = 2;
		else if(width<47.5) index = 3;
		else if(width<65) index = 4;
		else if(width<85) index = 5;
		else if(width<107.5) index = 6;
		else if(width<132.5) index = 7;
		else if(width<160) index = 8;
		else if(width<190) index = 9;
		else if(width<222.5) index = 10;
		else if(width<257.5) index = 11;
		else if(width<295) index = 12;
		else if(width<350) index = 13;
		else index = 14;
		
		if(smallerThan) index -= 1; //smallerThan returns the largest fish that is always smaller than the width
		return listEnemiesBySize()[index];
	}
	
	private static String[] listEnemiesBySize() {
		String[] enemies = {"orangetamale", "browntamale", "redtamale", "greentamale", "angelfish", "uglyfish", "lobster", "pirhana", "turtle", "squid", "roundfish", "clayfish", "orca", "whale", "bus"};
		return enemies;		
	}
	
	public boolean isBottomDweller() {
		return name.equals("lobster") || name.equals("turtle");
	}
	
	public void update() {
		super.update();
		xSpeed = mySpeed;
	}
	
	public String getName() {
		return name;
	}
}