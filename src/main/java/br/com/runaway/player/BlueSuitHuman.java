package br.com.runaway.player;

import br.com.tide.action.player.ActionPlayerListener;

public class BlueSuitHuman extends Hero {
	
	public BlueSuitHuman(int x, int y, ActionPlayerListener<TopViewPlayer> listener) {
		super(x, y, listener);
		bodyLayer.setPath("player/blue_team.png");
		
		turnSpeed = 5;
		currentSpeed = 2;
	}
}

