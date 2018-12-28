package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import toolbox.Position;

public class Frontline {

	
	Position position = new Position(400, 250);
	Position focal = position.plus(new Position(200, 0));
	ArrayList<Unit> units = new ArrayList<Unit>();
	double angle = 5.0;
	int width = 100;

	

	public Frontline() {
		for(int i = 0; i < 100; i++){
			addUnit();
		}
		//spawna alla units
	}
	
	
	
	public void addUnit() {
		Unit newunit = new Unit(position, focal.x, width, this);
		units.add(newunit);
		
		
	}
	
	public void simstep() {
		for(Unit u1: units){
			for(Unit u2: units){
				if(u1 != u2){
					u1.push(u2);
				}
			}
		}
		
		for(Unit u: units){
			u.move(focal);
		}
		//för varje unit:
			//accelerera den ifrån fokalpunkten (men bara till en viss maxhastighet)
			//placera en movement-barriär vid frontlinjen, och en given vinkel utifrån normallinjen
		
		
		
	}



	public void repaint(Graphics g) {
		for(Unit u : units){
			u.paint(g);
		}
		Position p0 = position.plus(focal);
		g.setColor(Color.green);
		g.fillOval((int)p0.x-10, (int)p0.y-10, 20, 20);
		
		Position p = position;
		g.setColor(Color.green);
		g.fillOval((int)p.x-5, (int)p.y-5, 10, 10);
		
		int size = (int) focal.length();
		
		g.drawOval((int)p0.x-(size), (int)p0.y-(size), size*2, size*2);
		
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
}
