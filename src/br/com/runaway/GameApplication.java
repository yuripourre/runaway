package br.com.runaway;

import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.io.FileNotFoundException;

import br.com.etyllica.cinematics.Camera;
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
import br.com.runaway.player.TopViewPlayer;
import br.com.runaway.trap.SpikeFloor;
import br.com.tide.input.controller.Controller;
import br.com.tide.input.controller.EasyController;
import br.com.tide.input.controller.FirstPlayerController;
import br.com.vite.editor.MapEditor;
import br.com.vite.export.MapExporter;

public class GameApplication extends Application {

	private Camera camera1;
	private Camera camera2;

	private MapEditor map;

	private TopViewPlayer player;

	private TopViewPlayer player2;

	private Controller firstPlayerController;

	private Controller secondPlayerController;

	private Layer obstacle;

	private ShadowLayer shadowMap;

	private LightSource torch1;

	private LightSource torch2;

	private SpikeFloor trap;

	public GameApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		camera1 = new Camera(0, 0, w/2, h);
		camera2 = new Camera(w/2, 0, w/2, h);
		
		player = new TopViewPlayer(w/4, h/2);

		player2 = new TopViewPlayer(w/4+20, h/2+80);

		firstPlayerController = new FirstPlayerController(player);

		secondPlayerController = new EasyController(player2);

		updateAtFixedRate(30);

		obstacle = new Layer(200,200,100,50);
		obstacle.setAngle(20);

		shadowMap = new ShadowLayer(x, y, w, h);
		torch1 = new LightSource(player.getX(), player.getY(), 120);
		torch2 = new LightSource(player2.getX(), player2.getY(), 120);

		try {
			map = MapExporter.load("map1.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		trap = new SpikeFloor(w/2, h/2);

		loading = 100;
	}

	public void timeUpdate(long now) {
		player.update(now);
		player2.update(now);

		trap.update(now);

		int p1x = player.getX()+player.getLayer().getTileW()/2;
		int p1y = player.getY()+player.getLayer().getTileH()/2;

		torch1.setCoordinates(p1x-torch1.getW()/2, p1y-torch1.getH()/2);

		int p2x = player2.getX()+player2.getLayer().getTileW()/2;
		int p2y = player2.getY()+player2.getLayer().getTileH()/2;

		torch2.setCoordinates(p2x-torch2.getW()/2, p2y-torch2.getH()/2);
		
		//camera1.setAimLocation(player.getX(), 0);
		//camera2.setAimLocation(player2.getX(), 0);
	}

	@Override
	public void draw(Graphic g) {

		//Draw First Player Camera
		g.setCamera(camera1);
		drawScene(g);
		g.resetCamera(camera1);
		camera1.draw(g);

		//Draw Second Player Camera
		g.setCamera(camera2);
		drawScene(g);
		g.resetCamera(camera2);
		camera2.draw(g);

	}

	private void drawScene(Graphic g) {
		map.draw(g);

		trap.draw(g);

		/*g.setColor(Color.GREEN);
		g.fillRect(0, 0, w/2, h);
		g.setColor(Color.BLUE);
		g.fillRect(w/2, 0, w/2, h);*/

		player.draw(g);
		player2.draw(g);

		g.setColor(Color.BLACK);
		for(Point2D point: CollisionDetector.getBounds(player.getHitbox())) {
			g.fillCircle(point, 5);
		}
		for(Point2D point: CollisionDetector.getBounds(obstacle)) {
			g.fillCircle(point, 5);
		}

		if(CollisionDetector.colidePolygon(player.getHitbox(), obstacle)) {
			g.setColor(SVGColor.SAVAGE_BLUE);
		} else {
			g.setColor(SVGColor.CORN_SILK);
		}

		g.fillRect(obstacle);

		//shadowMap.drawLights(g, torch1, torch2);
	}

	@Override
	public GUIEvent updateMouse(PointerEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GUIEvent updateKeyboard(KeyEvent event) {

		firstPlayerController.handleEvent(event);
		secondPlayerController.handleEvent(event);

		return null;
	}


}
