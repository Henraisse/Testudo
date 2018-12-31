package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JFrame;

import mechanics.World;




public class TFrame extends JFrame implements KeyListener, MouseListener, MouseMotionListener{

	CanvasPanel canvas;
	IOModule io;
	
	public TFrame(World world, IOModule io) {
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.io = io;
		canvas = new CanvasPanel(world);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setSize(1200, 700);
		getContentPane().add(canvas);
		setVisible(true);		
		
	}
	
	

	@Override
	public void keyPressed(KeyEvent e) {
		io.keyPressed(e);	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		io.keyReleased(e);		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		io.keyTyped(e);		
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		io.mouseClicked(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		io.mouseEntered(e);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		io.mouseExited(e);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		io.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		io.mouseReleased(e);
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		io.mouseDragged(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		io.mouseMoved(e);
		
	}
	
	
}
