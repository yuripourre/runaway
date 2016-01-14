package br.com.runaway.item;

import br.com.runaway.player.Hero;


public class MedKit extends Item {

	public MedKit(int x, int y) {
		super(x, y, "item/medkit.png");
	}

	public void act(Hero player, long now) {
		player.gainLife(now);
	}
}
