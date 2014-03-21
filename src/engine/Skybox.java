package engine;

import static javax.media.opengl.GL2.*;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.texture.Texture;

import framework.util.TextureUtils;

public class Skybox {
	static Texture textureFront;
	static Texture textureBack;
	static Texture textureLeft;
	static Texture textureRight;
	static Texture textureTop;
	static Texture textureBottom;
	public static void init(GL2 gl){
		textureFront = TextureUtils.loadImageAsTexture_FLIPPED(gl,
															   "data/Weltraum.png");
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, textureFront);
		textureBack = TextureUtils.loadImageAsTexture_FLIPPED(gl,
															  "data/WeltraumH.png");
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, textureBack);
		textureLeft = TextureUtils.loadImageAsTexture_FLIPPED(gl,
		  													  "data/WeltraumL.png");
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, textureLeft);
		textureRight = TextureUtils.loadImageAsTexture_FLIPPED(gl,
															   "data/WeltraumR.png");
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, textureRight);
		textureTop = TextureUtils.loadImageAsTexture_FLIPPED(gl,
		   													 "data/WeltraumO.png");
		TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, textureTop);
		textureBottom = TextureUtils.loadImageAsTexture_FLIPPED(gl,
			 													"data/WeltraumU.png");
TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, textureBottom);
		
        gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
	}
	public static void draw(GL2 gl){
		textureFront.enable(gl);
		textureFront.bind(gl);
		gl.glPushMatrix();
		//gl.glTranslated(-125, 0, 0);
		gl.glBegin(GL_QUADS);
		    gl.glTexCoord2d(0, 1); gl.glVertex3d(-250, -250, 250);
		    gl.glTexCoord2d(0, 0); gl.glVertex3d(-250, -250, -250);
		    gl.glTexCoord2d(1, 0); gl.glVertex3d(-250, 250, -250);
		    gl.glTexCoord2d(1, 1); gl.glVertex3d(-250, 250, 250);
	    gl.glEnd();
	    gl.glPopMatrix();
	    textureFront.disable(gl);
	    
	    textureBack.enable(gl);
	    textureBack.bind(gl);
	    
	    gl.glPushMatrix();
	    //gl.glTranslated(125, 0, 0);
	    gl.glBegin(GL_QUADS);
		    gl.glTexCoord2d(0, 1); gl.glVertex3d(250, 250, 250);
		    gl.glTexCoord2d(0, 0); gl.glVertex3d(250, 250, -250);
		    gl.glTexCoord2d(1, 0); gl.glVertex3d(250, -250, -250);
		    gl.glTexCoord2d(1, 1); gl.glVertex3d(250, -250, 250);
	    gl.glEnd();
	    gl.glPopMatrix();
	    textureBack.disable(gl);
	    
	    textureLeft.enable(gl);
	    textureLeft.bind(gl);
	    gl.glPushMatrix();
	    //gl.glTranslated(0, -125, 0);
	    gl.glBegin(GL_QUADS);
		    gl.glTexCoord2d(0, 1); gl.glVertex3d(250, -250, 250);
		    gl.glTexCoord2d(0, 0); gl.glVertex3d(250, -250, -250);
		    gl.glTexCoord2d(1, 0); gl.glVertex3d(-250, -250, -250);
		    gl.glTexCoord2d(1, 1); gl.glVertex3d(-250, -250, 250);
	    gl.glEnd();
	    gl.glPopMatrix();
	    textureLeft.disable(gl);
	    
	    textureRight.enable(gl);
	    textureRight.bind(gl);
	    gl.glPushMatrix();
	    //gl.glTranslated(0, 125, 0);
	    gl.glBegin(GL_QUADS);
		    gl.glTexCoord2d(0, 1); gl.glVertex3d(-250, 250, 250);
		    gl.glTexCoord2d(0, 0); gl.glVertex3d(-250, 250, -250);
		    gl.glTexCoord2d(1, 0); gl.glVertex3d(250, 250, -250);
		    gl.glTexCoord2d(1, 1); gl.glVertex3d(250, 250, 250);
	    gl.glEnd();
	    gl.glPopMatrix();
	    textureRight.disable(gl);
		
	    textureTop.enable(gl);
	    textureTop.bind(gl);
	    gl.glBegin(GL_QUADS);
		    gl.glTexCoord2d(0, 1); gl.glVertex3d(250, -250, 230);
		    gl.glTexCoord2d(0, 0); gl.glVertex3d(-250, -250, 230);
		    gl.glTexCoord2d(1, 0); gl.glVertex3d(-250, 250, 230);
		    gl.glTexCoord2d(1, 1); gl.glVertex3d(250, 250, 230);
	    gl.glEnd();
	    textureTop.disable(gl);
	    
	    textureBottom.enable(gl);
	    textureBottom.bind(gl);
	    gl.glBegin(GL_QUADS);
		    gl.glTexCoord2d(0, 1); gl.glVertex3d(-250, -250, -250);
		    gl.glTexCoord2d(0, 0); gl.glVertex3d(250, -250, -250);
		    gl.glTexCoord2d(1, 0); gl.glVertex3d(250, 250, -250);
		    gl.glTexCoord2d(1, 1); gl.glVertex3d(-250, 250, -250);
	    gl.glEnd();
	    textureBottom.disable(gl);
	    
	}
}
