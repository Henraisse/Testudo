import toolbox.Position;
import gui.TFrame;
import mechanics.World;

public class Main {

	public static void main(String[] args) {
		
		World world = new World();
		TFrame frame = new TFrame(world);
		
		while(true) {
			world.simstep();
			frame.repaint();
			try {Thread.sleep(5);} catch (InterruptedException e) {e.printStackTrace();}
		}
		
	}

}
