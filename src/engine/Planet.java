package engine;

import static javax.media.opengl.GL2ES1.GL_DECAL;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV_MODE;

import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.texture.Texture;

import framework.util.TextureUtils;

import math.Vector3D;

public class Planet extends Solid {
	protected double radius;
	protected Texture texture;
	public Planet(GL2 gl, Vector3D position,
				  double radius,
				  String textureFileName){
		// Load texture from image
		texture = TextureUtils.loadImageAsTexture_FLIPPED(gl, textureFileName);
		
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, texture);
        gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
		this.position = position;
		this.radius = radius;
	}
	
	protected void draw(GL2 gl, GLU glu){
		GLUquadric params = glu.gluNewQuadric();
		texture.enable(gl);
		texture.bind(gl);
		glu.gluQuadricTexture(params, true);
		gl.glPushMatrix();
		gl.glTranslated(position.x, position.y, position.z);
		glu.gluSphere(params, radius, 20, 20);
		gl.glPopMatrix();
		texture.disable(gl);
	}
}
