package view;

import static constants.Constants.CANVAS_HEIGHT;
import static constants.Constants.CANVAS_WIDTH;
import static javax.media.opengl.GL.GL_COLOR_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_BUFFER_BIT;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

import math.Vector3D;
import sound.Sound;
import ai.Animator;

import controller.Controller;
import engine.Explosion;
import engine.Missile;
import engine.Scene;
import engine.Shuttle;
import engine.Spacecraft;
import engine.Torpedo;

@SuppressWarnings("serial")
public class Canvas extends GLCanvas implements GLEventListener {
	private GLU glu;
	private Scene scene;
	private Shuttle shuttle;
	private Spacecraft spacecraft;
	
	private Animator animator;
	
	private Sound sound;
	//private Sound bgSound;
	
	private ArrayList<Missile> missiles = new ArrayList<Missile>();
	private ArrayList<Torpedo> torpedos = new ArrayList<Torpedo>();
	private ArrayList<Explosion> explosions = new ArrayList<Explosion>();
	
	private Controller controller;
	private Camera camera;
	
	long current_time = System.currentTimeMillis();
    long elapsed_time;
    long last_time = current_time;
    
    protected Menu menu;
    public static boolean mode;
    
	public Canvas(){
		this.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.addGLEventListener(this);
		mode = true;
	}
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		/*
		gl.glMatrixMode(GL_PROJECTION);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glOrtho(0, CANVAS_WIDTH, 0, CANVAS_HEIGHT, -1, 1);
		*/
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity();
		current_time = System.currentTimeMillis();
	    elapsed_time = current_time - last_time; //on calcule le temps écoulé depuis la dernière image
	    last_time = current_time;
	    controller.animate(elapsed_time);
	    if (!mode){
			camera.look(glu);
			scene.draw(gl, glu);
			shuttle.draw(gl);
			if (spacecraft.getVie()>0){
				animator.run(elapsed_time);
				spacecraft.draw(gl);
				
			}
			if (spacecraft.getVie()==0)
				for (int i=0;i<torpedos.size();i++)
					torpedos.remove(i);
			for (int i=0;i<torpedos.size();i++){
				if (torpedos.get(i).getState()==1)
					// if the torpedo is active, move with the spacecraft
					torpedos.get(i).setPosition(spacecraft.getPosition());
				if (torpedos.get(i).getState()!=0)
					torpedos.get(i).draw(gl, elapsed_time);
				if (torpedos.get(i).getPosition().distance(shuttle.getPosition())>100)
					torpedos.get(i).setState(3);
				if (torpedos.get(i).getPosition().distance(shuttle.getPosition())<5){
					sound.play();
					explosions.add(new Explosion(torpedos.get(i).getPosition()));
					explosions.get(explosions.size()-1).initialize(gl, 3000);
					shuttle.toucher();
					torpedos.get(i).setState(3);
				}
			}
			/*
			System.out.println("Torpedos: "+torpedos.size());
			System.out.println("Life : "+shuttle.getVie());
			*/
			for (int i=0;i<missiles.size();i++){
				if (missiles.get(i).getState()!=0){
					if (missiles.get(i).getState()!=2){
						// if the missile in not moving, move with the shuttle
						missiles.get(i).setPosition(shuttle.getPosition());
						missiles.get(i).setDirection(shuttle.getDirection());
					}
					if (missiles.get(i).getPosition().distance(shuttle.getPosition())>100 &&
						missiles.get(i).getState()==2) // TODO enough far and moving
						missiles.get(i).setState(3);
					missiles.get(i).draw(gl, elapsed_time);
				}
				if (missiles.get(i).getPosition().length()<5){
					explosions.add(new Explosion(missiles.get(i).getPosition()));
					explosions.get(explosions.size()-1).initialize(gl, 3000);
					sound.play();
					missiles.get(i).setState(3);
					
				}
				if (missiles.get(i).getPosition().distance(spacecraft.getPosition())<5 &&
						spacecraft.getVie()>0){
					explosions.add(new Explosion(missiles.get(i).getPosition()));
					explosions.get(explosions.size()-1).initialize(gl, 3000);
					spacecraft.toucher();
					sound.play();
					missiles.get(i).setState(3);
				}
				
			}
			for (int i=0;i<missiles.size();i++)
				if (missiles.get(i).getState()==3)
					missiles.remove(i);
			
			for (int i=0;i<torpedos.size();i++)
				if (torpedos.get(i).getState()==3)
					torpedos.remove(i);
			
			for (int i=0;i<explosions.size();i++){
				if (explosions.get(i).isActive())
					explosions.get(i).draw(gl);
				else
					explosions.remove(i);
			}
	    }
	    else{
	    	glu.gluLookAt(10, 0, 0, 0, 0, 0, 0, 0, 1);
	    	Menu.draw(gl);
	    	
	    }
	}
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL_DEPTH_TEST);
		glu = new GLU();
		
		gl.glMatrixMode( GL_PROJECTION );
		gl.glLoadIdentity();
		glu.gluPerspective(70, (double)CANVAS_WIDTH/CANVAS_HEIGHT, 1, 1000);
		scene = new Scene(gl);
		Shuttle.loadTexture(gl, "data/spstob_1.bmp");
		Shuttle.loadModel(gl, "data/SpaceShuttle.obj");
		shuttle = new Shuttle(gl, new Vector3D(20, 0, 0));
		
		Missile.loadTexture(gl, "data/missile.png");
		Missile.loadModel(gl, "data/missile.obj");
		
		for (int i=0;i<30;i++){
			missiles.add(new Missile(gl));
			missiles.get(i).setPosition(shuttle.getPosition());
			missiles.get(i).setDirection(shuttle.getDirection());
			
			if (i>1)
				missiles.get(i).setState(0);
		}
		Spacecraft.loadTexture(gl, "data/Space.bmp");
		Spacecraft.loadModel(gl, "data/Spacecraft.obj");
		// TODO position initial spacecraft
		spacecraft = new Spacecraft(gl, new Vector3D(-40, 0, 0));
		spacecraft.setDirection(new Vector3D(1, 0, 0));
		
		Torpedo.loadTexture(gl, "data/torpedo.bmp");
		Torpedo.loadModel(gl, "data/torpedo.obj");
		for (int i=0;i<30;i++){
			torpedos.add(new Torpedo(gl));
			torpedos.get(i).setPosition(spacecraft.getPosition());
			torpedos.get(i).setDirection(spacecraft.getDirection());
			
			if (i>0)
				torpedos.get(i).setState(0);
		}
		
		camera = new Camera(new Vector3D(30, 0, 4), new Vector3D(0, 0, 0));
		controller.setCamera(camera);
		controller.setShuttle(shuttle);
		controller.setMissiles(missiles);
		
		animator = new Animator(spacecraft);
		animator.setTorpedos(torpedos);
		
		Sound.init();
		sound = new Sound("data/Laser_Cannon.wav");
		
		//bgSound = new Sound("data/fond.wav");
		//bgSound.play();
		
		Menu.init(gl, glu);
		
	}
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		GL2 gl = drawable.getGL().getGL2();
		
		float aspect = (float)width / height;
		
		controller.setWidthCanvas(width);
		controller.setHeightCanvas(height);
		
		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(70, aspect, 1, 1000);
		
	}
	
	public void setController(Controller controller){
		this.controller = controller;
	}
	public int getWidthCanvas(){
		return CANVAS_WIDTH;
	}
	public int getHeightCanvas(){
		return CANVAS_HEIGHT;
	}
	
}
