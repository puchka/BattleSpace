package view;

import javax.media.opengl.glu.GLU;

import math.Vector3D;

public class Camera {
	private Vector3D _position;
	private Vector3D _target;
	public Camera(Vector3D position, Vector3D target){
		_position = position;
		_target = target;
	}
	public void look(GLU glu){
		glu.gluLookAt(_position.x, _position.y, _position.z,
	              	  _target.x, _target.y, _target.z,
	              	  0, 0, 1);
	}
	public void setPosition(Vector3D _position) {
		this._position = _position;
	}
	public void setTarget(Vector3D _target) {
		this._target = _target;
	}
	
}
