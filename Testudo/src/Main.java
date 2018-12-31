import toolbox.Position;
import gui.IOModule;
import gui.TFrame;
import mechanics.World;

public class Main {

	public static void main(String[] args) {
		
		IOModule io = new IOModule();
		World world = new World(io);		
		TFrame frame = new TFrame(world, io);
		
		
		
		while(true) {
			world.simstep();
			frame.repaint();
			try {Thread.sleep(1000/100);} catch (InterruptedException e) {e.printStackTrace();}
			
			
		}
		
	}

}
