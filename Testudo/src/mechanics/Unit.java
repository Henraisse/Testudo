package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import toolbox.Position;

public class Unit {

	public static final int UNIT_SPAWN_PADDING = 2;
	Frontline front;
	
	boolean isAlive = true;
	
	Position position;
	Position speedVector = new Position(0,0);	
	Position pushForce = new Position(0,0);
	boolean hasCollided = false;
	int noCollisionCooldown = 0;
	
	public Unit(Position p, Frontline frontline){
		front = frontline;
		position = p;

	}
	

	public void push(Unit u2) {
		Position p1 = this.position;
		Position p2 = u2.position;
		if(p1.minus(p2).length() < Frontline.UNIT_WIDTH){
			Position springVector = p1.minus(p2);
			double compression = Math.abs((Frontline.UNIT_WIDTH*Frontline.UNIT_WIDTH) - (springVector.length()*springVector.length()));
			double repulsionForce = compression*Frontline.forceConstant;
			Position repulsionVector = springVector.times(repulsionForce);
			pushForce.add(repulsionVector);
			noCollisionCooldown = Frontline.NoCollisionCooldownMAX;
			hasCollided = true;
		}		
	}
	
	
	public void move() {
		speedVector.add(pushForce);		
		position.add(speedVector.times(Frontline.unitAccelerationFactor));
		speedVector = speedVector.times(Frontline.speedVectorDecay);
		pushForce = new Position(0,0);		
	}
	
	

	
	
}
