package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import toolbox.Position;

public class Unit {

	Frontline front;
	static int UNIT_WIDTH = 10;
	Position position;
	
	Position actualPosition;
	
	Position pushForce = new Position(0,0);
	
	public Unit(Position p, double focal, int width, Frontline frontline){
		front = frontline;
		if(focal > 100){
			Random rand = new Random();
			int x = rand.nextInt(100)-50;
			int y = rand.nextInt(100)-50;
			position = new Position(0, 50).plus(p).plus(new Position(x, y));
			actualPosition = position.clone();
		}
	}
	
	
		
	public void paint(Graphics g){
		Position position = actualPosition;
		g.setColor(Color.red);
		g.drawLine((int)position.x, (int)position.y, (int)position.x, (int)position.y);
		g.drawOval((int)position.x-(UNIT_WIDTH/2), (int)position.y-(UNIT_WIDTH/2), UNIT_WIDTH, UNIT_WIDTH);
	}



	public void push(Unit u2) {
		Position p1 = this.position;
		Position p2 = u2.position;
		if(p1.distance(p2) < UNIT_WIDTH){
			u2.pushForce.add(p2.minus(p1));
		}		
	}
	
	public void move(Position focal){
		Position movedist = this.pushForce.clone();
		if(pushForce.length() > 0.1){
			movedist.normalize();
			movedist = movedist.times(0.1);
		}
		
		//Håll alla kring frontlinjen
		if(position.plus(movedist).distance(focal.plus(front.position)) < focal.length()){
			movedist.subtract(front.position.minus(focal).normalize().times(0.2));
		}else{
			movedist.add(front.position.minus(focal).normalize().times(0.5));
		}
		
		//Håll alla innanför rätt vinkel:
		Position straight = new Position(0,0).minus(focal);
		Position offset = position.minus(front.position.plus(focal));
		if(Math.abs(straight.angle(offset)) > front.angle){
			double dir = straight.angle(offset)/Math.abs(straight.angle(offset));
			Position angled = straight.clone();
			angled.rotateSimple(-90*dir);
			Position smalloffset = angled.times(1.0/angled.length());
			movedist.add(smalloffset);
		}
		
		
		
		position.add(movedist);
		Position realDiff = position.minus(actualPosition);
		if(realDiff.length() > 1.5){
			actualPosition.add(realDiff.times(0.5));
		}
		pushForce = new Position(0,0);
	}
	
	
}
