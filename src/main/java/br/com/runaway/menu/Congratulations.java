package br.com.runaway.menu;

import java.awt.Color;

import br.com.etyllica.core.animation.script.OpacityAnimation;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.layer.ImageLayer;

public class Congratulations extends Application {

	private ImageLayer background;	
	
	public Congratulations(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		
		background = new ImageLayer("menu/congratulations.jpg");
		
		OpacityAnimation fadeIn = new OpacityAnimation(background, 5000);
		fadeIn.setInterval(0, 0xff);
		scene.addAnimation(fadeIn);
		
		loading = 100;
	}
	
	public void updateMouse(PointerEvent event) {
	
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT))
			restartGame();
	}
	
	@Override
	public void draw(Graphic g) {
		g.setColor(Color.BLACK);
		g.fillRect(this);
		background.draw(g);
	}
	
	private void restartGame() {
		nextApplication = new MainMenu(w, h);
	}

}
