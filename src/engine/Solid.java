package engine;

import math.Vector3D;

public class Solid {
	protected Vector3D position;
	protected int state;
	
	public Vector3D getPosition() {
		return position;
	}
	public void setPosition(Vector3D position) {
		this.position = position;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
}
