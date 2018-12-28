package gui;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import mechanics.World;

public class CanvasPanel extends JPanel{
	
	World world;

	
	
	public CanvasPanel(World world) {
		this.world = world;
		setBackground(Color.BLACK);
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		world.repaint(g);
	}
	
	
}
