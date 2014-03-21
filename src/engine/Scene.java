package engine;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

import math.Vector3D;

public class Scene {
	protected Planet earth;
	public Scene(GL2 gl){
		earth = new Planet(gl, new Vector3D(0, 0, 0),
						   5, "data/world_map.jpg");
		Skybox.init(gl);
	}
	
	public void draw(GL2 gl, GLU glu){
		earth.draw(gl, glu);
		Skybox.draw(gl);
	}
}
