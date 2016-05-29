package br.com.runaway.editor.item;

import br.com.vite.tile.layer.ImageTileObject;

public class Hole extends ImageTileObject {

	public Hole(String tileSetId) {
		super("item/hole.png", tileSetId);
		
		this.label = "HOLE";
	}

}
