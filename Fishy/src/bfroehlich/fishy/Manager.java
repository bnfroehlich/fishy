package bfroehlich.fishy;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ArrayList;


public abstract class Manager {
	
	protected GameWindow window;
	protected Engine engine;
	protected Board board;
	
	public static Image loadImage(String path, Dimension size) {
		if(path == null) {
			return null;
		}
		URL url = Manager.class.getResource("/" + path);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(url);
        if(size != null) {
        	image = image.getScaledInstance(size.width, size.height, Image.SCALE_DEFAULT);
        }
        return image;
	}
	
	public abstract void tick(long time);
	
	public abstract ArrayList<Component> getComponents();
}
