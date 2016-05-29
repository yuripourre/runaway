package br.com.runaway.menu;

import br.com.etyllica.core.context.Application;
import br.com.etyllica.core.event.Action;
import br.com.etyllica.core.event.GUIEvent;
import br.com.etyllica.core.graphics.Graphics;
import br.com.etyllica.gui.Button;
import br.com.etyllica.gui.label.TextLabel;
import br.com.etyllica.layer.ImageLayer;
import br.com.runaway.MazeMode;
import br.com.runaway.SurvivalMode;
import br.com.runaway.editor.MapEditorApplication;

public class MainMenu extends Application {

	private ImageLayer background;

	private ImageLayer title;
	
	private static final int INITIAL_LEVEL = 1;
	
	public MainMenu(int w, int h) {
		super(w, h);
		
		loadApplication = new RunawayLoading(x, y, w, h);
	}

	public void doOpenGame() {
		nextApplication = new MazeMode(w, h, INITIAL_LEVEL);
	}
	
	public void doOpenSurvivalGame() {
		nextApplication = new SurvivalMode(w, h, INITIAL_LEVEL+5);
	}

	public void doOpenEditor() {
		nextApplication = new MapEditorApplication(w, h);
	}

	@Override
	public void load() {

		background = new ImageLayer("menu/background.jpg");

		title = new ImageLayer(0, 60, "title.png");
		title.centralizeX(this);

		int buttonWidth = 200;

		Button playButton = new Button(w/2-buttonWidth/2, 260, buttonWidth, 60);
		playButton.setLabel(new TextLabel("Novo Jogo"));
		playButton.addAction(GUIEvent.MOUSE_LEFT_BUTTON_UP, new Action(this,  "doOpenGame"));
		
		Button survivalButton = new Button(w/2-buttonWidth/2, 340, buttonWidth, 60);
		survivalButton.setLabel(new TextLabel("Modo Survival"));
		survivalButton.addAction(GUIEvent.MOUSE_LEFT_BUTTON_UP, new Action(this,  "doOpenSurvivalGame"));

		Button editorButton = new Button(w/2-buttonWidth/2, 420, buttonWidth, 60);
		editorButton.setLabel(new TextLabel("Editor de Mapas"));
		editorButton.addAction(GUIEvent.MOUSE_LEFT_BUTTON_UP, new Action(this,  "doOpenEditor"));

		addView(playButton);
		addView(survivalButton);
		addView(editorButton);

		loading = 100;
	}

	@Override
	public void draw(Graphics g) {
		background.draw(g);
		title.draw(g);
	}

}
