package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import gui.IOModule;
import toolbox.Position;

public class Frontline {

	
	Color red = Color.red;
	Color green = Color.green;
	Color white = Color.white;
	Color orange = Color.orange;
	Color blue = Color.blue;
	

	Position nullVector = new Position(0,0);
	
	Position front = new Position(500, 300);					//Front center position
	Position focal = new Position(700, 50);
	ArrayList<Unit> units = new ArrayList<Unit>();
	double angle = 22.5;
	int width = 100;

	
	public static double sideForceMultiplier = 100;
	public static double frontLineForceMultiplier = 3.00;
	
	public static double focalForceMultiplier = 0.1;
	
	public static final double speedVectorDecay = 0.8;
	public static final double unitAccelerationFactor = 0.1;
	
	public static final int NoCollisionCooldownMAX = 5;
	public static final double noCollisionDecay = 0.50;
	
	public static double forceConstant = 0.0155;
	public static int unitcount = 100;
	static int UNIT_WIDTH = 18;
	static double MIN_AREA_FACTOR = 1.2;
	
	public static double dragForceConstant = 10.0;
	public static double dragForceAreaMultiplier = 25.0;
	
	boolean invertedArc = false;
	
	public Frontline() {
		for(int i = 0; i < unitcount; i++){
			Position p = getNextSpawnPosition();
			Unit newunit = new Unit(p, this);
			units.add(newunit);
		}
	}
	

	

	
	
	public void simstep(World w, IOModule io) {
		alterParams(io);
		
		for(Unit u1: units){
			for(Unit u2: units){
				if(u1 != u2){
					u1.push(u2);
				}
			}
		}
		for(Unit u1: units){
			for(Unit u2: w.units){
				if(u1 != u2){
					u1.push(u2);
				}
			}
		}
		
		if(io.dragged) {
			for(Unit u: units){
				dragUnit(u, io);
			}
			io.dragged = false;
		}
		
		for(Unit u: units) {
			repelFocal(u, io);
			repelFocalCore(u);
		}
		for(Unit u: units) {
			repelFrontline(u);
		}				
		for(Unit u: units) {
			repelSides(u);
		}						
		for(Unit u: units){
			u.move();
		}		
		

	}
	
	

	
	private void dragUnit(Unit u, IOModule io) {
		double distance = u.position.distance(new Position(io.mousePosX, io.mousePosY))*(1.0/dragForceAreaMultiplier);
		double force = dragForceConstant/(distance*distance);
		Position forceVector = io.draggedVector.times(force);
		u.pushForce.add(forceVector);
	}



	public void invertArc() {
		invertedArc = !invertedArc;
		focal = focal.times(-1.0);
	}
	
	
	
	private void alterParams(IOModule io) {		
		if((io.combos[0] && !invertedArc) || (io.combos[1] && invertedArc)) {	
			double focallength = focal.length();
			focal = focal.normalize();
			focal = focal.times(focallength * 1.035);
			double focallength2 = focal.length();
			if(focal.length() > 50000) {
				invertArc();
			}
			angle = angle*(focallength/focallength2);
			
		}else if((io.combos[1] && !invertedArc) || (io.combos[0] && invertedArc)) {
			double focallength = focal.length();
			double nextFocallength = focallength * 0.965;
			double nextArea = nextFocallength * nextFocallength * Math.PI * 2*angle/360.0;
			
			if(nextArea > MIN_AREA_FACTOR*(UNIT_WIDTH/2.0)*(UNIT_WIDTH/2.0)*Math.PI*unitcount) {
				focal = focal.normalize();
				focal = focal.times(focallength * 0.965);
				double focallength2 = focal.length();
				angle = angle*(focallength/focallength2);
			}						
		}
		else if(io.combos[2]) {
			angle += 0.01*angle;
			if(angle > 180.0) {
				angle = 180.0;
			}
		}else if(io.combos[3]) {
			double focallength = focal.length();
			double nextArea = focallength * focallength * Math.PI * 2*(angle*0.99)/360.0;
			if(nextArea > MIN_AREA_FACTOR*(UNIT_WIDTH/2.0)*(UNIT_WIDTH/2.0)*Math.PI*unitcount) {
				angle -= 0.01*angle;
				if(angle < 0 ) {angle = 0;}
			}
			
		}
	}



