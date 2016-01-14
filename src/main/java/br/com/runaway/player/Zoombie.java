package br.com.runaway.player;

import br.com.tide.action.player.ActionPlayerListener;

public class Zoombie extends PlanningPlayer {

	public Zoombie(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener, "monster/zombie_walk.png");
		turnSpeed = 1;
		currentSpeed = 2;
	}
}

