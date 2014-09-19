package br.com.runaway.player;

import br.com.etyllica.core.Drawable;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.graphics.SVGColor;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.Layer;
import br.com.tide.action.player.ActionPlayer;
import br.com.tide.action.player.ActionPlayerListener;

public class TopViewPlayer extends ActionPlayer implements Drawable, ActionPlayerListener {

	private AnimatedLayer layer;
	
	private Layer hitbox;
		
	private static final int hitboxWidth = 20;

	public TopViewPlayer(int x, int y) {
		super(x, y);
		
		this.listener = this;
		
		this.currentSpeed = 3;
		
		layer = new AnimatedLayer(x, y, 66, 42, "player/player_walk.png");
		layer.setAngle(angle);
		layer.setSpeed(100);
		layer.setFrames(6);

		hitbox = new Layer(layer.getX()+layer.getTileW()/2-hitboxWidth/2, y, hitboxWidth, layer.getTileH());				
	}

	public void update(long now) {
		super.update(now);

		if(isWalking()) {
			layer.animate(now);
			layer.setCoordinates(x, y);
			
			hitbox.setBounds(layer.getX()+layer.getTileW()/2-hitboxWidth/2, y, hitboxWidth, layer.getTileH());
		}
		
		if(isTurning()) {
			layer.setAngle(angle);
			hitbox.setAngle(angle);
		}

	}

	@Override
	public void onTurnLeft() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTurnRight() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWalkForward() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWalkBackward() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnLeft() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnRight() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopWalkForward() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopWalkBackward() {
		// TODO Auto-generated method stub
	}
	
	public Layer getHitbox() {
		return hitbox;
	}

	@Override
	public void draw(Graphic g) {
				
		//Draw HitBox
		g.setColor(SVGColor.DARK_GOLDENROD);
		g.setAlpha(80);
		g.fillRect(hitbox);
		g.resetOpacity();
		
		layer.draw(g);
	}
	
	public AnimatedLayer getLayer() {
		return layer;
	}

}
