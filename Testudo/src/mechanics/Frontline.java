package mechanics;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import toolbox.Position;

public class Frontline {

	Color red = Color.red;
	Color green = Color.green;
	Color white = Color.white;
	Color orange = Color.orange;
	Color blue = Color.blue;
	
	Position nullVector = new Position(0,0);
	
	Position front = new Position(300, 250);					//Front center position
	Position focal = new Position(5000, 50);
	ArrayList<Unit> units = new ArrayList<Unit>();
	double angle = 2;
	int width = 100;

	
	public Frontline() {
		for(int i = 0; i < 150; i++){
			Position p = getNextSpawnPosition();
			Unit newunit = new Unit(p, this);
			units.add(newunit);
		}
	}
	

	
	public void simstep() {
		for(Unit u: units) {
			repelFrontine(u);
		}
		
		for(Unit u: units) {
			repelFocal(u);
		}
		
		for(Unit u: units) {
			repelSides(u);
		}
		
		
		for(Unit u1: units){
			for(Unit u2: units){
				if(u1 != u2){
					u1.push(u2);
				}
			}
		}		
		for(Unit u: units){
			u.move();
		}		
	}
	
	
	public static double sideForceMultiplier = 1000;
	public static double frontLineForceMultiplier = 100;

	
	
	private void repelSides(Unit u) {
		double angle = sidelineOverstepAngle(u.position);
		while(angle < 0) {
			angle+=360.0;
		}
		if((angle < 180 && angle > this.angle)){
			double ap = (angle-this.angle)*sideForceMultiplier;
			double aa = Unit.forceConstant*ap*ap;
			Position forceVector = getLeftSideLineVector();
			forceVector.rotateSimple(-90);
			forceVector = forceVector.times(aa/forceVector.length());
			u.pushForce.add(forceVector);
		}
		
		if((angle > 180 && angle < 360-this.angle)){
			double ap = (angle-(360-this.angle))*sideForceMultiplier;
			double aa = Unit.forceConstant*ap*ap;
			Position forceVector = getRightSideLineVector();
			forceVector.rotateSimple(90);
			forceVector = forceVector.times(aa/forceVector.length());
			u.pushForce.add(forceVector);
		}
		
		
	}



	private void repelFocal(Unit u) {
		if(frontlineOverstepLength(u.position) < 0) {
			Position focalabs = focal.plus(front);
			Position p = u.position.minus(focalabs);
			p = p.times(0.5/p.length());
			u.pushForce.add(p);
		}
	}



	private void repelFrontine(Unit u) {
		double f = frontlineOverstepLength(u.position)*frontLineForceMultiplier;
		Position focalabs = focal.plus(front);
		Position frontrepel = u.position.minus(focalabs);
		
		frontrepel = frontrepel.times(-1.0/(frontrepel.length()));
		frontrepel = frontrepel.times(Unit.forceConstant*f*f);
		if(f > 0) {
			u.pushForce.add(frontrepel);
		}
	}



	public void repaint(Graphics g) {		
		Position focal = getAbsoluteFocalPosition();
		Position leftBoundaryAbsolute = getLeftSideLineVector().plus(focal);
		Position rightBoundaryAbsolute = getRightSideLineVector().plus(focal);
		
		//Draw front-center position
		paintOval(front, red, 15, g, true);
		drawLine(front, nullVector, red, g);
		
		//Draw focal position
		paintOval(focal, green, 15, g, true);
		drawLine(this.focal, front, green, g);
		
		//Draw left boundary-vector
		paintOval(leftBoundaryAbsolute, blue, 15, g, true);
		drawLine(getLeftSideLineVector(), focal, blue, g);
		
		//Draw left boundary-vector
		paintOval(rightBoundaryAbsolute, orange, 15, g, true);
		drawLine(getRightSideLineVector(), focal, orange, g);
		
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
				paintOval(u.position, blue, Unit.UNIT_WIDTH, g, false);
			}else if((angle < 180 && angle > this.angle)) {
				paintOval(u.position, orange, Unit.UNIT_WIDTH, g, false);
			}else if(frontlineOverstepLength(u.position) > 0) {
				paintOval(u.position, white, Unit.UNIT_WIDTH, g, false);
			}else {
				paintOval(u.position, red, Unit.UNIT_WIDTH, g, false);
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
		Position p0 = p.minus(p.times((row*(Unit.UNIT_WIDTH+Unit.UNIT_SPAWN_PADDING))/p.length()));
		double length = p0.length();
				
		//beräkna sidovinkel för en enhet på detta avstånd
		double x = Unit.UNIT_WIDTH + Unit.UNIT_SPAWN_PADDING;
		double sideStepAngle_1 = 360*(x/(length*2*Math.PI));
		
		//beräkna totalvinkel baserat på tidigare utplaceringar
		double angle = sideStepAngle_1*side*rowOffset;
		
		//Om fler inte får plats, börja om på ny rad, nollställ allt
		if(Math.abs(angle) > this.angle) {
			row++;
			angle = 0;
			side = -1;
			p0 = p.minus(p.times((row*(Unit.UNIT_WIDTH+Unit.UNIT_SPAWN_PADDING))/p.length()));
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
	
	
	
	
	public void paintOval(Position p, Color c, int size, Graphics g, boolean fill) {
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
