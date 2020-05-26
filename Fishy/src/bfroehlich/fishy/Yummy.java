package bfroehlich.fishy;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public class Yummy extends Component {
	
	private static Image yummy;

	private HeroFish hero;
	private Image food;
	
	public Yummy(Board board, HeroFish hero, Image food) {
		super(board);
		this.hero = hero;
		dimensions = new Dimension(120, 80);
		if(yummy == null) {
			yummy = Manager.loadImage("yummy.png", null);
		}
		this.food = food.getScaledInstance(80, 40, Image.SCALE_DEFAULT);
	}
	
	public Point getLocation() {
		return new Point(hero.getLocation().x+hero.getDimensions().width, hero.getLocation().y-80);
	}
	
	protected Image getImage() {
		return yummy;
	}
	
	public void paint(Graphics g) {
		if(yummy != null) {
			g.drawImage(yummy, 0, 0, 120, 80, board);
		}
		else {
			g.fillRect(0, 0, 90, 60);
		}
		if(food != null)  {
			g.drawImage(food, 20, 11, 80, 40, board);
		}
	}
}