package br.com.runaway.player;

import java.util.ArrayList;
import java.util.List;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.linear.PointInt2D;
import br.com.runaway.ai.ActionData;
import br.com.tide.action.player.ActionPlayerListener;
import br.com.tide.ai.planning.PlanningAction;
import br.com.tide.ai.planning.PlanningStep;

public abstract class PlanningPlayer extends TopViewPlayer {

	protected long lastPath = 0; 
	protected long actDelay = 500;
		
	protected List<PlanningStep<ActionData>> actions = new ArrayList<PlanningStep<ActionData>>();
	protected List<PointInt2D> path = new ArrayList<>();
	
	public PlanningPlayer(int x, int y, ActionPlayerListener<TopViewPlayer> listener, String path) {
		super(x, y, listener, path);
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
	
	private PlanningStep<ActionData> WAIT_ACTION = new PlanningStep<ActionData>(PlanningAction.WAIT, new ActionData());
	
	public PlanningStep<ActionData> currentAction() {
		if(actions.isEmpty()) {
			WAIT_ACTION.getData().point = target;
			return WAIT_ACTION;
		}
		return actions.get(0);
	}

	public void nextAction() {
		if(!actions.isEmpty()) {
			actions.remove(actions.get(0));	
		}
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

	public void addWaitAction(PointInt2D point) {
		ActionData data = new ActionData();
		data.point = point;
		actions.add(new PlanningStep<ActionData>(PlanningAction.WAIT, data));
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

