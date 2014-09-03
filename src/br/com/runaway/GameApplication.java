package br.com.runaway;

import java.awt.Color;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.graphics.SVGColor;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.layer.colision.ColisionDetector;
import br.com.etyllica.linear.Point2D;
import br.com.runaway.player.TopViewPlayer;
import br.com.tide.input.controller.Controller;
import br.com.tide.input.controller.FirstPlayerController;

public class GameApplication extends Application {
	
	private TopViewPlayer player;
	
	private Controller firstPlayerController;
	
	private Layer obstacle;
	
	public GameApplication(int w, int h) {
		super(w, h);
	}
	
	@Override
	public void load() {
		
		player = new TopViewPlayer();
		
		firstPlayerController = new FirstPlayerController(player);
		
		updateAtFixedRate(30);
		
		obstacle = new Layer(200,200,100,50);
		obstacle.setAngle(20);
		
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
		

		g.setColor(Color.BLACK);
		for(Point2D point: ColisionDetector.getBounds(player.getHitbox())) {
			g.fillCircle(point, 5);
		}
		for(Point2D point: ColisionDetector.getBounds(obstacle)) {
			g.fillCircle(point, 5);
		}
		
		if(ColisionDetector.colidePolygon(player.getHitbox(), obstacle)) {
			g.setColor(SVGColor.SAVAGE_BLUE);
		} else {
			g.setColor(SVGColor.CORN_SILK);
		}
				
		g.fillRect(obstacle);
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
