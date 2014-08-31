
import br.com.etyllica.EtyllicaFrame;
import br.com.etyllica.context.Application;
import br.com.runaway.GameApplication;


public class Runaway extends EtyllicaFrame {

	public Runaway() {
		super(800, 600);
	}
	
	public static void main(String[] args){
		Runaway map = new Runaway();
		map.init();
	}

	public Application startApplication() {
		
		String s = Runaway.class.getResource("").toString();
		setPath(s+"../");
		
		return new GameApplication(w, h);
	}
	
}
