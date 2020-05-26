package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Bubble extends Component {
	
	private static Image bubble;

	public Bubble(Point location, Board board) {
		super(board);
		this.x = location.x;
		this.y = location.y;
		ySpeed = -.5;
		dimensions = new Dimension(5, 5);
		if(bubble == null) {
			bubble = Manager.loadImage("bubble.png", null);
		}
	}
	
	protected Image getImage() {
		return bubble;
	}
}