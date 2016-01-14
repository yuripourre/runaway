package br.com.runaway.item;


public class Key extends Item {

	public Key(int x, int y) {
		super(x, y, "item/key.png");
	}
	
	@Override
	public boolean colide(int px, int py) {
		return layer.colideCirclePoint(px, py);
	}

}
