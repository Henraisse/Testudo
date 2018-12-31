package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import gui.IOModule;
import toolbox.Position;

public class World {

	ArrayList<Unit> units = new ArrayList<Unit>();
	
	Frontline testline = new Frontline();
	IOModule io;
	
	public static double FRONT_STEPSIZE = 1.25;
	public static double FRONT_STEPANGLE = 0.25;
	
	public World(IOModule io) {
		this.io = io;
		
		for(int i = 0; i < 25; i++){
			int x = 250 + (i%5)*(Frontline.UNIT_WIDTH+3);
			int y = 250 + (i/5)*(Frontline.UNIT_WIDTH+3);
			Position p = new Position(x, y);
			Unit newunit = new Unit(p, null);
			units.add(newunit);
		}
		
	}
	
	
	
	public void simstep() {
		for(Unit u1: units){
			for(Unit u2: units){
				if(u1 != u2){
					u1.push(u2);
				}
			}
		}
		for(Unit u1: units){
			for(Unit u2: testline.units){
				if(u1 != u2){
					u1.push(u2);
				}
			}
		}
		
		moveTestLine(io);
		testline.simstep(this, io);
		for(Unit u: units){
			u.move();
		}
		
		fight(units, testline.units);
	}
	
	
	
	public void fight(ArrayList<Unit> units2, ArrayList<Unit> units3) {
		for(Unit u1: units2) {
			for(Unit u2: units3) {
				if(u1.position.distance(u2.position) < Frontline.UNIT_WIDTH) {
					Random random = new Random();
					if(random.nextFloat() > 0.99) {
						u1.isAlive = false;
					}
					if(random.nextFloat() > 0.99) {
						u2.isAlive = false;
					}
				}
			}
		}
		
		for(int i = 0; i < units2.size(); i++) {
			if(!units2.get(i).isAlive) {
				units2.remove(i);
				i--;
			}
		}
		for(int i = 0; i < units3.size(); i++) {
			if(!units3.get(i).isAlive) {
				units3.remove(i);
				i--;
			}
		}
	}



	public void repaint(Graphics g) {
		for(Unit u: units) {
			Frontline.paintOval(u.position, Color.cyan, Frontline.UNIT_WIDTH, g, false);
			Frontline.paintOval(u.position, Color.cyan, 2, g, false);
		}						
		
		
		
		testline.repaint(g);
	}

	
	public void moveTestLine(IOModule io) {
		Position forwardVector = testline.getFrontRelativeFocal().times(FRONT_STEPSIZE/testline.getFrontRelativeFocal().length());
		if(io.keys_down[87] && !io.keys_down[17]) {
			testline.front.add(forwardVector);
			for(Unit u: testline.units) {
				u.position.add(forwardVector.times(1.0));
			}
		}
		if(io.keys_down[83] && !io.keys_down[17]) {
			testline.front.subtract(forwardVector);
		}
		
		if(io.keys_down[68] && !io.keys_down[17]) {
			Position axis = testline.getRightSideLineVector().plus(testline.focal.plus(testline.front));
			testline.front.rotateAroundAxis(FRONT_STEPANGLE, axis);
			testline.focal.rotateAroundAxis(FRONT_STEPANGLE, new Position(0,0));			
			for(Unit u: testline.units) {
				double distanceFromRotAxis = u.position.distance(axis);
				double rotationalCircumference = distanceFromRotAxis*2*Math.PI;
				double maxDistance = 1.5;
				double maxAngle = (maxDistance/rotationalCircumference)*360.0;
				if(maxAngle < FRONT_STEPANGLE) {
					u.position.rotateAroundAxis(maxAngle, axis);
				}else {
					u.position.rotateAroundAxis(FRONT_STEPANGLE, axis);
				}
			}
		}
		
		if(io.keys_down[65] && !io.keys_down[17]) {
			Position axis = testline.getLeftSideLineVector().plus(testline.focal.plus(testline.front));
			testline.front.rotateAroundAxis(-FRONT_STEPANGLE, axis);
			testline.focal.rotateAroundAxis(-FRONT_STEPANGLE, new Position(0,0));			
			for(Unit u: testline.units) {
				double distanceFromRotAxis = u.position.distance(axis);
				double rotationalCircumference = distanceFromRotAxis*2*Math.PI;
				double maxDistance = 1.5;
				double maxAngle = (maxDistance/rotationalCircumference)*360.0;
				if(maxAngle < FRONT_STEPANGLE) {
					u.position.rotateAroundAxis(-maxAngle, axis);
				}else {
					u.position.rotateAroundAxis(-FRONT_STEPANGLE, axis);
				}			
			}
		}
	}
	

	
	
	
	
	
	
	
	
	
	
	
}
