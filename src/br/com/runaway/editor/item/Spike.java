package br.com.runaway.editor.item;

import br.com.vite.tile.layer.ImageTileObject;

public class Spike extends ImageTileObject {

	public Spike() {
		super("item/spike.png");
		
		offsetY = -6;
		
		this.label = "SPIKE";
	}

}
