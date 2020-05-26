package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public abstract class PowerUp extends Component {

	public PowerUp(Point location, Board board) {
		super(board);
		this.x = location.x;
		this.y = location.y;
		ySpeed = -.5;
	}
}