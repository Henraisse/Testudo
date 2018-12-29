package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import toolbox.Position;

public class Unit {

	public static final int UNIT_SPAWN_PADDING = 2;
	Frontline front;
	static int UNIT_WIDTH = 10;
	static double forceConstant = 0.0075;
	Position position;
	Position speedVector = new Position(0,0);	
	Position pushForce = new Position(0,0);
	
	public Unit(Position p, Frontline frontline){
		front = frontline;
		position = p;

	}
	

	public void push(Unit u2) {
		Position p1 = this.position;
		Position p2 = u2.position;
		if(p1.minus(p2).length() < UNIT_WIDTH){
			Position springVector = p1.minus(p2);
			double compression = Math.abs((UNIT_WIDTH*UNIT_WIDTH) - (springVector.length()*springVector.length()));
			double repulsionForce = compression*forceConstant;
			Position repulsionVector = springVector.times(repulsionForce);
			pushForce.add(repulsionVector);
		}		
	}
	
	
	public void move() {
		speedVector.add(pushForce);		
		position.add(speedVector.times(0.1));
		speedVector = speedVector.times(0.5);
		pushForce = new Position(0,0);
		
	}
	
	

	
	
}
