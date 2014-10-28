package br.com.runaway;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.animation.scripts.OpacityAnimation;
import br.com.etyllica.collision.CollisionDetector;
import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.effects.light.LightSource;
import br.com.etyllica.effects.light.ShadowLayer;
import br.com.etyllica.linear.Point2D;
import br.com.etyllica.linear.PointInt2D;
import br.com.runaway.animation.HitAnimation;
import br.com.runaway.collision.CollisionHandler;
import br.com.runaway.menu.GameOver;
import br.com.runaway.player.TopViewPlayer;
import br.com.runaway.trap.SpikeFloor;
import br.com.runaway.trap.Trap;
import br.com.runaway.ui.LifeBar;
import br.com.tide.input.controller.Controller;
import br.com.tide.input.controller.EasyController;
import br.com.vite.editor.MapEditor;
import br.com.vite.export.MapExporter;
import br.com.vite.tile.Tile;
import br.com.vite.tile.layer.ImageTileObject;

public class GameApplication extends Application {

	//GUI Stuff
	private LifeBar lifeBar;

	private MapEditor map;

	private TopViewPlayer player;

	private Controller secondPlayerController;

	private ShadowLayer shadowMap;

	private LightSource torch1;

	private List<Trap> traps;

	private CollisionHandler handler;
	
	private HitAnimation invincible;
	
	public GameApplication(int w, int h) {
		super(w, h);
	}

	@Override
	public void load() {

		loadMap();

		handler = new CollisionHandler(map.getMap());

		player = new TopViewPlayer(w/4+264, h/2-50, handler);

		secondPlayerController = new EasyController(player);

		updateAtFixedRate(30);

		shadowMap = new ShadowLayer(x, y, w, h);
		torch1 = new LightSource(player.getX(), player.getY(), 120);

		invincible = new HitAnimation(player);
		
		lifeBar = new LifeBar(player);

		loading = 100;
	}

	private void loadMap() {

		try {
			map = MapExporter.load("map8.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		traps = new ArrayList<Trap>();

		Tile[][] tiles = map.getTiles();

		for(int j = 0; j < map.getLines(); j++) {

			for(int i = 0; i < map.getColumns(); i++) {

				ImageTileObject obj = tiles[j][i].getObjectLayer();

				if(obj != null) {

					if("SPIKE".equals(obj.getLabel())) {
						traps.add(new SpikeFloor(i*map.getTileWidth(), j*map.getTileHeight()));
						tiles[j][i].setObjectLayer(null);
					}
				}
			}
		}
	}

	public void timeUpdate(long now) {
		player.update(now);

		for(Trap trap : traps) {
			trap.update(now);

			if(trap.isActive() && !player.isInvincibility()) {
				
				PointInt2D center = player.getCenter();
				
				if(trap.colideCirclePoint(center.getX(), center.getY())) {
					player.loseLife();
					player.setInvincibility(true);				
					invincible.startAnimation(now);
					
					if(player.getCurrentLife()<0)
						nextApplication = new GameOver(w, h);
				}
			}

		}

		int p1x = player.getX()+player.getLayer().getTileW()/2;
		int p1y = player.getY()+player.getLayer().getTileH()/2;

		torch1.setCoordinates(p1x-torch1.getW()/2, p1y-torch1.getH()/2);

		handler.updateCollision(player);

	}

	@Override
	public void draw(Graphic g) {

		drawScene(g);

		lifeBar.draw(g);
	}

	private void drawScene(Graphic g) {
		map.draw(g);

		for(Trap trap : traps) {
			trap.draw(g);	
		}		

		player.draw(g);

		g.setColor(Color.BLACK);
		for(Point2D point: CollisionDetector.getBounds(player.getHitbox())) {
			g.fillCircle(point, 5);
		}

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
