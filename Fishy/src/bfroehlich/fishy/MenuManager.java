package bfroehlich.fishy;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MenuManager extends Manager {
	
	public static final Dimension MENU_SIZE = new Dimension(1200, 800);
	
	private MenuWindow window;
	private GameManager gameManager;
	
	private Random random;
	private Image poisonFish;
	
	private HeroFish hero;
	private RankEvilFish startFish;
	private RankEvilFish rulesFish;
	private RankEvilFish exitFish;
	
	private ArrayList<Bubble> bubbles;
	private long lastBubbleTime;
	
	private SoundEngine track;
	private long musicStartTime;
	private Float musicLength;
	
	public MenuManager() {
		window = new MenuWindow("Menu", this);
		engine = window.getEngine();
		board = window.getBoard();
		gameManager = new GameManager(this);
		random = new Random();
		bubbles = new ArrayList<Bubble>();
		init();
	}
	
	private void init() {
		track = new SoundEngine();
		musicLength = track.play("elevator.wav");
		track.pause();
		track.seek(0);
		poisonFish = loadImage("poisonfish.png", new Dimension(60, 23));
		
		hero = new HeroFish(board);
		hero.setLocation(new Point(MENU_SIZE.width/2-20, MENU_SIZE.height/3));
		startFish = RankEvilFish.theSourceOfAllEvil("greenfish", board, Direction.EAST, 0, 0);
		startFish.setLocation(new Point(MENU_SIZE.width*98/600, MENU_SIZE.height*7/10));
		rulesFish = RankEvilFish.theSourceOfAllEvil("yellowfish", board, Direction.EAST, 0, 0);
		rulesFish.setLocation(new Point(MENU_SIZE.width*290/600, MENU_SIZE.height*7/10));
		exitFish = RankEvilFish.theSourceOfAllEvil("redfish", board, Direction.EAST, 0, 0);
		exitFish.setLocation(new Point(MENU_SIZE.width*493/600, MENU_SIZE.height*7/10));
		board.setBackgroundImage(loadImage("underthemenu.png", MENU_SIZE));
		board.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(Direction.wasdDirection(c) != null) {
					hero.directionPressed(Direction.wasdDirection(c));
				}
				else if(Character.toLowerCase(c) == 'p') {
					engine.toggleGoing();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if(Direction.wasdDirection(c) != null) {
					hero.directionReleased(Direction.wasdDirection(c));
				}
			}
			
			public void keyPressed(KeyEvent e) {}
		});
	}
	
	public void showMenu() {
		window.setVisible(true);
		engine.start();
	}

	public void tick(long time) {
		updateMusic(time);
		updateFish();
		updateBubbles(time);
	}
	
	private void updateFish() {
		hero.update();
		getBackInYourCage(hero);
		Rectangle heroRect = new Rectangle(hero.getLocation(), hero.getDimensions());
		Rectangle startRect = new Rectangle(startFish.getLocation(), startFish.getDimensions());
		Rectangle rulesRect = new Rectangle(rulesFish.getLocation(), rulesFish.getDimensions());
		Rectangle exitRect = new Rectangle(exitFish.getLocation(), exitFish.getDimensions());
		if(heroRect.intersects(startRect)) {
			SoundEngine.staticPlay("bite1.wav");
			engine.stop();
			hero.stop();
			hero.setLocation(new Point(MENU_SIZE.width/2-20, MENU_SIZE.height/3));
			stopMusic();
			window.setVisible(false);
			gameManager.startGame(false);
		}
		else if(heroRect.intersects(rulesRect)) {
			SoundEngine.staticPlay("bite1.wav");
			engine.stop();
			JOptionPane.showMessageDialog(window, new JLabel("Eat or be eaten. Watch out for poison fish.", new ImageIcon(poisonFish), JLabel.CENTER));
			hero.stop();
			hero.setLocation(new Point(MENU_SIZE.width/2-20, MENU_SIZE.height/3));
			engine.start();
		}
		else if(heroRect.intersects(exitRect)) {
			System.exit(0);
		}
	}
	
	private void stopMusic() {
		track.pause();
		track.seek(0);
		musicStartTime = 0;
	}
	
	private void updateMusic(long time) {
        if(time - musicStartTime > musicLength*1000 - 800) {
        	track.pause();
        	track.seek(0);
        	track.resume();
        	musicStartTime = System.currentTimeMillis();
        }
	}
	
	private void updateBubbles(long time) {
		for(Bubble bubble : bubbles) {
			bubble.update();
		}
		if(time - lastBubbleTime > 2000) {
			if(random.nextInt(3) == 0) {
				SoundEngine.staticPlay("mouseOver.wav");
				Point loc = hero.getLocation();
				if(!hero.isLeftFacing()) {
					loc = new Point(loc.x+hero.getDimensions().width, loc.y);
				}
				Bubble bubble = new Bubble(loc, board);
				bubbles.add(bubble);
				lastBubbleTime = time;
			}
		}
	}
	
	public boolean getBackInYourCage(Fish fish) {
		Point fishLoc = fish.getLocation();
		Dimension fishSize = fish.getDimensions();
		if(fish instanceof HeroFish) {
			if(fishLoc.x > MENU_SIZE.width - fishSize.width/2) {
				fish.setLocation(new Point(-fishSize.width/2, fishLoc.y));
			}
			else if(fishLoc.x < -fishSize.width/2) {
				fish.setLocation(new Point(MENU_SIZE.width - fishSize.width/2, fishLoc.y));
			}
			if(fishLoc.y > MENU_SIZE.height - fishSize.height) {
				fish.setLocation(new Point(fishLoc.x, MENU_SIZE.height - fishSize.height));
				fish.setYSpeed(0);
			}
			else if(fishLoc.y < 0) {
				fish.setLocation(new Point(fishLoc.x, 0));
				fish.setYSpeed(0);
			}
			return false;
		}
		else {
			if(fishLoc.x > MENU_SIZE.width || fishLoc.x < -fishSize.width) {
				return true;
			}
			return false;
		}
	}

	public ArrayList<Component> getComponents() {
		ArrayList<Component> components = new ArrayList<Component>();
		components.add(hero);
		components.add(startFish);
		components.add(rulesFish);
		components.add(exitFish);
		components.addAll(bubbles);
		return components;
	}

}
