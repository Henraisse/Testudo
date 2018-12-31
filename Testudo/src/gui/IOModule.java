package gui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import toolbox.Position;

public class IOModule{
	
	public boolean[] keys_down = new boolean[1000];
	public boolean[] combos = new boolean[10];
	public int mousePosX = -1;
	public int mousePosY = -1;
	public boolean leftMouseDown = false;
	public boolean dragged = false;
	public Position draggedVector = new Position(0,0);
	
	public IOModule() {
	}
	
	public void keyPressed(KeyEvent e) {
		keys_down[e.getKeyCode()] = true;
		updateCombos();
		//System.out.println(e.getKeyCode() + " char=" + e.getKeyChar());		
	}
	
	
	public void keyReleased(KeyEvent e) {
		keys_down[e.getKeyCode()] = false;
		updateCombos();
	}

	
	public void keyTyped(KeyEvent e) {
	}

	
	private void updateCombos() {
		combos[0] = checkCombination(new int[]{17, 87}); if(combos[0]) {return;}
		combos[1] = checkCombination(new int[]{17, 83}); if(combos[1]) {return;}
		combos[2] = checkCombination(new int[]{17, 68}); if(combos[2]) {return;}
		combos[3] = checkCombination(new int[]{17, 65}); if(combos[3]) {return;}
		combos[4] = checkCombination(new int[]{81});	 if(combos[4]) {return;}
		
		combos[5] = checkCombination(new int[]{87});	 if(combos[5]) {return;}
		combos[6] = checkCombination(new int[]{83});	 if(combos[6]) {return;}
		combos[7] = checkCombination(new int[]{68});	 if(combos[7]) {return;}
		combos[8] = checkCombination(new int[]{65});	 if(combos[8]) {return;}
	}
	
	

	
	
	
	
	
	
	public boolean checkCombination(int[] combo) {
		for(int i: combo) {
			if(keys_down[i] == false) {
				return false;
			}
		}
		return true;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1) {
			leftMouseDown = true;
			mousePosX = e.getX();
			mousePosY = e.getY();
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		draggedVector = new Position(e.getX() - mousePosX, e.getY() - mousePosY);
		dragged = true;
		mousePosX = e.getX();
		mousePosY = e.getY();				
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	
	
	
	
}
