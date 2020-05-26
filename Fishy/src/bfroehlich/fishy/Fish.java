package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;

public abstract class Fish extends Component {

	protected Board board;
	protected Image image;
	protected Image leftFacingImage;
	
	public Fish(Image image, Image leftFacingImage, Dimension dimensions, Board board) {
		super(board);
		this.image = image;
		this.leftFacingImage = leftFacingImage;
		this.dimensions = dimensions;
	}
	
	public boolean isBottomDweller() {
		return false;
	}
	
	public Image getImage() {
		if(!isLeftFacing()) {
			return image;
		}
		return leftFacingImage;
	}
	
	public boolean isLeftFacing() {
		return xSpeed < 0;
	}
	
	public void update() {
		super.update();
		xSpeed -= xSpeed*.01;
		ySpeed -= ySpeed*.01;
	}
	
	
}
