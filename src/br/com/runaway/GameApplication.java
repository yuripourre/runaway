package br.com.runaway;

import java.awt.Color;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;
import br.com.etyllica.layer.ImageLayer;

public class GameApplication extends Application {

	private AnimatedLayer player;
	
	public GameApplication(int w, int h) {
		super(w, h);
	}
	
	private int turnSpeed = 5;
	private boolean rightArrow = false;
	private boolean leftArrow = false;
	
	
	@Override
	public void load() {
		
		player = new AnimatedLayer(0, 60, 32, 48, "player/player_walk_strip.png");
		
		updateAtFixedRate(30);
		
		loading = 100;
	}
	
	public void timeUpdate(long now) {
		if(rightArrow) {
			player.setOffsetAngle(turnSpeed);
		}
		if(leftArrow) {
			player.setOffsetAngle(-turnSpeed);
		}
	}

	@Override
	public void draw(Graphic g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, w/2, h);
		
		player.draw(g);
	}
	
	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event) {
		
		if(event.isKeyDown(KeyEvent.TSK_RIGHT_ARROW)) {
			rightArrow = true;
		} else if(event.isKeyUp(KeyEvent.TSK_RIGHT_ARROW)) {
			rightArrow = false;
		}
		
		if(event.isKeyDown(KeyEvent.TSK_LEFT_ARROW)) {
			leftArrow = true;
		}else if(event.isKeyUp(KeyEvent.TSK_LEFT_ARROW)) {
			leftArrow = false;
		}
		
		// TODO Auto-generated method stub
		return null;
	}


}
