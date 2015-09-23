package br.com.runaway.player;

import br.com.etyllica.core.graphics.Graphic;
import br.com.tide.action.player.ActionPlayerListener;

public class Monster extends TopViewPlayer {

	private String name = "Name";
	
	public Monster(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener, "monster/zombie_walk.png");
	}

	@Override
	public void draw(Graphic g, int x, int y) {
		super.draw(g, x, y);
		
		g.drawShadow(this.x+x, this.y+y, name);
	}
	
}
