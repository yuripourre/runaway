package br.com.runaway.player;

import br.com.etyllica.core.Drawable;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.graphics.SVGColor;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.linear.PointInt2D;
import br.com.tide.action.player.ActionPlayer;
import br.com.tide.action.player.ActionPlayerListener;

public class TopViewPlayer extends ActionPlayer implements Drawable, ActionPlayerListener {

	private AnimatedLayer layer;
	
	private Layer hitbox;
	
	private PointInt2D centerPoint;
			
	private static final int HITBOX_WIDTH = 28;

	public TopViewPlayer(int x, int y) {
		super(x, y);
		
		this.listener = this;
		
		this.currentSpeed = 3;
		
		layer = new AnimatedLayer(x, y, 66, 42, "player/player_walk.png");
		layer.setAngle(angle);
		layer.setSpeed(100);
		layer.setFrames(6);

		hitbox = new Layer();
		centerPoint = new PointInt2D();
		//hitbox.centralize(layer);
		centralizeHitbox();
	}

	public void update(long now) {
		super.update(now);

		if(isWalking()) {
			layer.animate(now);
			layer.setCoordinates(x, y);
		
			centralizeHitbox();
		}
		
		if(isTurning()) {
			layer.setAngle(angle);
		}

	}
	
	private void centralizeHitbox() {
		
		int cx = layer.getX()+layer.getTileW()/2-HITBOX_WIDTH/2;
		int cy = layer.getY()+layer.getTileH()/2-HITBOX_WIDTH/2;
		
		centerPoint.setLocation(cx, cy);
		
		hitbox.setBounds(cx, cy, HITBOX_WIDTH, HITBOX_WIDTH);
	}

	@Override
	public void onTurnLeft(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTurnRight(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWalkForward(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onWalkBackward(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnLeft(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnRight(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopWalkForward(ActionPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopWalkBackward(ActionPlayer player) {
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
