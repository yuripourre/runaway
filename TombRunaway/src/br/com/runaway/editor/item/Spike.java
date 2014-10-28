package br.com.runaway.editor.item;

import br.com.vite.tile.layer.ImageTileObject;

public class Spike extends ImageTileObject {

	public Spike() {
		super("item/spike.png");
		
		offsetX = 16;
		offsetY = -2;
		
		this.label = "SPIKE";
	}

}
