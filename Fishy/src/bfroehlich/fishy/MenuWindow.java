package bfroehlich.fishy;

import javax.swing.JFrame;

public class MenuWindow extends JFrame {
	
	private MenuManager manager;
	private Engine engine;
	private Board board;

	public MenuWindow(String name, MenuManager manager) {
		super(name);
		this.manager = manager;
		this.board = new Board(manager);
		board.setPreferredSize(MenuManager.MENU_SIZE);
		add(board);
		engine = new Engine(board, manager);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Engine getEngine() {
		return engine;
	}
}
