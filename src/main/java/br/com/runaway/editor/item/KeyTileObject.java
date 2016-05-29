package br.com.runaway.editor.item;

import br.com.vite.tile.layer.ImageTileObject;

public class KeyTileObject extends ImageTileObject {

	public KeyTileObject(String tileSetId) {
		super("item/key.png", tileSetId);
		
		offsetX = 18;
		offsetY = -10;
		
		this.label = "KEY";
	}

}
