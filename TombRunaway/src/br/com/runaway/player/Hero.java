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
	protected void updateAngle() {
		super.updateAngle();
		hair.setAngle(angle);
	}

	@Override
	protected void updatePosition() {
		super.updatePosition();
		hair.setCoordinates(x, y);
	}
	
	@Override
	public void draw(Graphic g) {
		super.draw(g);
		hair.draw(g);
	}

}
