package br.com.runaway.menu;

import java.awt.Color;

import br.com.etyllica.core.animation.OnAnimationFinishListener;
import br.com.etyllica.core.animation.script.OpacityAnimation;
import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;

public class GameOver extends Application implements OnAnimationFinishListener {

	private ImageLayer background;	
	
	public GameOver(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {
		
		background = new ImageLayer("menu/gameover.jpg");
		
		OpacityAnimation fadeIn = new OpacityAnimation(background, 10000);
		fadeIn.setInterval(0, 0xff);
		fadeIn.setListener(this);
		scene.addAnimation(fadeIn);
		
		loading = 100;
	}
	
	public void updateMouse(PointerEvent event) {
		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT))
			restartGame();
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(this);
		background.draw(g);
	}

	@Override
	public void onAnimationFinish(long now) {
		restartGame();
	}
	
	private void restartGame() {
		nextApplication = new MainMenu(w, h);
	}

}
