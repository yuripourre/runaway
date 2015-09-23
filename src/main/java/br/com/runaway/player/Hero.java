package br.com.runaway.player;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.tide.action.player.ActionPlayerListener;

public class Hero extends TopViewPlayer {

	protected ImageLayer hair;
	
	public Hero(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener, "player/body_default.png");
		
		hair = new ImageLayer(x, y, "player/black_hair.png");
		hair.setAngle(angle);
	}
	
	@Override
	public void updateAngle() {
		super.updateAngle();
		hair.setAngle(angle);
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
		hair.setCoordinates(x, y);
	}
		
	@Override
	public void draw(Graphic g, int x, int y) {
		super.draw(g, x, y);
		hair.draw(g, x, y);
	}

}
