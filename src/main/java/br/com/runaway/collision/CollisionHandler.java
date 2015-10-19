package br.com.runaway.collision;

import java.util.List;

import br.com.etyllica.core.linear.PointInt2D;
import br.com.etyllica.layer.Layer;
import br.com.runaway.player.TopViewPlayer;
import br.com.runaway.trap.Trap;
import br.com.tide.action.player.ActionPlayer;
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

	public boolean updateCollision(long now, TopViewPlayer player) {
		if(!handleCollision)
			return false;

		int cx = player.getCenter().getX()+map.getX();
		int cy = player.getCenter().getY()+map.getY();

		updateHitPoints(player);

		Tile tile = map.getTile(cx, cy, targetTile);

		/*if(map.isBlock(tile)) {
			player.setColor(Color.RED);
		} else {
			player.resetColor();
		}*/

		boolean colisionVertical =  handleVerticalCollision(player);
		boolean colisionHorizontal = handleHorizontalCollision(player);

		return colisionHorizontal||colisionVertical;
	}

	private boolean handleVerticalCollision(TopViewPlayer player) {

		int cy = player.getCenter().getY();

		int ydif = cy%map.getTileHeight();

		if(ydif < map.getTileHeight()/2) {

			if(map.isBlock(getUpperTile(targetTile))) {
				player.setY(player.getY() + map.getTileHeight()/2 - ydif);
				return true;
			}

		} else if(ydif > map.getTileHeight()/2) {

			if(map.isBlock(getLowerTile(targetTile))) {
				player.setY(player.getY() - (ydif - map.getTileHeight()/2));
				return true;
			}
		}
		return false;
	}

	private boolean handleHorizontalCollision(TopViewPlayer player) {

		int cx = player.getCenter().getX();

		int xdif = cx%map.getTileWidth();

		if(xdif < map.getTileWidth()/2) {

			if(map.isBlock(getLeftTile(targetTile))) {
				player.setX(player.getX() + map.getTileWidth()/2 - xdif);
				return true;
			}

		} else if(xdif > map.getTileWidth()/2) {

			if(map.isBlock(getRightTile(targetTile))) {
				player.setX(player.getX() - (xdif - map.getTileWidth()/2));
				return true;
			}
		}
		return false;	
	}

	public boolean checkTrapCollisions(long now, TopViewPlayer player, List<Trap> traps) {

		PointInt2D center = player.getCenter();

		for(Trap trap : traps) {
			trap.update(now);

			if(trap.isActive() && !player.isInvincibility()) {
				if(trap.colideCirclePoint(center.getX(), center.getY())) {
					return true;
				}
			}
		}

		return false;
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
	public void onWalkForward(ActionPlayer<TopViewPlayer> player) {
		handleCollision = true;
	}

	@Override
	public void onWalkBackward(ActionPlayer<TopViewPlayer> player) {
		handleCollision = true;
	}

	@Override
	public void onStopWalkForward(ActionPlayer<TopViewPlayer> player) {
		handleCollision = false;
	}

	@Override
	public void onStopWalkBackward(ActionPlayer<TopViewPlayer> player) {
		handleCollision = false;
	}

	@Override
	public void onTurnLeft(ActionPlayer<TopViewPlayer> player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTurnRight(ActionPlayer<TopViewPlayer> player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnLeft(ActionPlayer<TopViewPlayer> player) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTurnRight(ActionPlayer<TopViewPlayer> player) {
		// TODO Auto-generated method stub
	}

	public Tile checkAimTarget(TopViewPlayer player, int x, int y) {
		int x1 = player.getCenter().getX();
		int y1 = player.getCenter().getY();
		int x2 = x;
		int y2 = y;

		return visionLine(x1, y1, x2, y2);
	}

	public void checkAim(List<Tile> aim, TopViewPlayer player, int x, int y) {
		aim.clear();

		int x1 = player.getCenter().getX();
		int y1 = player.getCenter().getY();
		int x2 = x;
		int y2 = y;

		useVisionLineList(aim, x1, y1, x2, y2);
	}

	/**
	 * Based on http://eugen.dedu.free.fr/projects/bresenham/
	 * @param y1
	 * @param x1
	 * @param y2
	 * @param x2
	 */
	private void useVisionLineList(List<Tile> aim, int x1, int y1, int x2, int y2) {
		int i;               // loop counter
		int ystep, xstep;    // the step on y and x axis
		int error;           // the error accumulated during the increment
		int errorprev;       // *vision the previous value of the error variable
		int y = y1/map.getTileHeight(), x = x1/map.getTileWidth();  // the line points
		int ddy, ddx;        // compulsory variables: the double values of dy and dx
		int dx = (x2 - x1)/map.getTileWidth(); //(dx/=tile.w)
		int dy = (y2 - y1)/map.getTileHeight(); //(dy/=tile.h)

		checkAimPoint(aim, x, y);  // first point

		// NB the last point can't be here, because of its previous point (which has to be verified)
		if (dy < 0) {
			ystep = -1;
			dy = -dy;
		} else {
			ystep = 1;
		}
		if (dx < 0) {
			xstep = -1;
			dx = -dx;
		} else {
			xstep = 1;
		}

		ddy = 2 * dy;  // work with double values for full precision
		ddx = 2 * dx;
		if (ddx >= ddy){  // first octant (0 <= slope <= 1)
			// compulsory initialization (even for errorprev, needed when dx==dy)
			errorprev = error = dx;  // start in the middle of the square
			for (i=0 ; i < dx ; i++){  // do not use the first point (already done)
				x += xstep;
				error += ddy;
				if (error > ddx){  // increment y if AFTER the middle ( > )
					y += ystep;
					error -= ddx;
					// three cases (octant == right->right-top for directions below):
					if (error + errorprev < ddx)  // bottom square also
						checkAimPoint(aim, x, y-ystep);
					else if (error + errorprev > ddx)  // left square also
						checkAimPoint(aim, x-xstep, y);
					/*else{  // corner: bottom and left squares also
	          POINT(aim, x, y-ystep);
	          POINT(aim, x-xstep, y);
	        }*/
				}
				checkAimPoint(aim, x, y);
				errorprev = error;
			}
		}else{  // the same as above
			errorprev = error = dy;
			for (i=0 ; i < dy ; i++){
				y += ystep;
				error += ddx;
				if (error > ddy){
					x += xstep;
					error -= ddy;
					if (error + errorprev < ddy)
						checkAimPoint(aim, x-xstep, y);
					else if (error + errorprev > ddy)
						checkAimPoint(aim, x, y-ystep);
					/*else{
	          POINT(aim, x-xstep, y);
	          POINT(aim, x, y-ystep);
	        }*/
				}
				checkAimPoint(aim, x, y);
				errorprev = error;
			}
		}
	}

	private boolean checkAimPoint(List<Tile> aim, int x, int y) {
		Tile tile = map.getTiles()[y][x];
		aim.add(tile);
		if(map.isBlock(tile)) {
			return true;
		}
		return false;
	}
	
	public Tile getCurrentTile(TopViewPlayer player) {
		int x = player.getCenter().getX()/map.getTileWidth();
		int y = player.getCenter().getY()/map.getTileHeight();
		return getCurrentTile(x, y);
	}
	
	public Tile getCurrentTile(int x, int y) {
		return map.getTiles()[y][x];
	}

	private Tile visionLine(int x1, int y1, int x2, int y2) {
		int i;               // loop counter
		int ystep, xstep;    // the step on y and x axis
		int error;           // the error accumulated during the increment
		int errorprev;       // *vision the previous value of the error variable
		int y = y1/map.getTileHeight(), x = x1/map.getTileWidth();  // the line points
		int ddy, ddx;        // compulsory variables: the double values of dy and dx
		int dx = (x2 - x1)/map.getTileWidth(); //(dx/=tile.w)
		int dy = (y2 - y1)/map.getTileHeight(); //(dy/=tile.h)

		// NB the last point can't be here, because of its previous point (which has to be verified)
		if (dy < 0) {
			ystep = -1;
			dy = -dy;
		} else {
			ystep = 1;
		}
		if (dx < 0) {
			xstep = -1;
			dx = -dx;
		} else {
			xstep = 1;
		}

		ddy = 2 * dy;  // work with double values for full precision
		ddx = 2 * dx;
		if (ddx >= ddy){  // first octant (0 <= slope <= 1)
			// compulsory initialization (even for errorprev, needed when dx==dy)
			errorprev = error = dx;  // start in the middle of the square
			for (i=0 ; i < dx ; i++){  // do not use the first point (already done)
				x += xstep;
				error += ddy;
				if (error > ddx){  // increment y if AFTER the middle ( > )
					y += ystep;
					error -= ddx;
					// three cases (octant == right->right-top for directions below):
					if (error + errorprev < ddx) {  // bottom square also
						if(checkTarget(x, y-ystep)) {
							return map.getTiles()[y-ystep][x];
						}
					} else if (error + errorprev > ddx) {  // left square also
						if (checkTarget(x-xstep, y)) {
							return map.getTiles()[y][x-xstep];
						}
					} else {
						if (checkTarget(x-xstep, y)) {
							return map.getTiles()[y][x-xstep];
						}
						if(checkTarget(x, y-ystep)) {
							return map.getTiles()[y-ystep][x];
						}
					}
					

				}
				if (checkTarget(x, y)) {
					return map.getTiles()[y][x];
				}
				errorprev = error;
			}
		} else {  // the same as above
			errorprev = error = dy;
			for (i=0 ; i < dy ; i++) {
				y += ystep;
				error += ddx;
				if (error > ddy) {
					x += xstep;
					error -= ddy;
					if (error + errorprev < ddy) {
						if (checkTarget(x-xstep, y)) {
							return map.getTiles()[y][x-xstep];
						}
					} else if (error + errorprev > ddy) {
						if(checkTarget(x, y-ystep)) {
							return map.getTiles()[y-ystep][x];
						}
					} else {
						if (checkTarget(x-xstep, y)) {
							return map.getTiles()[y][x-xstep];
						}
						if(checkTarget(x, y-ystep)) {
							return map.getTiles()[y-ystep][x];
						}
					}
					
				}
				if (checkTarget(x, y)) {
					return map.getTiles()[y][x];
				}
				errorprev = error;
			}
		}
		
		return null;
	}

	private boolean checkTarget(int x, int y) {
		Tile tile = map.getTiles()[y][x];
		return map.isBlock(tile);
	}

}
