package br.com.runaway.player;

import br.com.tide.action.player.ActionPlayerListener;

public class RedSuitHuman extends Hero {
	
	public RedSuitHuman(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener);
		bodyLayer.setPath("player/red_team.png");
		
		turnSpeed = 5;
		currentSpeed = 2;
	}
}

