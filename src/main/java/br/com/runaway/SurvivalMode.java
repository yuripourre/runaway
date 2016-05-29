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
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.core.linear.PointInt2D;
import br.com.etyllica.effects.light.LightSource;
import br.com.etyllica.effects.light.ShadowLayer;
import br.com.runaway.ai.MoveHandler;
import br.com.runaway.collision.CollisionHandler;
import br.com.runaway.item.Item;
import br.com.runaway.item.Key;
import br.com.runaway.item.MedKit;
import br.com.runaway.menu.Congratulations;
import br.com.runaway.menu.GameOver;
import br.com.runaway.player.BlueSuitHuman;
import br.com.runaway.player.Hero;
import br.com.runaway.player.PlanningPlayer;
import br.com.runaway.player.RedSuitHuman;
import br.com.runaway.player.TopViewPlayer;
import br.com.runaway.player.Zoombie;
import br.com.runaway.trap.Explosive;
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
	private PlanningPlayer monster;
	private List<PlanningPlayer> monsters = new ArrayList<PlanningPlayer>(3);

	private Controller controller;
	private Controller joystick;

	private ShadowLayer shadowMap;
	private LightSource torch;

	private List<Trap> traps;
	private List<Item> items = new ArrayList<Item>();
	private List<Item> itemSet = new ArrayList<Item>();

	private CollisionHandler handler;
	private MoveHandler moveHandler;

	private Key key;

	private int ox = 0;
	private int oy = 0;

	private List<Tile> aim = new ArrayList<Tile>();

	long delay = 10;
	long lastUpdate = 0;
	
	int bombLimit = 5;

	public SurvivalMode(int w, int h, int currentLevel) {
		super(w, h);

		this.currentLevel = currentLevel;
	}

	@Override
	public void load() {
		loadMap();

		loading = 40;

		handler = new CollisionHandler(map.getMap());
		moveHandler = new MoveHandler(map.getMap());

		System.out.println("Columns: "+map.getColumns());
		System.out.println("Lines: "+map.getRows());

		player = new Hero(32, 32, handler);
		
		monsters.add(new Zoombie(232, 32, handler));
		monsters.add(new Zoombie(520, 32, handler));
		monsters.add(new Zoombie(520, 332, handler));
		monsters.add(new RedSuitHuman(520, 432, handler));
		
		monsters.add(new BlueSuitHuman(660, 332, handler));
		monsters.add(new RedSuitHuman(600, 432, handler));
		
		monsters.add(new Zoombie(660, 500, handler));

		monster = monsters.get(0);
		
		items.add(new MedKit(48, 200));

		controller = new EasyController(player);
		joystick = new JoystickController(player);

		loading = 50;

		shadowMap = new ShadowLayer(x, y, w, h);
		torch = new LightSource(player.getX(), player.getY(), 120);

		lifeBar = new LifeBar(player);

		loading = 100;

		//updateAtFixedRate(30, this);
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

		//map.disableGridShow();
		map.disableCollisionShow();
		map.disableCurrentTileShow();

		loading = 20;
		loadObjects(map);

		loading = 30;	
	}

	private void loadObjects(MapEditor map) {

		traps = new ArrayList<Trap>();

		Tile[][] tiles = map.getTiles();

		for(int j = 0; j < map.getRows(); j++) {

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

	public void update(long now) {
		/*if (lastUpdate + delay < now) {
			timeUpdate(now);
			lastUpdate = now;
		}*/
	}

	@Override
	public void timeUpdate(long now) {
		if(loading != 100) {
			return;
		}

		handler.updateCollision(now, player);
		player.update(now);
		
		ox = w/2-player.getX();
		oy = h/2-player.getY();

		for(PlanningPlayer monster: monsters) {
			handler.updateCollision(now, monster);
			monster.update(now);
			moveHandler.move(now, monster, player);
			
			if(handler.checkTrapCollisions(now, monster, traps)) {
				monster.setName("DEAD");
				monster.die();
			}
		}

		if(handler.checkTrapCollisions(now, player, traps)) {
			trapCollision(now, player);
		}
		
		for(Item item: items) {
			if(item.colide(player.getCenter().getX(), player.getCenter().getY())) {
				item.act(player, now);
				itemSet.add(item);
			}
		}
		
		for(Item item: itemSet) {
			items.remove(item);
		}

		if(checkKeyCollision(now)) {
			nextLevel();
		}

		/*int p1x = player.getX()+player.getBodyLayer().getTileW()/2;
		int p1y = player.getY()+player.getBodyLayer().getTileH()/2;

		torch.setCoordinates(p1x-torch.getW()/2, p1y-torch.getH()/2);*/

		//handler.updateCollision(now, player);

		//moveHandler.move(now, monster, player);
		//monster.update(now);
		player.setTargetUpdated(false);
		for(PlanningPlayer monster: monsters) {
			monster.setTargetUpdated(false);
		}
	}

	private boolean checkKeyCollision(long now) {
		if(key == null)
			return false;

		PointInt2D center = player.getCenter();

		if(key.colide(center.getX(), center.getY())) {
			return true;
		}

		return false;
	}

	private void trapCollision(long now, TopViewPlayer player) {
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
	public void draw(Graphics g) {

		long now = System.currentTimeMillis();
		if (lastUpdate + delay < now) {
			timeUpdate(now);
			lastUpdate = now;
		}

		if (loading != 100) {
			return;
		}
		
		drawScene(g);

		lifeBar.draw(g);
	}

	private void drawScene(Graphics g) {
		map.getMap().draw(g, ox, oy);

		//Draw aim
		for(Tile tile:aim) {
			g.setColor(Color.BLACK);
			g.fillRect(tile);
		}

		//Draw current tile
		/*
		g.setColor(Color.RED);
		g.fillRect(currentTile);

		//Draw monster path
		g.setColor(Color.BLUE);
		for(PointInt2D path: monster.getPath()) {
			int w = currentTile.getW();
			int h = currentTile.getH();
			//map.getMap().getIndex(path.getX(), path.getY(), pathTile);
			g.fillRect(path.getX()*w, path.getY()*h, w, h);
		}*/

		for(Trap trap : traps) {
			trap.draw(g, ox, oy);
		}
		
		for(Item item : items) {
			item.draw(g, ox, oy);
		}

		if (key != null) {
			key.draw(g, ox, oy);
		}

		player.draw(g, ox, oy);
		for(PlanningPlayer monster: monsters) {
			if(monster.isDead()) {
				continue;
			}
			monster.draw(g, ox, oy);
		}

		//shadowMap.drawLights(g, torch);
	}

	@Override
	public void updateKeyboard(KeyEvent event) {
		controller.handleEvent(event);
		joystick.handleEvent(event);

		if(event.isAnyKeyDown(KeyEvent.VK_SPACE)) {
			dropBomb();
		}
	}
	
	private void dropBomb() {
		if (bombLimit <= 0) {
			return;
		}
		
		bombLimit--;
		traps.add(new Explosive(player));
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
