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

public class Shuttle extends MovingSolid {
	protected static Texture texture;
	protected static int idModel;
	protected int vie;
	public Shuttle(GL2 gl, Vector3D position){
		this.position = position;
		this.direction = new Vector3D(-1, 0, 0);
		this.vie = 5;
		
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
	
	public void draw(GL2 gl){
		gl.glPushMatrix();
		gl.glTranslated(position.x, position.y, position.z);
		gl.glRotated(90, 0, 0, 1);
		gl.glRotated(90, 1, 0, 0);
		
		if (direction.y>0)
			gl.glRotated(180+Math.toDegrees(Math.acos(direction.x)),
				 	 	 0, 1, 0);
		else
			gl.glRotated(90+Math.toDegrees(Math.asin(direction.x)),
				 	 	 0, 1, 0);
		gl.glCallList(list);
		gl.glPopMatrix();
	}
	public int getVie(){
		return vie;
	}
	public void toucher(){
		vie--;
	}
}
