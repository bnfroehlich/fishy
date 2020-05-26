package bfroehlich.fishy;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class GameManager extends Manager {
	
	public static final Dimension BOARD_SIZE = new Dimension(1200, 750);
	public static final int tummySize = 10;
	
	private static final int[][] frequencies = {
			{0, 0, 0, 0, 0, 0, 0, 1, 1, 1},
			{0, 0, 0, 0, 1, 1, 1, 1, 2, 2},
			{3, 3, 3, 1, 1, 1, 1, 2, 2, 2}
	};
	private static final String[][] names = {
			{"orangetamale", "squid", "clayfish", "poisonfish"},
			{"redtamale", "uglyfish", "roundfish", "poisonfish"},
			{"yellowfish", "angelfish", "orca", "poisonfish"},
			{"browntamale", "pirhana", "whale", "poisonfish"}
	};
	
	private boolean isRankMode;
	
	private MenuManager menuManager;
	private Random random;
	private JFrame dummy;
	
	private int score;
	private int lives;
	private int level;
	
	private SoundEngine track1;
	private SoundEngine track2;
	private long currentMusicStartTime;
	private float currentMusicLength;
	private int currentTrackNumber;
	
	private long lastBubbleTime;
	private long lastPowerUpTime;
	private long lastYummyTime;
	private Yummy yummy;

	private ArrayList<Fish> fishes;
	private ArrayList<Component> junk;

	public GameManager(MenuManager menuManager) {
		this.menuManager = menuManager;
		window = new GameWindow("Fishy", this);
		engine = window.getEngine();
		board = window.getBoard();
		random = new Random();
		
		junk = new ArrayList<Component>();
		fishes = new ArrayList<Fish>();
		HeroFish hero = new HeroFish(board);
		hero.setLocation(new Point(BOARD_SIZE.width/2, BOARD_SIZE.height/2));
		fishes.add(hero);
		
		dummy = new JFrame();
		
		init();
	}
	
	private void init() {
		track1 = new SoundEngine();
		track1.play("water.wav");
		track1.pause();
		track1.seek(0);
		track2 = new SoundEngine();
		track2.play("water2.wav");
		track2.pause();
		track2.seek(0);
		
		board.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if(Direction.wasdDirection(c) != null) {
					iNeedAHero().directionPressed(Direction.wasdDirection(c));
				}
				else if(Character.toLowerCase(c) == 'p') {
					engine.toggleGoing();
				}
			}
			
			public void keyReleased(KeyEvent e) {
				char c = e.getKeyChar();
				if(Direction.wasdDirection(c) != null) {
					iNeedAHero().directionReleased(Direction.wasdDirection(c));
				}
			}
			
			public void keyPressed(KeyEvent e) {}
		});
	}
	
	public void startGame(boolean makeEnemiesByRank) {
		this.isRankMode = makeEnemiesByRank;
		board.setBackgroundImage(loadImage("underthesea.jpg", BOARD_SIZE));
		JOptionPane.showMessageDialog(dummy, "Get ready.");
		lives = 0;
		level = 1;
		score = 0;
		HeroFish hero = iNeedAHero();
		hero.setLevel(0);
		reset(true);
		window.setVisible(true);
		engine.start();
		makeYummy(System.currentTimeMillis());
	}
	
	public void gameOver() {
		engine.stop();
		stopMusic();
		SoundEngine.staticPlay("GameOver.wav");
		JOptionPane.showMessageDialog(dummy, "Game over.");
		window.setVisible(false);
		menuManager.showMenu();
	}
    
	public void youWin() {
		engine.stop();
		stopMusic();
		JOptionPane.showMessageDialog(dummy, "You win.");
		window.setVisible(false);
		menuManager.showMenu();
	}
	
	public boolean isRankMode() {
		return isRankMode;
	}
	
    public int getScore() {
    	return score;
    }
    
    public int getLivesLeft() {
    	return lives;
    }
    
    public int getLevel() {
    	return level;
    }
    
    public ArrayList<Component> getJunk() {
    	return junk;
    }
	
	public ArrayList<Fish> getFishes() {
		return fishes;
	}
	
	public ArrayList<RankEvilFish> getEvilFishes() {
		ArrayList<RankEvilFish> evilFishes = new ArrayList<RankEvilFish>();
		for(Fish fish : getFishes()) {
			if(fish instanceof RankEvilFish) {
				evilFishes.add((RankEvilFish) fish);
			}
		}
		return evilFishes;
	}
	
	public HeroFish iNeedAHero() {
		for(Fish fish : fishes) {
			if(fish instanceof HeroFish) {
				return (HeroFish) fish;
			}
		}
		return null;
	}
	
	public ArrayList<Component> getComponents() {
		ArrayList<Component> components = new ArrayList<Component>();
		components.addAll(fishes);
		components.addAll(junk);
		components.add(yummy);
		return components;
	}
	
	public boolean getBackInYourCage(Fish fish) {
		Point fishLoc = fish.getLocation();
		Dimension fishSize = fish.getDimensions();
		if(fish instanceof HeroFish) {
			if(fishLoc.x > BOARD_SIZE.width - fishSize.width/2) {
				fish.setLocation(new Point(-fishSize.width/2, fishLoc.y));
			}
			else if(fishLoc.x < -fishSize.width/2) {
				fish.setLocation(new Point(BOARD_SIZE.width - fishSize.width/2, fishLoc.y));
			}
			if(fishLoc.y >= BOARD_SIZE.height - fishSize.height) {
				fish.setLocation(new Point(fishLoc.x, BOARD_SIZE.height - fishSize.height - 1));
				fish.setYSpeed(0);
			}
			else if(fishLoc.y < 0) {
				fish.setLocation(new Point(fishLoc.x, 0));
				fish.setYSpeed(0);
			}
			return false;
		}
		else {
			if(fishLoc.x > BOARD_SIZE.width || fishLoc.x < -fishSize.width) {
				return true;
			}
			return false;
		}
	}
	
	public void spawnNewEnemy() {
		double speed = random.nextDouble()*(1.5) + .5;
		int side = random.nextInt(2);
		int startingX = 0;
		Direction direction;
		if(side == 0) {
			direction = Direction.EAST;
		}
		else {
			direction = Direction.WEST;
		}
		
		Fish enemy = null;
		if(isRankMode) {
			int size = random.nextInt(10);
			int enemyRank = frequencies[iNeedAHero().getCurrentRank()][size];
			enemy = RankEvilFish.theSourceOfAllEvil(names[level-1][enemyRank], board, direction, speed, enemyRank);
			
		}
		else {
			double heroWidth = iNeedAHero().getDimensions().width;
			double minWidth = 15;
			double maxWidth = heroWidth*(2.0-0.8*(2.0*heroWidth/((double) BOARD_SIZE.width))); //scales down from 2x to 1.2x when x = boardwidth/2
			double proportionEdible = .25 + .4*heroWidth*4.0/((double) BOARD_SIZE.width); //scales up from 0.3 to 0.7 when x = boardwidth/4
			if(proportionEdible > 0.7) {
				proportionEdible = 0.7;
			}
			boolean edible = Math.random() < proportionEdible;
			double width = 0;
			if(edible) {
				width = Math.random()*(heroWidth*.8-minWidth) + minWidth;
			}
			else {
				width = Math.random()*(maxWidth - heroWidth*1.2) + heroWidth*1.2;
			}
			enemy = EvilFish.theSourceOfAllEvil("", board, direction, speed, (int) width);	
		}
		
		if(side == 0) {
			startingX = -enemy.getDimensions().width + 1;
		}
		else {
			startingX = BOARD_SIZE.width  - 1;
		}
		int startingY = random.nextInt(BOARD_SIZE.height-enemy.getDimensions().height-2)+1;
		if(enemy.isBottomDweller()) {
			startingY = BOARD_SIZE.height-enemy.getDimensions().height;
		}
		enemy.setLocation(new Point(startingX, startingY));
		fishes.add(enemy);
	}
	
	public void reset(boolean fullReset) {
		SoundEngine.staticPlay("playerSpawn.wav");
		HeroFish hero = iNeedAHero();
		hero.stop();
		hero.setLocation(new Point(BOARD_SIZE.width/2, 0));
		hero.setYSpeed(3);
		hero.setHasShield(false);
		hero.setInvincibilityMillis(0);
		yummy = null;
		if(fullReset) {
			hero.resetRank();
		}
		else {
			hero.setTummy(0);
		}
		clearEnemies();
		clearJunk();
	}
	
	public void clearEnemies() {
		HeroFish hero = iNeedAHero();
		fishes.clear();
		fishes.add(hero);
	}
	
	public void clearJunk() {
		junk.clear();
	}
	
	public void levelUp() {
		level++;
		if(level == 5) {
			youWin();
			return;
		}
		if(level == 3) {
			board.setBackgroundImage(loadImage("underthesun.jpg", BOARD_SIZE));
		}
		if(level == 3) {
			HeroFish hero = iNeedAHero();
			hero.setLevel(hero.getLevel()+1);
		}
		engine.stop();
		window.setVisible(false);
		JOptionPane.showMessageDialog(dummy, "Level " + getLevel());
		window.setVisible(true);
		reset(true);
		engine.start();
	}
	
	public void death() {
		lives--;
		if(lives < 0) {
			gameOver();
		}
		else {
			engine.stop();
			window.setVisible(false);
			JOptionPane.showMessageDialog(dummy, "You died.");
			window.setVisible(true);
			reset(false);
			engine.start();
		}
	}
	
	public void tick(long time) {
		try {
			updateMusic(time);
			updateJunk(time);
    		updateFishes();
    		eatThings();
    		window.repaint();
        } catch (ResetException e) {
        	if(e.getReason().equals("grow")) {
        		clearEnemies();
        		makeYummy(time);
        	}
        	else if(e.getReason().equals("death")) {
        		death();
        		makeYummy(time);
        	}
        	else if(e.getReason().equals("level up")) {
				levelUp();
				makeYummy(time);
        	}
        	else if(e.getReason().equals("you win")) {
        		youWin();
        	}
        }
	}
	
	private void makeYummy(long time) {
		try {
			if(isRankMode) {
				yummy = new Yummy(board, iNeedAHero(), RankEvilFish.getImages(names[level-1][iNeedAHero().getCurrentRank()])[0]);
			}
			else {
				String name = EvilFish.getEnemyBySize(iNeedAHero().getDimensions().width, true);
				yummy = new Yummy(board, iNeedAHero(), EvilFish.getImages(name)[0]);
			}
			lastYummyTime = time;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			//must have leveled up or something
		}
	}
	
	private void updateMusic(long time) {
        if(time - currentMusicStartTime > currentMusicLength*1000 - 800) {
        	if(currentTrackNumber == 1) {
        		track2.resume();
        		track1.pause();
        		track1.seek(0);
        		currentTrackNumber = 2;
        		currentMusicLength = track2.getDuration();
        	}
        	else {
        		track1.resume();
        		track2.pause();
        		track2.seek(0);
        		currentTrackNumber = 1;
        		currentMusicLength = track1.getDuration();
        	}
        	currentMusicStartTime = System.currentTimeMillis();
        }
	}
	
	private void stopMusic() {
		if(currentTrackNumber == 1) {
			track1.pause();
			track1.seek(0);
			currentTrackNumber = 2;
    		currentMusicLength = track2.getDuration();
		}
		else {
			track2.pause();
			track2.seek(0);
			currentTrackNumber = 2;
    		currentMusicLength = track1.getDuration();
		}
		currentMusicStartTime = 0;
	}
	
	private void updateJunk(long time) {
		for(Component c : junk) {
			c.update();
		}
		if(time - lastBubbleTime > 2000) {
			if(random.nextInt(3) == 0) {
				SoundEngine.staticPlay("mouseOver.wav");
				HeroFish hero = iNeedAHero();
				Point loc = hero.getLocation();
				if(!hero.isLeftFacing()) {
					loc = new Point(loc.x+hero.getDimensions().width, loc.y);
				}
				Bubble bubble = new Bubble(loc, board);
				junk.add(bubble);
				lastBubbleTime = time;
			}
		}
    	if(time - lastPowerUpTime > 5000) {
    		if(random.nextInt(2) == 0) {
    			SoundEngine.staticPlay("mouseDown.wav");
    			ExtraLife extra = new ExtraLife(new Point(random.nextInt(BOARD_SIZE.width-20), BOARD_SIZE.height-20), board);
    			junk.add(extra);
    			lastPowerUpTime = time;
    		}
    		else {
    			SoundEngine.staticPlay("mouseDown.wav");
    			Shield shield = new Shield(new Point(random.nextInt(BOARD_SIZE.width-20), BOARD_SIZE.height-20), board);
    			junk.add(shield);
    			lastPowerUpTime = time;
    		}
    	}
    	if(time - lastYummyTime > 5000) {
    		yummy = null;
    	}
	}
    
    private void updateFishes() throws ResetException {
    	Iterator<Fish> fit = getFishes().iterator();
    	while(fit.hasNext()) {
        	Fish fish = fit.next();
    		fish.update();
    		if(fish instanceof HeroFish) {
    			getBackInYourCage(fish);
    		}
    		else {
    			if(getBackInYourCage(fish)) {
	    			fit.remove();
	    		}
    		}
    	}
    	if(getFishes().size() < 10) {
    		spawnNewEnemy();
    	}
    }
    
    private void eatThings() throws ResetException {
    	HeroFish hero = iNeedAHero();
    	Rectangle heroRect = new Rectangle(hero.getLocation(), hero.getDimensions());
    	Iterator<Fish> fit = getFishes().iterator();
    	while(fit.hasNext()) {
    		Fish fish = fit.next();
    		RankEvilFish evilFish;
    		if(fish instanceof HeroFish) {
    			continue;	
    		}
			Rectangle rect = new Rectangle(fish.getLocation(), fish.getDimensions());
			if(heroRect.intersects(rect)) {
				if(heroCanEat(hero, fish)) {
					fit.remove();
					if(isRankMode) {
		    			RankEvilFish rankEvilFish = (RankEvilFish) fish;
						SoundEngine.staticPlay("bite" + (rankEvilFish.getRank()+1) + ".wav");
						score += rankEvilFish.getRank()+1;
						if(rankEvilFish.getRank() == 0) {
							hero.youAteAFish(1);
						}
						else {
							hero.youAteAFish(2);
						}
						if(hero.getTummy() >= tummySize) {
							SoundEngine.staticPlay("playerGrow.wav");
							if(hero.rankUp()) {
								throw new ResetException("level up");
							}
							else {
								throw new ResetException("grow");
							}
						}
					}
					else {
						SoundEngine.staticPlay("bite1.wav");
						//SoundEngine.staticPlay("playerGrow.wav");
						hero.incrementGirth();
						score = hero.getDimensions().width;
						makeYummy(System.currentTimeMillis());
						if(hero.getDimensions().width > BOARD_SIZE.width/2) {
							throw new ResetException("you win");
						}
					}
				}
				else {
					if(hero.hasShield()) {
						SoundEngine.staticPlay("playerPoison.wav");
						hero.setHasShield(false);
						hero.setInvincibilityMillis(2000);
					}
					else if(!hero.isInvincible()) {
						SoundEngine.staticPlay("playerDie.wav");
						throw new ResetException("death");
					}
				}
			}
    	}
    	Iterator<Component> jit = getJunk().iterator();
    	while(jit.hasNext()) {
    		Component j = jit.next();
			Rectangle rect = new Rectangle(j.getLocation(), j.getDimensions());
			if(heroRect.intersects(rect)) {
				if(j instanceof ExtraLife) {
					SoundEngine.staticPlay("freeLife.wav");
					lives++;
					jit.remove();
				}
				else if(j instanceof Shield) {
					SoundEngine.staticPlay("starPickup.wav");
					iNeedAHero().setHasShield(true);
					jit.remove();
				}
			}
    	}
    }
    
    private boolean heroCanEat(HeroFish hero, Fish enemy) {
    	if(enemy instanceof RankEvilFish && ((RankEvilFish) enemy).getName().equals("poisonfish")) {
    		return false;
    	}
    	return enemy.getDimensions().width < hero.getDimensions().width;
    }
}
