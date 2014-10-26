package br.com.runaway.collision;

import java.awt.Color;

import br.com.etyllica.layer.Layer;
import br.com.etyllica.linear.PointInt2D;
import br.com.runaway.player.TopViewPlayer;
import br.com.tide.action.player.ActionPlayerListener;
import br.com.vite.map.Map;
import br.com.vite.tile.Tile;

public class CollisionHandler implements ActionPlayerListener<TopViewPlayer> {

	private Map map;

	private boolean handleCollision = false;

	private PointInt2D upperLeftPoint = new PointInt2D(0, 0);

	private PointInt2D lowerRightPoint = new PointInt2D(0, 0);

	private PointInt2D targetTile = new PointInt2D(0, 0);

	public CollisionHandler(Map map) {
		super();
		this.map = map;
	}

	public void updateCollision(TopViewPlayer player) {
		if(!handleCollision)
			return;

		int cx = player.getCenter().getX();
		int cy = player.getCenter().getY();

		updateHitPoints(player);

		Tile tile = map.getTile(cx, cy, targetTile);

		if(map.isBlock(tile)) {
			player.setColor(Color.RED);
		} else {
			player.resetColor();
		}

		//If collide upper tile
		if(cy%map.getTileHeight()<map.getTileHeight()/2) {

			if(map.isBlock(getUpperTile(targetTile))) {
				player.setColor(Color.GREEN);
			}

			//If collide lower tile
		} else if(cy%map.getTileHeight()>map.getTileHeight()/2) {

			if(map.isBlock(getLowerTile(targetTile))) {
				player.setColor(Color.BLUE);
			}

		}
		
		//Horizontal collision
		if(cx%map.getTileWidth()<map.getTileWidth()/2) {

			if(map.isBlock(getLeftTile(targetTile))) {
				player.setColor(Color.ORANGE);
			}

			//If collide lower tile
		} else if(cx%map.getTileWidth()>map.getTileWidth()/2) {

			if(map.isBlock(getRightTile(targetTile))) {
				player.setColor(Color.MAGENTA);
			}

		}
	}

	private void updateHitPoints(TopViewPlayer player) {

		Layer hitbox = player.getHitbox();

		upperLeftPoint.setLocation(hitbox.getX(), hitbox.getY());

		lowerRightPoint.setLocation(hitbox.getX()+hitbox.getW(), hitbox.getY()+hitbox.getH());

	}	

	private Tile getUpperTile(PointInt2D target) {
		return map.getTile(target.getX()*map.getTileWidth(), (target.getY()-1)*map.getTileHeight());
	}

	private Tile getLowerTile(PointInt2D target) {
		return map.getTile(target.getX()*map.getTileWidth(), (target.getY()+1)*map.getTileHeight());
	}

	private Tile getRightTile(PointInt2D target) {
		return map.getTile((target.getX()+1)*map.getTileWidth(), target.getY()*map.getTileHeight());
	}

	private Tile getLeftTile(PointInt2D target) {
		return map.getTile((target.getX()-1)*map.getTileWidth(), target.getY()*map.getTileHeight());
	}

	@Override
	public void onWalkForward(TopViewPlayer player) {
		handleCollision = true;
	}

	@Override
	public void onWalkBackward(TopViewPlayer player) {
		handleCollision = true;
	}

	@Override
	public void onStopWalkForward(TopViewPlayer player) {
		handleCollision = false;
	}

	@Override
	public void onStopWalkBackward(TopViewPlayer player) {
		handleCollision = false;
	}

	@Override
	public void onTurnLeft(TopViewPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTurnRight(TopViewPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnLeft(TopViewPlayer player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnRight(TopViewPlayer player) {
		// TODO Auto-generated method stub
	}

}
