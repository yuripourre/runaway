package br.com.runaway.menu;

import br.com.etyllica.context.Application;
import br.com.etyllica.core.event.Action;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.graphics.Graphic;
import br.com.etyllica.gui.Button;
import br.com.etyllica.gui.label.TextLabel;
import br.com.etyllica.layer.ImageLayer;
import br.com.runaway.GameApplication;
import br.com.runaway.editor.MapEditorApplication;

public class MainMenu extends Application {

	private ImageLayer background;

	private ImageLayer title;	

	public MainMenu(int w, int h) {
		super(w, h);
	}

	public void doOpenGame() {

		//Always start from first level
		session.put(GameApplication.PARAM_LEVEL, 2);
		nextApplication = new GameApplication(w, h);
	}

	public void doOpenEditor() {
		nextApplication = new MapEditorApplication(w, h);
	}

	@Override
	public void load() {

		background = new ImageLayer("menu/background.png");

		title = new ImageLayer(0, 60, "title.png");
		title.centralizeX(this);

		int buttonWidth = 200;

		Button playButton = new Button(w/2-buttonWidth/2, 300, buttonWidth, 60);
		playButton.setLabel(new TextLabel("Novo Jogo"));
		playButton.addAction(GUIEvent.MOUSE_LEFT_BUTTON_UP, new Action(this,  "doOpenGame"));

		Button editorButton = new Button(w/2-buttonWidth/2, 380, buttonWidth, 60);
		editorButton.setLabel(new TextLabel("Editor de Mapas"));
		editorButton.addAction(GUIEvent.MOUSE_LEFT_BUTTON_UP, new Action(this,  "doOpenEditor"));

		add(playButton);
		add(editorButton);

		loading = 100;
	}

	@Override
	public void draw(Graphic g) {
		background.draw(g);
		title.draw(g);
	}

}
