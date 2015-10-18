package br.com.runaway;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.context.UpdateIntervalListener;
import br.com.etyllica.core.event.KeyEvent;
import br.com.etyllica.core.event.MouseButton;
import br.com.etyllica.core.event.PointerEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.linear.PointInt2D;
import br.com.etyllica.effects.light.LightSource;
import br.com.etyllica.effects.light.ShadowLayer;
import br.com.runaway.collision.CollisionHandler;
import br.com.runaway.item.Key;
import br.com.runaway.menu.Congratulations;
import br.com.runaway.menu.GameOver;
import br.com.runaway.player.Hero;
import br.com.runaway.player.Monster;
import br.com.runaway.trap.SpikeFloor;
import br.com.runaway.trap.Trap;
import br.com.runaway.ui.LifeBar;
import br.com.tide.input.controller.Controller;
import br.com.tide.input.controller.EasyController;
import br.com.tide.input.controller.JoystickController;
import br.com.vite.editor.MapEditor;
import br.com.vite.export.MapExporter;
import br.com.vite.tile.Tile;
import br.com.vite.tile.layer.ImageTileObject;

public class SurvivalMode extends Application implements UpdateIntervalListener {

	public int currentLevel = 1;

	public static final int MAX_LEVEL = 10;

	public static final String PARAM_LEVEL = "level";

	//GUI Stuff
	private LifeBar lifeBar;

	private MapEditor map;

	private Hero player;
	private Monster monster;

	private Controller controller;

	private Controller joystick;

	private ShadowLayer shadowMap;

	private LightSource torch;

	private List<Trap> traps;

	private CollisionHandler handler;

	private Key key;

	private int ox = 0;
	private int oy = 0;
	
	private List<Tile> aim = new ArrayList<Tile>();

	public SurvivalMode(int w, int h, int currentLevel) {
		super(w, h);

		this.currentLevel = currentLevel;
	}

	@Override
	public void load() {

		loadMap();

		loading = 40;

		handler = new CollisionHandler(map.getMap());

		player = new Hero(32, 32, handler);
		monster = new Monster(320, 32, handler);		

		controller = new EasyController(player);
		joystick = new JoystickController(player);

		loading = 50;

		shadowMap = new ShadowLayer(x, y, w, h);
		torch = new LightSource(player.getX(), player.getY(), 120);

		lifeBar = new LifeBar(player);

		loading = 100;

		updateAtFixedRate(30, this);
	}

	private void loadMap() {

		int level = currentLevel;

		loadingInfo = "Loading Level "+level;
		loading = 1;

		try {
			map = MapExporter.load("map"+level+".json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		loading = 10;

		map.disableGridShow();
		map.disableCollisionShow();
		map.disableCurrentTileShow();

		loading = 20;
		loadObjects(map);

		loading = 30;		
	}

	private void loadObjects(MapEditor map) {

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

					if("KEY".equals(obj.getLabel())) {
						key = new Key(i*map.getTileWidth(), j*map.getTileHeight());
						tiles[j][i].setObjectLayer(null);
					}
				}
			}
		}
	}

	@Override
	public void timeUpdate(long now) {
		player.update(now);

		if(handler.checkTrapCollisions(now, player, traps))
			trapCollision(now);

		if(checkKeyCollision(now))
			nextLevel();

		int p1x = player.getX()+player.getBodyLayer().getTileW()/2;
		int p1y = player.getY()+player.getBodyLayer().getTileH()/2;

		torch.setCoordinates(p1x-torch.getW()/2, p1y-torch.getH()/2);

		handler.updateCollision(now, player);
	}

	private boolean checkKeyCollision(long now) {
		if(key == null)
			return false;

		PointInt2D center = player.getCenter();

		if(key.colideCirclePoint(center.getX(), center.getY())) {
			return true;
		}

		return false;
	}

	private void trapCollision(long now) {
		player.loseLife(now);

		if(player.getCurrentLife() < 0)
			nextApplication = new GameOver(w, h);
	}

	private void nextLevel() {

		int level = currentLevel;

		if(level < MAX_LEVEL) {

			session.put(PARAM_LEVEL, level+1);

			nextApplication = new SurvivalMode(w, h, level+1);

		} else {
			nextApplication = new Congratulations(w, h);
		}
	}

	@Override
	public void draw(Graphic g) {

		drawScene(g);

		lifeBar.draw(g);
	}

	private void drawScene(Graphic g) {
		map.getMap().draw(g, ox, oy);

		for(Trap trap : traps) {
			trap.draw(g, ox, oy);
		}

		if(key!=null)
			key.draw(g, ox, oy);

		player.draw(g, ox, oy);
		monster.draw(g, ox, oy);

		//Draw aim
		for(Tile tile:aim) {
			g.setColor(Color.BLACK);
			g.fillRect(tile);	
		}
		
		
		//shadowMap.drawLights(g, torch);
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		controller.handleEvent(event);
		joystick.handleEvent(event);
	}
	
	@Override
	public void updateMouse(PointerEvent event) {

		if(event.isButtonDown(MouseButton.MOUSE_BUTTON_LEFT)) {
			aim.clear();
			handler.checkAim(aim, player, event.getX(), event.getY());
			
			/*Tile tile = handler.checkAimTarget(player, event.getX(), event.getY());
			
			if(tile!= null) {
				aim.add(tile);
			}*/
		}
	}

}
