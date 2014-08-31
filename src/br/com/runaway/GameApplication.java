package br.com.runaway;

import java.awt.Color;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;

public class GameApplication extends Application {

	private ImageLayer player;
	
	public GameApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		
		player = new ImageLayer(0, 0, 96,64, "player/player_walk_strip6.png");
		
		loading = 100;
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
		// TODO Auto-generated method stub
		return null;
	}


}
