package mechanics;

import java.awt.Graphics;

public class World {

	Frontline testline = new Frontline();
	
	
	
	
	public World() {
		
		
		
		
	}
	
	
	
	public void simstep() {
		testline.simstep();
	}
	
	
	
	public void repaint(Graphics g) {
		testline.repaint(g);
	}

}
