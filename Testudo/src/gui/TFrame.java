package gui;

import javax.swing.JFrame;

import mechanics.World;




public class TFrame extends JFrame{

	CanvasPanel canvas;
	
	public TFrame(World world) {		
		canvas = new CanvasPanel(world);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1200, 700);
		getContentPane().add(canvas);
		setVisible(true);		
		
	}
	
	

	
}
