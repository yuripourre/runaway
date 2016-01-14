package br.com.runaway.player;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;
import br.com.tide.action.player.ActionPlayerListener;

public class Hero extends TopViewPlayer {

	protected ImageLayer head;
	
	public Hero(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener, "player/body_default.png");
		
		head = new ImageLayer(x, y, "player/black_hair.png");
		head.setAngle(angle);
	}
	
	@Override
	public void updateAngle() {
		super.updateAngle();
		head.setAngle(angle);
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
		if(head!=null) {
			head.setCoordinates(x, y);	
		}
	}
		
	@Override
	public void draw(Graphic g, int x, int y) {
		super.draw(g, x, y);
		head.draw(g, x, y);
	}

}
