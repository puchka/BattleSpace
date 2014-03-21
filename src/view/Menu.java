package view;

import static constants.Constants.CANVAS_HEIGHT;
import static constants.Constants.CANVAS_WIDTH;
import static javax.media.opengl.GL.GL_DEPTH_TEST;
import static javax.media.opengl.GL.GL_LINEAR;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL2.GL_QUADS;
import static javax.media.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import javax.media.opengl.glu.GLU;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

public class Menu{
	// Rotational angle about the x, y and z axes in degrees
	private static float angleX = 0.0f;
	private static float angleY = 0.0f;
	private static float angleZ = 0.0f;
	// Rotational speed about x, y, z axes in degrees per refresh
	private static float rotateSpeedX = 0.3f;
	private static float rotateSpeedY = 0.2f;
	private static float rotateSpeedZ = 0.4f;
	
	public static double angleScene = 0;
	
	// Texture
	private static Texture texture;
	private static Texture texture2;
	
	// Texture image flips vertically. Shall use TextureCoords class to retrieve the
	// top, bottom, left and right coordinates.
	private static float textureTop, textureBottom, textureLeft, textureRight;
	
	public static void draw(GL2 gl) {
		gl.glPushMatrix();
		
		gl.glTranslated(5*Math.cos(Math.toRadians(angleScene)),
						5*Math.sin(Math.toRadians(angleScene)), 0);
		
		//gl.glTranslatef(0.0f, 0.0f, -5.0f); // translate into the screen
		gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
		gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
		gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f); // rotate about the z-axis

		// Enables this texture's target in the current GL context's state.
		texture.enable(gl);  // same as gl.glEnable(texture.getTarget());
		// gl.glTexEnvi(GL.GL_TEXTURE_ENV, GL.GL_TEXTURE_ENV_MODE, GL.GL_REPLACE);
		// Binds this texture to the current GL context.
		texture.bind(gl);  // same as gl.glBindTexture(texture.getTarget(), texture.getTextureObject());
	 
		gl.glBegin(GL_QUADS);

		// Front Face
		gl.glTexCoord2f(textureLeft, textureBottom);
		gl.glVertex3f(-1.0f, -1.0f, 1.0f); // bottom-left of the texture and quad
		gl.glTexCoord2f(textureRight, textureBottom);
		gl.glVertex3f(1.0f, -1.0f, 1.0f);  // bottom-right of the texture and quad
		gl.glTexCoord2f(textureRight, textureTop);
		gl.glVertex3f(1.0f, 1.0f, 1.0f);   // top-right of the texture and quad
		gl.glTexCoord2f(textureLeft, textureTop);
		gl.glVertex3f(-1.0f, 1.0f, 1.0f);  // top-left of the texture and quad

		// Back Face
		gl.glTexCoord2f(textureRight, textureBottom);
	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	      gl.glTexCoord2f(textureRight, textureTop);
	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
	      gl.glTexCoord2f(textureLeft, textureTop);
	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
	      gl.glTexCoord2f(textureLeft, textureBottom);
	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
	      
	      // Top Face
	      gl.glTexCoord2f(textureLeft, textureTop);
	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
	      gl.glTexCoord2f(textureLeft, textureBottom);
	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
	      gl.glTexCoord2f(textureRight, textureBottom);
	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
	      gl.glTexCoord2f(textureRight, textureTop);
	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
	      
	      // Bottom Face
	      gl.glTexCoord2f(textureRight, textureTop);
	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	      gl.glTexCoord2f(textureLeft, textureTop);
	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
	      gl.glTexCoord2f(textureLeft, textureBottom);
	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
	      gl.glTexCoord2f(textureRight, textureBottom);
	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
	      
	      // Right face
	      gl.glTexCoord2f(textureRight, textureBottom);
	      gl.glVertex3f(1.0f, -1.0f, -1.0f);
	      gl.glTexCoord2f(textureRight, textureTop);
	      gl.glVertex3f(1.0f, 1.0f, -1.0f);
	      gl.glTexCoord2f(textureLeft, textureTop);
	      gl.glVertex3f(1.0f, 1.0f, 1.0f);
	      gl.glTexCoord2f(textureLeft, textureBottom);
	      gl.glVertex3f(1.0f, -1.0f, 1.0f);
	      
	      // Left Face
	      gl.glTexCoord2f(textureLeft, textureBottom);
	      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	      gl.glTexCoord2f(textureRight, textureBottom);
	      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
	      gl.glTexCoord2f(textureRight, textureTop);
	      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
	      gl.glTexCoord2f(textureLeft, textureTop);
	      gl.glVertex3f(-1.0f, 1.0f, -1.0f);

	      gl.glEnd();
	      
	      texture.disable(gl);
	      
	      gl.glPopMatrix();
	      
	      // Menu 2
	      
	      texture2.enable(gl);
	      texture2.bind(gl);
	      
	      gl.glPushMatrix();
	      
	      gl.glTranslated(5*Math.cos(Math.toRadians(angleScene+180)),
	    		  		  5*Math.sin(Math.toRadians(angleScene+180)), 0);
	      
	      gl.glRotatef(angleX, 1.0f, 0.0f, 0.0f); // rotate about the x-axis
			gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis
			gl.glRotatef(angleZ, 0.0f, 0.0f, 1.0f); // rotate about the z-axis
		 
			gl.glBegin(GL_QUADS);

