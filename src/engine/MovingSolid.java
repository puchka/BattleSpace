package engine;

import javax.media.opengl.GL2;

import math.Vector3D;

public class MovingSolid extends Solid {
	protected double velocity;
	protected Vector3D direction;
	protected int list;
	
	public double getVelocity() {
		return velocity;
	}

	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	public Vector3D getDirection() {
		return direction;
	}

	public void setDirection(Vector3D direction) {
		this.direction = direction;
	}
	
	public void draw(GL2 gl){
		gl.glPushMatrix();
		gl.glTranslated(position.x, position.y, position.z);
		gl.glCallList(list);
		gl.glPopMatrix();
	}
	
}
