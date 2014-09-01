package br.com.runaway;

import java.awt.Color;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.AnimatedLayer;

public class GameApplication extends Application {

	private AnimatedLayer player;
	
	public GameApplication(int w, int h) {
		super(w, h);
	}
	
	private double turnSpeed = 5;
	private double walkSpeed = 3;
	private double backWalkSpeed = -1.5;
	
	private boolean rightArrow = false;
	private boolean leftArrow = false;
	private boolean upArrow = false;
	private boolean downArrow = false;
		
	@Override
	public void load() {
		
		player = new AnimatedLayer(20, 60, 66, 42, "player/player_walk.png");
		player.setFrames(6);
		player.setSpeed(100);
		
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
		if(upArrow) {
			player.animate(now);
			
			//go forward
			player.setX((int)(player.getX() + Math.sin(Math.toRadians(player.getAngle()+90)) * walkSpeed));		
			
			player.setY((int)(player.getY() - Math.cos(Math.toRadians(player.getAngle()+90)) * walkSpeed));
			
		} else if(downArrow) {
			
			player.animate(now);
			
			//go backward
			player.setX((int)(player.getX() + Math.sin(Math.toRadians(player.getAngle()+90)) * backWalkSpeed));		
			
			player.setY((int)(player.getY() - Math.cos(Math.toRadians(player.getAngle()+90)) * backWalkSpeed));
			
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
		
		if(event.isKeyDown(KeyEvent.TSK_UP_ARROW)) {
			upArrow = true;
		} else if(event.isKeyUp(KeyEvent.TSK_UP_ARROW)) {
			upArrow = false;
		}
		
		if(event.isKeyDown(KeyEvent.TSK_DOWN_ARROW)) {
			downArrow = true;
		} else if(event.isKeyUp(KeyEvent.TSK_DOWN_ARROW)) {
			downArrow = false;
		}
		
		// TODO Auto-generated method stub
		return null;
	}


}
