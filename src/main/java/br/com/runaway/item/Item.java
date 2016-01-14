package br.com.runaway.item;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.runaway.player.Hero;

public class Item {
	
	protected ImageLayer layer;
	
	public Item(int x, int y, String path) {
		super();
		layer = new ImageLayer(x, y, path);
	}

	public void draw(Graphic g, int ox, int oy) {
		layer.draw(g, ox, oy);
	}
	
	public boolean colide(int px, int py) {
		return layer.colideRectPoint(px, py);
	}

	public void act(Hero player, long now) {
	
	}
	
}
