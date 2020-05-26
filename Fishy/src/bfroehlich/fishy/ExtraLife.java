package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class ExtraLife extends PowerUp {
	
	private static Image extraLife;
	
	public ExtraLife(Point location, Board board) {
		super(location, board);
		dimensions = new Dimension(20, 20);
		if(extraLife == null) {
			extraLife = Manager.loadImage("plus.png", new Dimension(5, 5));
		}
	}
	
	public Image getImage() {
		return extraLife;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillOval(0, 0, dimensions.width, dimensions.height);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times New Roman", Font.BOLD, 16));
		g.drawString("+1", 2, 15);
	}
}
