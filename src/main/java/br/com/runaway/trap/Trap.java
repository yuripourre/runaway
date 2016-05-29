package br.com.runaway.trap;

import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.layer.GeometricLayer;

public abstract class Trap extends GeometricLayer {
		
	protected int interval = 500;
	
	protected boolean active = false;
	protected boolean started = false;

	protected long activeTime = 0;
		
	public abstract void update(long now);

	public boolean isActive() {
		return active;
	}
	
	public void offset(int x, int y) {
		this.setLocation(x, y);
	}

	public void draw(Graphics g, int x, int y) {
		// TODO Auto-generated method stub
	}
}
