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

public class Spacecraft extends MovingSolid {
	protected static Texture texture;
	protected static int idModel;
	protected int vie;
	public Spacecraft(GL2 gl, Vector3D position){
		this.position = position;
		this.direction = new Vector3D(0, 0, 0);
		
		list = gl.glGenLists(1);
		gl.glNewList(list, GL_COMPILE);
		
		texture.enable(gl);
        texture.bind(gl);
		
        gl.glPushMatrix();
        
		gl.glCallList(idModel);
		
		gl.glPopMatrix();
		
		texture.disable(gl);
		
		gl.glEndList();
		vie = 5;
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
	
	public void draw(GL2 gl){
		gl.glPushMatrix();
		gl.glTranslated(position.x, position.y, position.z);
		gl.glRotated(0, 0, 0, 1);
		gl.glRotated(90, 1, 0, 0);
		
		gl.glScaled(0.1, 0.1, 0.1);
		gl.glRotated(-Math.toDegrees(direction.z),
				 	 0, 0, 1);
		gl.glCallList(list);
		gl.glPopMatrix();
	}
	public void toucher(){
		vie--;
	}
	public int getVie(){
		return vie;
	}
}
