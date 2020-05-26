package bfroehlich.fishy;

import java.awt.Dimension;
import java.awt.Image;
import java.util.HashMap;

public class RankEvilFish extends Fish {
	
	private static HashMap<String, Image[]> images = new HashMap<String, Image[]>();
	
	private String name;
	private double mySpeed;
	private int rank;
	
	public RankEvilFish(String name, Image image, Image leftFacingImage, Dimension size, Board board, Direction direction, double speed, int rank) {
		super(image, leftFacingImage, size, board);
		this.name = name;
		this.rank = rank;
		if(direction == Direction.EAST) {
			mySpeed = speed;
		}
		else if(direction == Direction.WEST) {
			mySpeed = -speed;
		}
	}
	
	public static RankEvilFish theSourceOfAllEvil(String name, Board board, Direction direction, double speed, int rank) {
		name = name.toLowerCase();
		HashMap<String, Dimension> data = new HashMap<String, Dimension>();
		data.put("guppy", new Dimension(15, 5));
		data.put("angelfish", new Dimension(80, 39));
		data.put("clayfish", new Dimension(160, 59));
		data.put("greenfish", new Dimension(15, 7));
		data.put("greentamale", new Dimension(15, 5));
		data.put("lobster", new Dimension(80, 60));
		data.put("orangetamale", new Dimension(15, 5));
		data.put("orca", new Dimension(150, 66));
		data.put("pirhana", new Dimension(80, 51));
		data.put("poisonfish", new Dimension(40, 15));
		data.put("redfish", new Dimension(15, 7));
		data.put("redtamale", new Dimension(15, 5));
		data.put("roundfish", new Dimension(140, 82));
		data.put("whale", new Dimension(160, 53));
		data.put("squid", new Dimension(80, 30));
		data.put("turtle", new Dimension(15, 8));
		data.put("uglyfish", new Dimension(80, 31));
		data.put("yellowfish", new Dimension(15, 7));
		data.put("browntamale", new Dimension(15, 5));
		Image[] myFace = images.get(name);
		if(myFace == null) {
			Image[] stuff = getImages(name);
			images.put(name, stuff);
			myFace = stuff;
		}
		return new RankEvilFish(name, myFace[0], myFace[1], data.get(name), board, direction, speed, rank);
	}
	
	public static Image[] getImages(String name) {
		Image[] stuff = {Manager.loadImage(""+name+".png", null), Manager.loadImage("leftfacing"+name+".png", null)};
		return stuff;
	}
	
	public void update() {
		super.update();
		xSpeed = mySpeed;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getName() {
		return name;
	}
}