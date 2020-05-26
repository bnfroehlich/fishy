package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Shield extends PowerUp {
	
	public Shield(Point location, Board board) {
		super(location, board);
		dimensions = new Dimension(20, 20);
	}
	
	protected Image getImage() {
		return null;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawOval(0, 0, dimensions.width, dimensions.height);
	}

}
