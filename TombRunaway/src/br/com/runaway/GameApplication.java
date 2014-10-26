package br.com.runaway;

import java.awt.Color;
import java.io.FileNotFoundException;

import br.com.etyllica.collision.CollisionDetector;
import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.graphics.SVGColor;
import br.com.etyllica.effects.light.LightSource;
import br.com.etyllica.effects.light.ShadowLayer;
import br.com.etyllica.layer.Layer;
import br.com.etyllica.linear.Point2D;
import br.com.etyllica.linear.PointInt2D;
import br.com.runaway.collision.CollisionHandler;
import br.com.runaway.player.TopViewPlayer;
import br.com.runaway.trap.SpikeFloor;
import br.com.runaway.ui.LifeBar;
import br.com.tide.action.player.ActionPlayerListener;
import br.com.tide.input.controller.Controller;
import br.com.tide.input.controller.EasyController;
import br.com.tide.input.controller.FirstPlayerController;
import br.com.vite.editor.MapEditor;
import br.com.vite.export.MapExporter;
import br.com.vite.map.Map;
import br.com.vite.tile.Tile;

public class GameApplication extends Application {

	/*private Camera camera1;
	private Camera camera2;*/
	
	//GUI Stuff
	private LifeBar lifeBar;

	private MapEditor map;

	private TopViewPlayer player;

	private Controller secondPlayerController;

	private Layer obstacle;

	private ShadowLayer shadowMap;

	private LightSource torch1;

	private SpikeFloor trap;
		
	private CollisionHandler handler;
		
	public GameApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		//camera1 = new Camera(0, 0, w/2, h);
		//camera2 = new Camera(w/2, 0, w/2, h);
		
		//player = new TopViewPlayer(w/4, h/2, this);
		//firstPlayerController = new FirstPlayerController(player);


		try {
			map = MapExporter.load("map1.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		handler = new CollisionHandler(map.getMap());

		player = new TopViewPlayer(w/4+20, h/2+80, handler);

		secondPlayerController = new EasyController(player);

		updateAtFixedRate(30);

		obstacle = new Layer(200,200,100,50);
		obstacle.setAngle(20);

		shadowMap = new ShadowLayer(x, y, w, h);
		torch1 = new LightSource(player.getX(), player.getY(), 120);

		trap = new SpikeFloor(w/2, h/2);
		
		lifeBar = new LifeBar();

		loading = 100;
	}

	public void timeUpdate(long now) {
		player.update(now);

		trap.update(now);

		int p1x = player.getX()+player.getLayer().getTileW()/2;
		int p1y = player.getY()+player.getLayer().getTileH()/2;

		torch1.setCoordinates(p1x-torch1.getW()/2, p1y-torch1.getH()/2);
		
		handler.updateCollision(player);
		
	}
	
	@Override
	public void draw(Graphic g) {

		drawScene(g);
				
		lifeBar.draw(g, 2, 3);

	}

	private void drawScene(Graphic g) {
		map.draw(g);

		trap.draw(g);

		player.draw(g);

		g.setColor(Color.BLACK);
		for(Point2D point: CollisionDetector.getBounds(player.getHitbox())) {
			g.fillCircle(point, 5);
		}
		for(Point2D point: CollisionDetector.getBounds(obstacle)) {
			g.fillCircle(point, 5);
		}


		g.fillRect(obstacle);

		//shadowMap.drawLights(g, torch1);
	}

	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event) {

		secondPlayerController.handleEvent(event);

		return null;
	}
		
	

}
