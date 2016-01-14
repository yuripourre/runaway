package br.com.runaway.ai;

import java.util.List;

import br.com.etyllica.core.linear.PointInt2D;
import br.com.etyllica.util.PointUtils;
import br.com.runaway.player.Hero;
import br.com.runaway.player.Monster;
import br.com.tide.ai.planning.PlanningAction;
import br.com.vite.map.Map;
import br.com.vite.pathfind.AStar;
import br.com.vite.pathfind.Cell;
import br.com.vite.pathfind.PathContainer;
import br.com.vite.pathfind.PathFinder;

public class MoveHandler {

	private Map map;
	PathFinder pathFinder;

	public MoveHandler(Map map) {
		super();
		this.map = map;
		pathFinder = new AStar(map);
	}

	/**
	 * Get the path to go from (si, sj) to (ei, ej) 
	 * @param si Start location's x
	 * @param sj Start location's y
	 * @param ei End location's x
	 * @param ej End location's y
	 * @return List of Points (Empty list if target is unreachable) 
	 */
	public List<PointInt2D> path(int si, int sj, int ei, int ej) {
		return path(map.getColumns(), map.getLines(), si, sj, ei, ej);
	}

	/**
	 * Get the path to go from (si, sj) to (ei, ej)
	 * @param x Board's lines
	 * @param y Board's columns
	 * @param si Start location's x
	 * @param sj Start location's y
	 * @param ei End location's x
	 * @param ej End location's y
	 * @return List of Points (Empty list if target is unreachable) 
	 */
	public List<PointInt2D> path(int x, int y, int si, int sj, int ei, int ej) {
		pathFinder.init();

		//Display initial map
		//printGrid(x, y, si, sj, ei, ej);

		PathContainer container = pathFinder.calculatePath(si, sj, ei, ej);

		//printScores(container.getGrid(), x, y);
		//printPath(container.getPath());

		return container.getPath();
	}

	private void printScores(Cell[][] grid, int x, int y) {
		System.out.println("\nScores for cells: ");
		for(int i=0;i<x;++i){
			for(int j=0;j<y;++j){
				if(grid[i][j]!=null)System.out.printf("%-3d ", grid[i][j].getFinalCost());
				else System.out.print("BL  ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private void printPath(List<PointInt2D> path) {
		if(!path.isEmpty()) {
			System.out.println("Path: ");

			for(PointInt2D point: path) {
				System.out.print(point+" -> ");
			}

			System.out.println("*");

		} else {
			System.out.println("No possible path");
		}
	}

	private void printGrid(Cell[][] grid, int x, int y, int si, int sj, int ei, int ej) {
		System.out.println("Grid: ");
		for(int i=0;i<x;++i){
			for(int j=0;j<y;++j){
				if(i==si&&j==sj)System.out.print("SO  "); //Source
				else if(i==ei && j==ej)System.out.print("DE  ");  //Destination
				else if(grid[i][j]!=null)System.out.printf("%-3d ", 0);
				else System.out.print("BL  "); 
			}
			System.out.println();
		} 
		System.out.println();
	}

	public void move(long now, Monster monster, Hero target) {
		if (monster.getLastPath() == 0 || monster.getLastPath() + monster.getActDelay() < now) {
					
			/*if(monster.getActions().size() == 0) {
				//GeneratePath
				regenerateActions(monster, target);
			}*/
			regenerateActions(monster, target);
			
			monster.setLastPath(now);
		}

		if(monster.getActions().isEmpty()) {
			return;
		}
		
		PlanningAction action = monster.currentAction().getAction();
		
		switch(action) {
		case TURN:
			//turnAction(monster);
			//monster.nextAction();
			break;

		case MOVE:
			monster.setName("WALK");
			turnAction(monster);
			moveAction(monster);
			break;
		case ACT:
			monster.setName("ACT");
			/*monster.setName("ATK");
			attackAction(monster);*/
			break;
		case WAIT:
			monster.setName("WAIT");
			waitAction(monster);
			break;
		default:
			break;
		}
	}
	
	private PointInt2D regenerateMoveActions(Monster monster, PointInt2D target) {
		
		int mx = monster.getTarget().getX();
		int my = monster.getTarget().getY();

		int tx = target.getX();
		int ty = target.getY();

		if(mx<0||my<0||tx<0||ty<0) {
			return null;
		}
		
		monster.setPath(path(mx, my, tx, ty));
		//Clear actions

		if(monster.getPath().isEmpty()) {
			return null;
		}
		PointInt2D lastPoint = monster.getPath().get(0);
		monster.getPath().remove(0);

		for(PointInt2D point : monster.getPath()) {
			double angle = PointUtils.angle(point.getX(), point.getY(), lastPoint.getX(), lastPoint.getY());

			//monster.addTurnAction(angle);
			monster.addMoveAction(angle, point);
			
			lastPoint = point;
			break;
		}
		
		return lastPoint;
	}
	
	private void regenerateActions(Monster monster, Hero target) {
		PointInt2D lastPoint = regenerateMoveActions(monster, target.getTarget());
		
		//monster.addAttackAction(lastPoint, target);
		monster.addWaitAction(lastPoint);
	}

	private void attackAction(Monster monster) {
		monster.attack();
	}

	private void turnAction(Monster monster) {
		double angle = monster.currentAction().getData().angle+90;
		angle%=360;

		monster.setStartAngle(angle);
		monster.getBodyLayer().setAngle(angle-90);

		//Slow Turn
		/*int tolerance = 4;

		if (angle < monster.getAngle() - tolerance) {
			monster.turnLeft();
		} else if (angle > monster.getAngle() + tolerance) {
			monster.turnRight();
		} else {
			monster.stopTurnLeft();
			monster.stopTurnRight();
			monster.nextAction();
		}*/
	}

	private void moveAction(Monster monster) {
		monster.walkForward();

		//map.getIndex(monster.getCenter().getX(), monster.getCenter().getY(), monster.getTarget());

		PointInt2D target = monster.currentAction().getData().point;

		int mx = monster.getTarget().getX();
		int my = monster.getTarget().getY();

		if(mx == target.getY() && my == target.getX()) {
			monster.stopWalkForward();
			monster.nextAction();
		}
	}
	
	private void waitAction(Monster monster) {
		monster.stopWalkForward();
		monster.nextAction();
	}

}


