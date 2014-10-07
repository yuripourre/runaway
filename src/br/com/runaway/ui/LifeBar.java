package br.com.runaway.ui;

import java.awt.Color;

import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.core.graphics.SVGColor;

public class LifeBar {

	private int offsetX = 40;

	private int offsetY = 50;

	public void draw(Graphic g, int currentLife, int totalLifes) {

		int radius = 20;
		
		int spacing = 5;
		
		for(int i = 0; i < totalLifes; i++) {

			if(i < currentLife) {
				g.setColor(SVGColor.CRIMSON);
				g.fillCircle(offsetX+i*(2*radius+spacing), offsetY, radius);
			}
			
			g.setColor(Color.BLACK);
			g.drawCircle(offsetX+i*(2*radius+spacing), offsetY, radius);
			
		}

	}

}
