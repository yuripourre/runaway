package br.com.runaway.player;

import java.awt.Color;

import br.com.etyllica.animation.listener.OnAnimationFinishListener;
import br.com.etyllica.core.Drawable;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.graphics.SVGColor;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.linear.PointInt2D;
import br.com.runaway.animation.HitAnimation;
import br.com.tide.action.player.ActionPlayer;
import br.com.tide.action.player.ActionPlayerListener;

public abstract class TopViewPlayer extends ActionPlayer implements Drawable, OnAnimationFinishListener {

	protected AnimatedLayer bodyLayer;

	private Layer hitbox;

	private PointInt2D center;

	private static final int HITBOX_WIDTH = 28;

	private Color color = SVGColor.DARK_GOLDENROD;

	private int currentLife = 3;

	private int totalLife = 3;

	private boolean invincibility = false;
	
	private HitAnimation invincible;
	
	public TopViewPlayer(int x, int y, ActionPlayerListener<TopViewPlayer> listener, String bodyPart) {
		super(x, y, listener);

		this.currentSpeed = 3;

		bodyLayer = new AnimatedLayer(x, y, 66, 42, bodyPart);
		bodyLayer.setAngle(angle);
		bodyLayer.setSpeed(100);
		bodyLayer.setFrames(6);
		
		hitbox = new Layer();
		center = new PointInt2D();
		//hitbox.centralize(layer);
		centralizeHitbox();
		
		invincible = new HitAnimation(this);
	}

	public void update(long now) {
		super.update(now);

		if(isWalking()) {
			bodyLayer.animate(now);
			updatePosition();
			centralizeHitbox();
		}

		if(isTurning()) {
			updateAngle();
		}

	}

	protected void updateAngle() {
		bodyLayer.setAngle(angle);
	}

	protected void updatePosition() {
		bodyLayer.setCoordinates(x, y);
	}

	private void centralizeHitbox() {

		int cx = bodyLayer.getX()+bodyLayer.getTileW()/2;
		int cy = bodyLayer.getY()+bodyLayer.getTileH()/2;

		center.setLocation(cx, cy);

		hitbox.setBounds(cx-HITBOX_WIDTH/2, cy-HITBOX_WIDTH/2, HITBOX_WIDTH, HITBOX_WIDTH);
	}

	public Layer getHitbox() {
		return hitbox;
	}

	@Override
	public void draw(Graphic g) {

		//drawHitBox(g);

		bodyLayer.draw(g);
	}

	private void drawHitBox(Graphic g) {

		//Draw HitBox
		g.setColor(color);
		g.setAlpha(80);
		g.fillRect(hitbox);
		g.resetOpacity();
	}

	public AnimatedLayer getBodyLayer() {
		return bodyLayer;
	}
	
	public PointInt2D getCenter() {
		return center;
	}

	public boolean isInvincibility() {
		return invincibility;
	}

	public void setInvincibility(boolean invincibility) {
		this.invincibility = invincibility;
	}

	public void loseLife(long now) {
		currentLife--;
		invincible.startAnimation(now);
		invincibility = true;
	}

	public int getCurrentLife() {
		return currentLife;
	}

	public int getTotalLife() {
		return totalLife;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void resetColor() {
		this.color = SVGColor.DARK_GOLDENROD;
	}

	@Override
	public void onAnimationFinish(long now) {
		setInvincibility(false);
	}

}
