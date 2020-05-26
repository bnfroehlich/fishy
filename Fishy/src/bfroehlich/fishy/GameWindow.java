package bfroehlich.fishy;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameWindow extends JFrame {
	
	private static final int MARGIN = 25;

	private GameManager manager;
	private Board board;
	private Engine engine;

	public GameWindow(String name, GameManager manager) {
		super(name);
		this.manager = manager;
		this.board = new Board(manager);
		engine = new Engine(board, manager);
		init();
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void init() {
		setLayout(new BorderLayout());
		JPanel center = new JPanel();
		center.setLayout(new BorderLayout());
		center.add(board, BorderLayout.CENTER);
		JPanel leftMargin = new JPanel() {
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		leftMargin.setPreferredSize(new Dimension(MARGIN, GameManager.BOARD_SIZE.height));
		center.add(leftMargin, BorderLayout.WEST);
		JPanel rightMargin = new JPanel() {
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		rightMargin.setPreferredSize(new Dimension(MARGIN, GameManager.BOARD_SIZE.height));
		center.add(rightMargin, BorderLayout.EAST);
		add(center, BorderLayout.CENTER);
		
		JPanel score = new JPanel() {
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
				g.drawString("Score: " + manager.getScore(), MARGIN, 15);
				if(manager.isRankMode()) {
					g.translate(100, 5);
					HeroFish hero = manager.iNeedAHero();
					g.setColor(Color.RED);
					g.drawRect(0, 0, 300, 15);
					g.fillRect(0, 0, 100*hero.getCurrentRank()+100, 15);
					g.setColor(Color.GREEN);
					g.fillRect(0, 0, 100*hero.getCurrentRank()+100*hero.getTummy()/GameManager.tummySize, 15);
				}
			}
		};
		score.setPreferredSize(new Dimension(GameManager.BOARD_SIZE.width + MARGIN*2, MARGIN));
		add(score, BorderLayout.NORTH);
		JPanel lives = new JPanel() {
			public void paint(Graphics g) {
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, getWidth(), getHeight());
				g.setColor(Color.WHITE);
				g.setFont(new Font("Times New Roman", Font.PLAIN, 16));
				g.drawString("Lives: " + manager.getLivesLeft(), MARGIN, 15);
			}
		};
		lives.setPreferredSize(new Dimension(GameManager.BOARD_SIZE.width + MARGIN*2, MARGIN));
		add(lives, BorderLayout.SOUTH);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Engine getEngine() {
		return engine;
	}
}