
import br.com.etyllica.EtyllicaFrame;
import br.com.etyllica.core.context.Application;
import br.com.runaway.editor.MapEditorApplication;


public class RunawayMapEditor extends EtyllicaFrame {

	private static final long serialVersionUID = 1L;

	public RunawayMapEditor() {
		super(1024, 768);
	}
	
	public static void main(String[] args){
		RunawayMapEditor map = new RunawayMapEditor();
		map.init();
	}

	public Application startApplication() {
		
		String s = RunawayMapEditor.class.getResource("").toString();
		setPath(s+"../");
		
		return new MapEditorApplication(w, h);
	}
	
}
