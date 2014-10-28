
import br.com.etyllica.EtyllicaFrame;
import br.com.etyllica.context.Application;
import br.com.runaway.menu.MainMenu;


public class TombRunaway extends EtyllicaFrame {

	private static final long serialVersionUID = 1L;
	
	public TombRunaway() {
		super(800, 600);
	}
	
	public static void main(String[] args){
		TombRunaway map = new TombRunaway();
		map.init();
	}

	public Application startApplication() {
		
		String s = TombRunaway.class.getResource("").toString();
		setPath(s+"../");
		
		return new MainMenu(w, h);
	}
	
}
