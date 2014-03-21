package controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import math.Vector3D;
import view.Camera;

import com.jogamp.opengl.util.FPSAnimator;

import engine.Missile;
import engine.Shuttle;

public class Controller implements KeyListener,
								   MouseListener,
								   MouseMotionListener,
								   MouseWheelListener{
	protected FPSAnimator animator;
	protected Camera camera;
	protected Shuttle shuttle;
	protected ArrayList<Missile> missiles;
	protected double _speed;
	protected double _sensivity;
	protected long _timeBeforeStoppingVerticalMotion;
	protected boolean _verticalMotionActive;
	protected int _verticalMotionDirection;
	protected Hashtable<Integer, Boolean> _keystates;
	protected Hashtable<String, Integer> _keyconf;
	protected Vector3D _position;
	protected Vector3D _target;
	protected Vector3D _forward;
	protected Vector3D _left;
	protected double _theta;
	protected double _phi;
	
	protected int _xp;
	protected int _yp;
	
	protected Robot _robot;
	protected int _widthFrame;
	protected int _heightFrame;
	
	// Menu
	
	protected double _motionSensivity = 4;
	protected double _angleZ;
	protected boolean _hold;
	
	public Controller(Vector3D position){
		_position = position;
		_phi = 0;
		_theta = 0;
		_forward = new Vector3D(0, 0, 0);
		vectorsFromAngles();
		
		_speed = 0.01;
		_sensivity = 0.2;
		_verticalMotionActive = false;
		_keyconf = new Hashtable<String, Integer>();
		// Initialization key configurations
		_keyconf.put("forward", KeyEvent.VK_Z);
		_keyconf.put("backward", KeyEvent.VK_S);
		_keyconf.put("strafe_left", KeyEvent.VK_Q);
		_keyconf.put("strafe_right", KeyEvent.VK_D);
		_keyconf.put("boost", KeyEvent.VK_SHIFT);
		// Initialization of key states
		_keystates = new Hashtable<Integer, Boolean>();
		_keystates.put(_keyconf.get("forward"), false);
		_keystates.put(_keyconf.get("backward"), false);
		_keystates.put(_keyconf.get("strafe_left"), false);
		_keystates.put(_keyconf.get("strafe_right"), false);
		_keystates.put(_keyconf.get("boost"), false);
		
		_xp = 0;
		_yp = 0;
		
		try {
			_robot = new Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void vectorsFromAngles(){
		final Vector3D up = new Vector3D(0, 0, 1);
		/*
		if (_theta > -150)
	        _theta = -150;
	    else if (_theta < -220)
	        _theta = -220;
	    */
		double r_temp = Math.cos(Math.toRadians(_phi));
	    _forward.z = Math.sin(Math.toRadians(_phi));
	    _forward.x = r_temp*Math.cos(Math.toRadians(_theta));
	    _forward.y = r_temp*Math.sin(Math.toRadians(_theta));
	    
	    _left = up.crossProduct(_forward);
	    _left.normalize();
	    
	    _target = Vector3D.add(_position, _forward);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
			case KeyEvent.VK_ESCAPE:
				if (animator.isAnimating()) animator.stop();
				System.exit(0);
				break;
			case KeyEvent.VK_SPACE:
				if (view.Canvas.mode)
					view.Canvas.mode = false;
				else
					view.Canvas.mode = true;
				break;
			default:
				if (!view.Canvas.mode){
					for (Enumeration<Integer> en = _keystates.keys(); en.hasMoreElements();){
						int key = en.nextElement();
						if (e.getKeyCode()==key){
							_keystates.put(key, true);
							break;
						}
					}
				}
				break;
		
		}
		if (view.Canvas.mode){
			switch(e.getKeyCode()){
			case KeyEvent.VK_LEFT:
				_angleZ-=180;
				break;
			case KeyEvent.VK_RIGHT:
				_angleZ+=180;
				break;
			case KeyEvent.VK_ENTER:
				int n = (int)Math.floor(_angleZ)/360;
				
				double angle = _angleZ-n*360;
				//System.out.println(angle);
				if (Math.abs(angle)>250 || Math.abs(angle)<10)
					view.Canvas.mode = false;
					//System.out.println("pause");
				else if (Math.abs(angle)>170 || Math.abs(angle)<190){
					animator.stop();
					System.exit(0);
					//System.out.println("exit");
				}
			}
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!view.Canvas.mode){
			for (Enumeration<Integer> en = _keystates.keys(); en.hasMoreElements();){
				int key = en.nextElement();
				if (e.getKeyCode()==key){
					_keystates.put(key, false);
					break;
				}
			}
		}
	}
	
	public void setAnimator(FPSAnimator animator){
		this.animator = animator;
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if (_hold && view.Canvas.mode){
			double yrel = _yp - e.getY();
			_yp = e.getY();
			_angleZ+=yrel*_motionSensivity;
			
			view.Menu.angleScene = _angleZ;
			
			System.out.println(_angleZ);
			
		}
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		if (!view.Canvas.mode){
			int xrel = e.getX()-_xp;
			int yrel = e.getY()-_xp;
			yrel = 0;
			_xp = e.getX();
			_yp = e.getY();
			_theta -= xrel*_sensivity;
		    _phi -= yrel*_sensivity;
		    vectorsFromAngles();
			if (_xp<100){
				_robot.mouseMove(_widthFrame/2, _heightFrame/2);
				_xp = _widthFrame/2;
				_yp = _heightFrame/2;
			}
			else if (_xp>_widthFrame-100){
				_robot.mouseMove(_widthFrame/2, _heightFrame/2);
				_xp = _widthFrame/2;
				_yp = _heightFrame/2;
			}
			else if (_yp>_heightFrame-100){
				_robot.mouseMove(_widthFrame/2, _heightFrame/2);
				_xp = _widthFrame/2;
				_yp = _heightFrame/2;
			}
			else if (_yp<100){
				_robot.mouseMove(_widthFrame/2, _heightFrame/2);
				_xp = _widthFrame/2;
				_yp = _heightFrame/2;
			}
		}
		
	}
	public void setWidthCanvas(int width){
		_widthFrame = width;
	}
	public void setHeightCanvas(int height){
		_heightFrame = height;
	}
	public void animate(long timestep){
		double realspeed = (_keystates.get(_keyconf.get("boost")))?10*_speed:_speed;
		if (_keystates.get(_keyconf.get("forward"))) 
	        _position.add(_forward.scaleMul(realspeed * timestep)); //on avance
	    if (_keystates.get(_keyconf.get("backward")))
	        _position .sub(_forward.scaleMul(realspeed * timestep)); //on recule
	    if (_keystates.get(_keyconf.get("strafe_left")))
	        _position.add(_left.scaleMul(realspeed * timestep)); //on se déplace sur la gauche
	    if (_keystates.get(_keyconf.get("strafe_right"))) 
	        _position.sub(_left.scaleMul(realspeed * timestep)); //on se déplace sur la droite
	    if (_verticalMotionActive)
	    {
	        if (timestep > _timeBeforeStoppingVerticalMotion)
	            _verticalMotionActive = false;
	        else
	            _timeBeforeStoppingVerticalMotion -= timestep;
	        _position.add(new Vector3D(0,0,_verticalMotionDirection*realspeed*timestep)); //on monte ou on descend, en fonction de la valeur de _verticalMotionDirection
	    }
	    
	    _target = Vector3D.add(_position, _forward); //comme on a bougé, on recalcule la cible fixée
	    
		camera.setPosition(_position);
		camera.setTarget(_target);
		
		Vector3D posShuttle = Vector3D.add(_target, new Vector3D(0, 0, -4));
		Vector3D dirShuttle = new Vector3D(_forward);
		dirShuttle.normalize();
		posShuttle.add(_forward.scaleMul(13));
	    shuttle.setPosition(posShuttle);
	    shuttle.setDirection(dirShuttle);
	    
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!view.Canvas.mode){
			_verticalMotionActive = true; //on demande à activer le mouvement vertical
			_timeBeforeStoppingVerticalMotion = 250; //pendant 250 ms
			_verticalMotionDirection = e.getWheelRotation(); //et vers le haut
		}
	}
	public void setCamera(Camera camera){
		this.camera = camera;
	}
	public void setShuttle(Shuttle shuttle){
		this.shuttle = shuttle;
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!view.Canvas.mode){
			//touver la première missile en attente
			for (int i=0;i<missiles.size();i++){
				if (missiles.get(i).getState()==1){
					missiles.get(i).setVelocity(0.1);
					missiles.get(i).setState(2);
					if (i!=missiles.size()-1)
						missiles.get(i+1).setState(1);
					break;
				}
			}
		}
		if (view.Canvas.mode){
			int n = (int)Math.floor(_angleZ)/360;
			
			double angle = _angleZ-n*360;
			//System.out.println(angle);
			if (Math.abs(angle)>250 || Math.abs(angle)<10)
				view.Canvas.mode = false;
				//System.out.println("pause");
			else if (Math.abs(angle)>170 || Math.abs(angle)<190){
				animator.stop();
				System.exit(0);
				//System.out.println("exit");
			}
			
			
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if (view.Canvas.mode){
			_hold = true;
			_yp = e.getY();
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if (view.Canvas.mode)
			_hold = false;
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		if (!view.Canvas.mode)
			_robot.mouseMove(_widthFrame/2, _heightFrame/2);
		
	}
	public void setSensivity(double sensivity){
		_sensivity = sensivity;
	}
	public void setMissiles(ArrayList<Missile> missiles){
		this.missiles = missiles;
	}
	
}