	private void repelSides(Unit u) {
		double angle = sidelineOverstepAngle(u.position);
		while(angle < 0) {
			angle+=360.0;
		}
		
		
		
		if((angle < 180 && angle > this.angle)){
			double ap = (angle-this.angle)*sideForceMultiplier;
			double aa = ap*ap;
			
			Position forceVector = getRightSideLineVector().normalize().times(aa);
			forceVector.rotateSimple(-90);
			
			if(forceVector.length() > 10) {
				forceVector = forceVector.normalize().times(10);
			}

			u.pushForce.add(forceVector);
		}
		
		
		
		
		if((angle > 180 && angle < 360-this.angle)){
			
			double ap = (angle-(360-this.angle))*sideForceMultiplier;
			double aa = ap*ap;
			
			Position forceVector = getLeftSideLineVector().normalize().times(aa);
			forceVector.rotateSimple(90);
			
			if(forceVector.length() > 10) {
				forceVector = forceVector.normalize().times(10);
			}

			u.pushForce.add(forceVector);
		}
		
		
	}



	private void repelFocal(Unit u, IOModule io) {
		Position focalabs = focal.plus(front);
		Position p = u.position.minus(focalabs);
		p = p.times(focalForceMultiplier/p.length());
		if(u.noCollisionCooldown > 10) {
			p = p.times(1.0/u.noCollisionCooldown);
			u.noCollisionCooldown = (int)(noCollisionDecay*u.noCollisionCooldown);
		}else {
			if(!u.hasCollided || io.combos[4]) {
				p = p.times(10.0);
			}
		}
		if(p.length() > 10) {
			p = p.normalize().times(10);
		}
		
		if(frontlineOverstepLength(u.position) < 0) {			
			if(!invertedArc) {
				u.pushForce.add(p);
			}		
		}else {
			if(invertedArc){
				u.pushForce.subtract(p);
			}
		}
	}


	
	private void repelFocalCore(Unit u) {
		Position focalabs = focal.plus(front);
		Position towardCenter = focalabs.minus(u.position);
		if(focalabs.distance(u.position) < focal.length()/2.0 && towardCenter.unitDotProduct(u.speedVector) > 0) {
			u.speedVector = new Position(0,0).minus(u.speedVector).times(2);
		}
	}
	
	

	private void repelFrontline(Unit u) {
		double f = frontlineOverstepLength(u.position)*frontLineForceMultiplier;
		Position focalabs = focal.plus(front);
		Position frontrepel = u.position.minus(focalabs);
		
		frontrepel = frontrepel.times(-1.0/(frontrepel.length()));
		frontrepel = frontrepel.times(f*f);
		if(frontrepel.length() > 10) {
			frontrepel = frontrepel.normalize().times(10);
		}
		if(f > 0) {
			
			if(!invertedArc) {
				u.pushForce.add(frontrepel);	
			}						
		}else {
			if(invertedArc) {
				u.pushForce.subtract(frontrepel);	
			}
		}
	}



	public void repaint(Graphics g) {		
		Position focal = getAbsoluteFocalPosition();
		Position leftBoundaryAbsolute = getLeftSideLineVector().plus(focal);
		Position rightBoundaryAbsolute = getRightSideLineVector().plus(focal);
		
		//Draw front-center position
		//paintOval(front, red, 15, g, true);
		//drawLine(front, nullVector, red, g);
		
		//Draw focal position
		//paintOval(focal, green, 15, g, true);
		//drawLine(this.focal, front, green, g);
		
		//Draw left boundary-vector
		Color left_color = blue;
		Color right_color = orange;
		Color inside_color = white;
		Color outside_color = red;
		if(!invertedArc) {
			left_color = orange;
			right_color = blue;
			inside_color = red;
			outside_color = white;
		}
		paintOval(leftBoundaryAbsolute, left_color, 15, g, true);
		drawLine(getLeftSideLineVector(), focal, left_color, g);
		drawLine(getLeftSideLineVector(), focal.plus(getLeftSideLineVector()), left_color, g);
		

		//Draw left boundary-vector
		paintOval(rightBoundaryAbsolute, right_color, 15, g, true);
		drawLine(getRightSideLineVector(), focal, right_color, g);
		drawLine(getRightSideLineVector(), focal.plus(getRightSideLineVector()), right_color, g);
		
		//Draw front boundary circle
		paintOval(focal, white, (int)this.focal.length()*2, g, false);
		
		//int c = 0;
		for(Unit u: units) {
			//c++;
			//g.drawString(Integer.toString(c), (int)u.position.x, (int)u.position.y);
			
			double angle = sidelineOverstepAngle(u.position);
			while(angle < 0) {
				angle+=360.0;
			}
			if((angle > 180 && angle < 360-this.angle)) {
				paintOval(u.position, left_color, UNIT_WIDTH, g, false);
//				Position vec = getLeftSideLineVector().normalize().times(125);
//				vec.rotateSimple(90);
//				drawLine(vec, u.position, left_color, g);
			}else if((angle < 180 && angle > this.angle)) {
//				Position vec = getRightSideLineVector().normalize().times(125);
//				vec.rotateSimple(-90);
//				drawLine(vec, u.position, right_color, g);
				paintOval(u.position, right_color, UNIT_WIDTH, g, false);
			}else if(frontlineOverstepLength(u.position) > 0) {
				paintOval(u.position, outside_color, UNIT_WIDTH, g, false);
			}else {
				paintOval(u.position, inside_color, UNIT_WIDTH, g, false);
			}						
			paintOval(u.position, red, 2, g, false);
		}
		
	}
	
	
	
	
	//********************************************** VECTOR MANIPULATION *******************************************************
	
