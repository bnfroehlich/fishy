package bfroehlich.fishy;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Iterator;

import javax.swing.JPanel;

public class Board extends JPanel {
	
	private Image background;
	private Manager manager;
	
	public Board(Manager manager) {
		this.manager = manager;
	}
	
	public void setBackgroundImage(Image bg) {
		this.background = bg;
	}
	
	public void paint(Graphics g) {
		if(background != null) {
			g.drawImage(background, 0, 0, this);
		}
		else {
			g.setColor(Color.BLUE);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
        Iterator<Component> it = manager.getComponents().iterator();
        while (it.hasNext()) {
            Component c = it.next();
            if(c != null) {
                Graphics gtemp = g.create();
                gtemp.translate(c.getLocation().x, c.getLocation().y);
                c.paint(gtemp);
            }
        }
	}
}
