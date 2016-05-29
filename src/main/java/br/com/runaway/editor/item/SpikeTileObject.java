package br.com.runaway.editor.item;

import br.com.vite.tile.layer.ImageTileObject;

public class SpikeTileObject extends ImageTileObject {

	public SpikeTileObject(String tileSetId) {
		super("item/spike.png", tileSetId);
		
		offsetX = 16;
		offsetY = -2;
		
		this.label = "SPIKE";
	}

}
