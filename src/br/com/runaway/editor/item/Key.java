package br.com.runaway.editor.item;

import br.com.vite.tile.layer.ImageTileObject;

public class Key extends ImageTileObject {

	public Key() {
		super("item/key.png");
		
		offsetX = 2;
		offsetY = -10;
		
		this.label = "KEY";
	}

}