	/**
	 * Returns the distance the frontline has been overstepped.
	 * This corresponds basically to the unit distance from the focal point minus the distance between the front and focal positions.
	 * @return
	 */
	public double frontlineOverstepLength(Position p) {
		return p.minus(focal.plus(front)).length() - focal.length();
	}
	
	/**
	 * Returns the distance the left sideline has been overstepped.
	 * This corresponds, basically, to the angle between the unit and the front-center, measured from the focal.
	 * @param p - any absolute position
	 * @return
	 */
	public double sidelineOverstepAngle(Position p) {
		Position front_focal = getFrontRelativeFocal();	//
		Position p_focal = getFocalRelativePosition(p);
		
		double angle = front_focal.angle(p_focal);
		return angle;		
	}
	
	/**
	 * Returns the position of the front-center relative to the focal.
	 * @return
	 */
	public Position getFrontRelativeFocal() {
		return new Position(0,0).minus(focal);
	}
	
	/**
	 * Returns any position, seen relative to the focal.
	 * @param p
	 * @return
	 */
	public Position getFocalRelativePosition(Position p) {
		return p.minus(front.plus(focal));
	}
	
	
	
	public Position getAbsoluteFocalPosition() {
		return focal.plus(front);
	}
	
	
	/**
	 * Return the left boundary vector.
	 * @return
	 */
	public Position getLeftSideLineVector() {
		Position p = getFrontRelativeFocal().clone();
		p.rotateSimple(-angle);
		return p;
	}
	
	/**
	 * Return the right boundary vector.
	 * @return
	 */
	public Position getRightSideLineVector() {
		Position p = getFrontRelativeFocal().clone();
		p.rotateSimple(angle);
		return p;
	}
	
	
	
	
	
	
	//******************************************************************* ONE-OFF, UNCHANGING METHODS ******************************************************************
	int row = 2;
	int rowOffset = 0;
	int side = -1;
	public Position getNextSpawnPosition() {
		//utgå ifrån front-center
		Position p = getFrontRelativeFocal();
		
		//subtrahera från längden row*UNIT_WIDTH*front-center.normalized
		Position p0 = p.minus(p.times((row*(UNIT_WIDTH+Unit.UNIT_SPAWN_PADDING))/p.length()));
		double length = p0.length();
				
		//beräkna sidovinkel för en enhet på detta avstånd
		double x = UNIT_WIDTH + Unit.UNIT_SPAWN_PADDING;
		double sideStepAngle_1 = 360*(x/(length*2*Math.PI));
		
		//beräkna totalvinkel baserat på tidigare utplaceringar
		double angle = sideStepAngle_1*side*rowOffset;
		
		//Om fler inte får plats, börja om på ny rad, nollställ allt
		if(Math.abs(angle) > this.angle) {
			row++;
			angle = 0;
			side = -1;
			p0 = p.minus(p.times((row*(UNIT_WIDTH+Unit.UNIT_SPAWN_PADDING))/p.length()));
			rowOffset = 1;
		}
		//annars, rotera den åt något håll motsvarande avståndsbredd*rowOffset*side
		else {
			//beräkna positionen
			p0.rotateSimple(angle);
			//toggla side
			if(rowOffset == 0) {
				rowOffset++;
				
			}
			else {
				side*=-1;
				if(side == -1) {
					rowOffset++;
				}
			}
			
		}				
		//gör positionen global (genom att lägga till absolute-focal)
		p0 = p0.plus(front).plus(focal);
		return p0;
	}
	
	
	
	
	public static void paintOval(Position p, Color c, int size, Graphics g, boolean fill) {
		g.setColor(c);
		int x = (int)p.x;
		int y = (int)p.y;
		if(fill) {
			g.fillOval(x-(size/2), y-(size/2), size, size);
		}else {
			g.drawOval(x-(size/2), y-(size/2), size, size);
		}
		
	}
	public void drawLine(Position vector, Position relativeTo, Color c, Graphics g) {
		int x1 = (int)relativeTo.x;
		int y1 = (int)relativeTo.y;
		
		int x2 = (int)(relativeTo.x + vector.x);
		int y2 = (int)(relativeTo.y + vector.y);
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
	}
	
	
	
	




	

}
