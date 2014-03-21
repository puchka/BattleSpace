package engine;

import static javax.media.opengl.GL2.GL_COMPILE;
import static javax.media.opengl.GL2ES1.GL_DECAL;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV_MODE;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;

import framework.util.TextureUtils;
import framework.util.WavefrontObjectLoader_DisplayList;

import math.Vector3D;

public class Torpedo extends MovingSolid {
	protected double omega = 0;
	protected static Texture texture;
	protected static int idModel;
	
	public Torpedo(GL2 gl) {
		this.state = 1; // active
		this.setVelocity(0);
		
		list = gl.glGenLists(1);
		gl.glNewList(list, GL_COMPILE);
		
		texture.enable(gl);
        texture.bind(gl);
		
        gl.glPushMatrix();
        
		gl.glCallList(idModel);
		
		gl.glPopMatrix();
		
		texture.disable(gl);
		
		gl.glEndList();
	}
	public static void loadTexture(GL2 gl, String textureFileName){
		// Load texture from image
		texture = TextureUtils.loadImageAsTexture_FLIPPED(gl, textureFileName);
		
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, texture);
        gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
	}
	public static void loadModel(GL2 gl, String modelFileName){
		idModel = WavefrontObjectLoader_DisplayList.loadWavefrontObjectAsDisplayList(gl,
				modelFileName);
	}
	
	
	public void draw(GL2 gl, long timestep){
		gl.glPushMatrix();
		position = Vector3D.add(position, direction.scaleMul(1));
		position = Vector3D.add(position, direction.scaleMul(velocity*timestep));
		
		gl.glTranslated(position.x, position.y, position.z);
		
		gl.glRotated(90, 1, 0, 0);
		
		gl.glRotated(omega, 1, 0, 0);
		
		if (state==2)
			omega+=2*timestep;
		gl.glCallList(list);
		gl.glPopMatrix();
	}

}