			// Front Face
			gl.glTexCoord2f(textureLeft, textureBottom);
			gl.glVertex3f(-1.0f, -1.0f, 1.0f); // bottom-left of the texture and quad
			gl.glTexCoord2f(textureRight, textureBottom);
			gl.glVertex3f(1.0f, -1.0f, 1.0f);  // bottom-right of the texture and quad
			gl.glTexCoord2f(textureRight, textureTop);
			gl.glVertex3f(1.0f, 1.0f, 1.0f);   // top-right of the texture and quad
			gl.glTexCoord2f(textureLeft, textureTop);
			gl.glVertex3f(-1.0f, 1.0f, 1.0f);  // top-left of the texture and quad

			// Back Face
			gl.glTexCoord2f(textureRight, textureBottom);
		      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		      gl.glTexCoord2f(textureRight, textureTop);
		      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		      gl.glTexCoord2f(textureLeft, textureTop);
		      gl.glVertex3f(1.0f, 1.0f, -1.0f);
		      gl.glTexCoord2f(textureLeft, textureBottom);
		      gl.glVertex3f(1.0f, -1.0f, -1.0f);
		      
		      // Top Face
		      gl.glTexCoord2f(textureLeft, textureTop);
		      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
		      gl.glTexCoord2f(textureLeft, textureBottom);
		      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		      gl.glTexCoord2f(textureRight, textureBottom);
		      gl.glVertex3f(1.0f, 1.0f, 1.0f);
		      gl.glTexCoord2f(textureRight, textureTop);
		      gl.glVertex3f(1.0f, 1.0f, -1.0f);
		      
		      // Bottom Face
		      gl.glTexCoord2f(textureRight, textureTop);
		      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		      gl.glTexCoord2f(textureLeft, textureTop);
		      gl.glVertex3f(1.0f, -1.0f, -1.0f);
		      gl.glTexCoord2f(textureLeft, textureBottom);
		      gl.glVertex3f(1.0f, -1.0f, 1.0f);
		      gl.glTexCoord2f(textureRight, textureBottom);
		      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		      
		      // Right face
		      gl.glTexCoord2f(textureRight, textureBottom);
		      gl.glVertex3f(1.0f, -1.0f, -1.0f);
		      gl.glTexCoord2f(textureRight, textureTop);
		      gl.glVertex3f(1.0f, 1.0f, -1.0f);
		      gl.glTexCoord2f(textureLeft, textureTop);
		      gl.glVertex3f(1.0f, 1.0f, 1.0f);
		      gl.glTexCoord2f(textureLeft, textureBottom);
		      gl.glVertex3f(1.0f, -1.0f, 1.0f);
		      
		      // Left Face
		      gl.glTexCoord2f(textureLeft, textureBottom);
		      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
		      gl.glTexCoord2f(textureRight, textureBottom);
		      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
		      gl.glTexCoord2f(textureRight, textureTop);
		      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
		      gl.glTexCoord2f(textureLeft, textureTop);
		      gl.glVertex3f(-1.0f, 1.0f, -1.0f);

		      gl.glEnd();
		      
		      texture2.disable(gl);
		      
		      gl.glPopMatrix();

	      // Disables this texture's target (e.g., GL_TEXTURE_2D) in the current GL
	      // context's state.
	      //texture.disable(gl);  // same as gl.glDisable(texture.getTarget());

	      // Update the rotational angel after each refresh by the corresponding
	      // rotational speed
	      angleX += rotateSpeedX;
	      angleY += rotateSpeedY;
	      angleZ += rotateSpeedZ;
	}
	
	public static void init(GL2 gl, GLU glu) {
		gl.glEnable(GL_DEPTH_TEST);
		glu = new GLU();
		gl.glMatrixMode( GL_PROJECTION );
		gl.glLoadIdentity();
		glu.gluPerspective(70, (double)CANVAS_WIDTH/CANVAS_HEIGHT, 1, 1000);
		
		// Load texture from image
	      try {
	         // Create a OpenGL Texture object from (URL, mipmap, file suffix)
	         // Use URL so that can read from JAR and disk file.
	         texture = TextureIO.newTexture(new File("data/startgame.png"), true);

	         // Use linear filter for texture if image is larger than the original texture
	         gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	         // Use linear filter for texture if image is smaller than the original texture
	         gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

	         // Texture image flips vertically. Shall use TextureCoords class to retrieve
	         // the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
	         TextureCoords textureCoords = texture.getImageTexCoords();
	         textureTop = textureCoords.top();
	         textureBottom = textureCoords.bottom();
	         textureLeft = textureCoords.left();
	         textureRight = textureCoords.right();
	         
	      } catch (GLException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	      
	   // Load texture from image
	      try {
	         // Create a OpenGL Texture object from (URL, mipmap, file suffix)
	         // Use URL so that can read from JAR and disk file.
	         texture2 = TextureIO.newTexture(new File("data/exitgame.png"), true);

	         // Use linear filter for texture if image is larger than the original texture
	         gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	         // Use linear filter for texture if image is smaller than the original texture
	         gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

	         // Texture image flips vertically. Shall use TextureCoords class to retrieve
	         // the top, bottom, left and right coordinates, instead of using 0.0f and 1.0f.
	         TextureCoords textureCoords = texture2.getImageTexCoords();
	         textureTop = textureCoords.top();
	         textureBottom = textureCoords.bottom();
	         textureLeft = textureCoords.left();
	         textureRight = textureCoords.right();
	         
	      } catch (GLException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	}

}
