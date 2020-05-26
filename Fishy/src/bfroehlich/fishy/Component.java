package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public abstract class Component {

	protected Board board;

	protected double x;
	protected double y;
	protected double xSpeed;
	protected double ySpeed;
	
	protected Dimension dimensions;
	
	public Component(Board board) {
		this.board = board;
		dimensions = new Dimension(0, 0);
	}
	
	public double getXSpeed() {
		return xSpeed;
	}
	
	public double getYSpeed() {
		return ySpeed;
	}
	
	public void setXSpeed(double xSpeed) {
		this.xSpeed = xSpeed;
	}
	
	public void setYSpeed(double ySpeed) {
		this.ySpeed = ySpeed;
	}
	
	public Point getLocation() {
		return new Point((int) x, (int) y);
	}
	
	public void setLocation(Point location) {
		this.x = location.x;
		this.y = location.y;
	}
	
	public Dimension getDimensions() {
		return dimensions;
	}
	
	public void setDimensions(Dimension size) {
		this.dimensions = size;
	}
	
	public void update() {
		x += xSpeed;
		y += ySpeed;
	}
	
	protected abstract Image getImage();
	
	public void paint(Graphics g) {
		Image anImage = getImage();
		if(anImage != null) {
			//Graphics2D g2 = (Graphics2D) g;
			//g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g.drawImage(anImage, 0, 0, dimensions.width, dimensions.height, board);
		}
		else {
			g.setColor(Color.PINK);
			g.fillRect(0, 0, dimensions.width, dimensions.height);
		}
	}
}
