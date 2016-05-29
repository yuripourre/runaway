
import br.com.etyllica.Etyllica;
import br.com.etyllica.core.context.Application;
import br.com.runaway.menu.MainMenu;


public class TombRunaway extends Etyllica {

	private static final long serialVersionUID = 1L;
	
	public TombRunaway() {
		super(800, 600);
	}
	
	public static void main(String[] args){
		TombRunaway map = new TombRunaway();
		map.setTitle("Tomb Game");
		map.init();
	}

	public Application startApplication() {
		initialSetup("../");
		
		/*JoystickLoader.getInstance().start(1);
		new Thread(JoystickLoader.getInstance()).start();*/
		
		return new MainMenu(w, h);
	}
	
}
