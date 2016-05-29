package br.com.runaway.trap;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.ImageLayer;
import br.com.runaway.player.TopViewPlayer;

public class Explosive extends Trap {
	
	private TopViewPlayer owner;
	private ImageLayer layer;
	
	private static final int DELAY = 2200;
	
	public Explosive(TopViewPlayer owner) {
		super();
		
		this.owner = owner;
		
		int x = owner.getCenter().getX();
		int y = owner.getCenter().getY();
						
		int width = 32;
		setBounds(x-width/2, y-width/2, width, width);
		
		layer = new ImageLayer("traps/bomb.png");
		layer.setBounds(x-width/2, y-width/2, width, width);
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		layer.draw(g, x, y);
	}
	
	public void update(long now) {
		if(!started) {
			started = true;
			activeTime = now;
		}
		if(!active) {
			if(now > activeTime + DELAY) {
				layer.setYImage(32);
				active = true;
			}	
		}
		
	}
	
	@Override
	public void offset(int x, int y) {
		super.offset(x, y);
		layer.setOffset(x, y);		
	}

	public TopViewPlayer getOwner() {
		return owner;
	}
	
}
