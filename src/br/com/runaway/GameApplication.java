package br.com.runaway;

import java.awt.Color;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.runaway.player.TopViewPlayer;
import br.com.tide.input.controller.Controller;
import br.com.tide.input.controller.FirstPlayerController;

public class GameApplication extends Application {
	
	private TopViewPlayer player;
	
	private Controller firstPlayerController;
	
	public GameApplication(int w, int h) {
		super(w, h);
	}
	
	@Override
	public void load() {
		
		player = new TopViewPlayer();
		
		firstPlayerController = new FirstPlayerController(player);
		
		updateAtFixedRate(30);
		
		loading = 100;
	}
	
	public void timeUpdate(long now) {
		player.update(now);		
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
		
		firstPlayerController.handleEvent(event);
		
		return null;
	}


}
