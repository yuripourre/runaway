package br.com.runaway.player;

import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.linear.PointInt2D;
import br.com.runaway.ai.ActionData;
import br.com.tide.action.player.ActionPlayerListener;
import br.com.tide.ai.planning.PlanningAction;
import br.com.tide.ai.planning.PlanningStep;

public class Monster extends TopViewPlayer {

	private long lastPath = 0; 
	private long actDelay = 500;
	private PointInt2D target = new PointInt2D();
	
	private List<PlanningStep<ActionData>> actions = new ArrayList<PlanningStep<ActionData>>();
	private List<PointInt2D> path = new ArrayList<>();
	
	public Monster(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener, "monster/zombie_walk.png");
		turnSpeed = 1;
		currentSpeed = 2;
	}

	@Override
	public void draw(Graphic g, int x, int y) {
		super.draw(g, x, y);
		g.drawShadow(this.x+x, this.y+y, name);
	}

	public List<PointInt2D> getPath() {
		return path;
	}

	public void setPath(List<PointInt2D> path) {
		this.path = path;
		actions.clear();
	}
	
	public void addTurnAction(double angle) {
		ActionData data = new ActionData();
		data.angle = angle;
		actions.add(new PlanningStep<ActionData>(PlanningAction.TURN, data));
	}
	
	public PlanningStep<ActionData> currentAction() {
		return actions.get(0);
	}

	public void nextAction() {
		actions.remove(currentAction());
	}

	public void addMoveAction(double angle, PointInt2D point) {
		ActionData data = new ActionData();
		data.angle = angle;
		data.point = point;
		actions.add(new PlanningStep<ActionData>(PlanningAction.MOVE, data));
	}
	
	public void addMoveAction(PointInt2D point) {
		ActionData data = new ActionData();
		data.point = point;
		actions.add(new PlanningStep<ActionData>(PlanningAction.MOVE, data));
	}

	public void addAttackAction(PointInt2D point, Hero target) {
		ActionData data = new ActionData();
		data.point = point;
		data.target = target;
		actions.add(new PlanningStep<ActionData>(PlanningAction.ACT, data));
	}
	
	public PointInt2D getTarget() {
		return target;
	}

	public void setLastPath(long now) {
		this.lastPath = now;
	}
	
	public long getLastPath() {
		return lastPath;
	}
	
	public long getActDelay() {
		return actDelay;
	}

	public List<PlanningStep<ActionData>> getActions() {
		return actions;
	}
	
}

