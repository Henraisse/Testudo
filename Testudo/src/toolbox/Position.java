package toolbox;

import java.awt.geom.AffineTransform;
import java.io.Serializable;
import java.util.Random;

public class Position implements Serializable{
	public double x;
	public double y;

	public Position(double a, double b){
		x = a;
		y = b;
	}

	public Position normalize(){    
		if(x != 0 && y != 0){
			double xUnit = x / Math.sqrt((x*x)+(y*y));
			double yUnit = y / Math.sqrt((x*x)+(y*y));
			return new Position(xUnit, yUnit);
		}  
		else if(x == 0 && y != 0){y = 1;}
		else if(y == 0 && x != 0){x = 1;}
		else{
			x = 0;
			y = 0;
		}
		return new Position(x,y);
	}

	public double length(){   
		return Math.sqrt((x*x)+(y*y));
	}

	public Position minus(Position p){
		double xSub = x - p.x;
		double ySub = y - p.y;
		return new Position(xSub, ySub);
	}

	public Position minus(double d){
		double xSub = x - d;
		double ySub = y - d;
		return new Position(xSub, ySub);
	}

	public Position plus(Position p){
		double xSub = x + p.x;
		double ySub = y + p.y;
		return new Position(xSub, ySub);
	}

	public Position plus(double d){
		double xSub = x + d;
		double ySub = y + d;
		return new Position(xSub, ySub);
	}  

	public void add(double d){
		x = x + d;
		y = y + d;
	}  

	public void add(Position p){
		x = x + p.x;
		y = y + p.y;
	}

	public Position times(double d){
		double xSub = (double)x * d;
		double ySub = (double)y * d;
		return new Position(xSub, ySub);
	}

	public Position getNormal(){
		return new Position(-y, x);
	}

	public double distance(Position p){
		double distance = Math.sqrt(((x-p.x)*(x-p.x))+((y-p.y)*(y-p.y)));
		return distance;   
	}

	/*
	 * Calculates the unit dotproduct, a double between 1 and -1, reflecting how much
	 * the two vectors differ in direction. 1 is same direction, -1 is opposite directions, 0 is orthogonal.
	 */
	public double unitDotProduct(Position b){
		Position an = new Position(x,y).normalize();
		Position bn = b.normalize();
		double dotProduct = (an.x*bn.x) + (an.y*bn.y);     
		return dotProduct;
	}

	public void subtract(Position times) {

		x = x - times.x;
		y = y - times.y;

	}

	public void randomize(double x2, double y2){
		Random rand = new Random();
		x = rand.nextDouble()*(double)x2;
		y = rand.nextDouble()*(double)y2;
	}

	public String toString(){
		String r = "(" + x + ", " + y + ")";
		return r;
	}

	public void rotate(double radius, double degrees){
		double[] pt = {x, y};		
		AffineTransform.getRotateInstance(Math.toRadians(degrees), x-radius, y).transform(pt, 0, pt, 0, 1);

		x = pt[0];
		y = pt[1];

		//		double[] pt = {x, y};
		//		AffineTransform.getRotateInstance(Math.toRadians(angle), center.x, center.y).transform(pt, 0, pt, 0, 1); // specifying to use this double[] to hold coords
		//		double newX = pt[0];
		//		double newY = pt[1];
	}

	public void rotate(double radius, double degrees, int centerX, int centerY){
		double[] pt = {x, y};		
		AffineTransform.getRotateInstance(Math.toRadians(degrees), centerX, centerY).transform(pt, 0, pt, 0, 1);

		x = pt[0];
		y = pt[1];

		//		double[] pt = {x, y};
		//		AffineTransform.getRotateInstance(Math.toRadians(angle), center.x, center.y).transform(pt, 0, pt, 0, 1); // specifying to use this double[] to hold coords
		//		double newX = pt[0];
		//		double newY = pt[1];
	}

	public Position clone(){
		Position p = new Position(0, 0);
		p.x = x;
		p.y = y;

		return p;
	}

	public void clone(Position p){
		x = p.x;
		y = p.y;
	}


	public double angle(Position p){
		Position v1 = this;
		Position v2 = p;
		//double angle = Math.acos(dot) * 180.0/Math.PI;
		double angle = Math.atan2(v2.y,v2.x) - Math.atan2(v1.y,v1.x);
		angle = (angle/Math.PI)*180.0;
		return angle;
	}


	public void rotateSimple(double angle) { // angle in radians
		angle = (angle/360.0)*2*Math.PI;
		//normalize(vector); // No  need to normalize, vector is already ok...

		float x1 = (float)(x * Math.cos(angle) - y * Math.sin(angle));

		float y1 = (float)(x * Math.sin(angle) + y * Math.cos(angle)) ;

		x = x1;
		y = y1;

	}



}









