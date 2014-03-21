package engine;

import static javax.media.opengl.GL.GL_TRIANGLE_STRIP;
import static javax.media.opengl.GL2ES1.GL_DECAL;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV;
import static javax.media.opengl.GL2ES1.GL_TEXTURE_ENV_MODE;

import javax.media.opengl.GL2;

import math.Vector3D;
import sound.Sound;

import com.jogamp.opengl.util.texture.Texture;

import framework.util.TextureUtils;

public class Explosion {
	final int MAX_PARTICLES= 1000;
	
	boolean	rainbow=true;				// Rainbow Mode?
    boolean	sp;							// Spacebar Pressed?
    boolean	rp;							// Enter Key Pressed?
 
    float	slowdown=2.0f;				// Slow Down Particles
    float	xspeed;						// Base X Speed (To Allow Keyboard Direction Of Tail)
    float	yspeed;						// Base Y Speed (To Allow Keyboard Direction Of Tail)
    float	zoom= 0.0f;				// Used To Zoom Out
 
    int	loop;						// Misc Loop Variable
    int	col;						// Current Color Selection
    int	delay;						// Rainbow Effect Delay
    Texture texture;					// Storage For Our Particle Texture
    Vector3D position;
    long duration, start_time, current_time;
    boolean active;
    Sound sound;
    public Explosion(Vector3D v){
    	position = v;
    	
    }
    class particles
    {
       boolean	active;					// Active (Yes/No)
       float	life;					// Particle Life
       float	fade;					// Fade Speed
       float	r;						// Red Value
       float	g;						// Green Value
       float	b;						// Blue Value
       float	x;						// X Position
       float	y;						// Y Position
       float	z;						// Z Position
       float	xi;						// X Direction
       float	yi;						// Y Direction
       float	zi;						// Z Direction
       float	xg;						// X Gravity
       float	yg;						// Y Gravity
       float	zg;						// Z Gravity
    }
 
    particles[] particle=new particles[1000];
 
    float colors[][]=
    {
       {1.0f,0.5f,0.5f},{1.0f,0.75f,0.5f},{1.0f,1.0f,0.5f},{0.75f,1.0f,0.5f},
       {0.5f,1.0f,0.5f},{0.5f,1.0f,0.75f},{0.5f,1.0f,1.0f},{0.5f,0.75f,1.0f},
       {0.5f,0.5f,1.0f},{0.75f,0.5f,1.0f},{1.0f,0.5f,1.0f},{1.0f,0.5f,0.75f}
    };
    
    public void initialize(GL2 gl, long timestep){
    	for (loop=0;loop<1000;loop++)				// Initials All The Textures
        {
           particle[loop]=new particles();
           particle[loop].active=true;								// Make All The Particles Active
           particle[loop].life=1.0f;								// Give All The Particles Full Life
           particle[loop].fade=(float)(100 * Math.random())/1000.0f+0.003f;	// Random Fade Speed
           particle[loop].r=colors[loop*(12/MAX_PARTICLES)][0];	// Select Red Rainbow Color
           particle[loop].g=colors[loop*(12/MAX_PARTICLES)][1];	// Select Red Rainbow Color
           particle[loop].b=colors[loop*(12/MAX_PARTICLES)][2];	// Select Red Rainbow Color
           particle[loop].xi=(float)((50 * Math.random())-26.0f)*10.0f;		// Random Speed On X Axis
           particle[loop].yi=(float)((50 * Math.random())-25.0f)*10.0f;		// Random Speed On Y Axis
           particle[loop].zi=(float)((50 * Math.random())-25.0f)*10.0f;		// Random Speed On Z Axis
           particle[loop].xg=0.0f;									// Set Horizontal Pull To Zero
           particle[loop].yg=0.0f;								// Set Vertical Pull Downward
           particle[loop].zg=0.0f;									// Set Pull On Z Axis To Zero
        }
    	texture = TextureUtils.loadImageAsTexture_FLIPPED(gl, "data/Particle.png");
    	TextureUtils.preferAnisotropicFilteringOnTextureTarget(gl, texture);
        gl.glTexEnvi(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_DECAL);
        start_time = System.currentTimeMillis();
        active = true;
        duration = timestep;
        
    }
    public void draw(GL2 gl){
    	gl.glPushMatrix();
    	gl.glTranslated(position.x, position.y, position.z);
	    for (loop=0;loop<MAX_PARTICLES;loop++)					// Loop Through All The Particles
	    {
	       if (particle[loop].active)							// If The Particle Is Active
	       {
	          float x=particle[loop].x;						// Grab Our Particle X Position
	          float y=particle[loop].y;						// Grab Our Particle Y Position
	          float z=particle[loop].z+zoom;					// Particle Z Pos + Zoom
	       
	       // Draw The Particle Using Our RGB Values, Fade The Particle Based On It's Life
	          gl.glColor4f(particle[loop].r,particle[loop].g,particle[loop].b,particle[loop].life);
	          
	          texture.enable(gl);
	          texture.bind(gl);
	          
	          gl.glBegin(GL_TRIANGLE_STRIP);						// Build Quad From A Triangle Strip
	          gl.glTexCoord2d(1,1); gl.glVertex3f(x+0.5f,y+0.5f,z); // Top Right
              gl.glTexCoord2d(0,1); gl.glVertex3f(x-0.5f,y+0.5f,z); // Top Left
              gl.glTexCoord2d(1,0); gl.glVertex3f(x+0.5f,y-0.5f,z); // Bottom Right
              gl.glTexCoord2d(0,0); gl.glVertex3f(x-0.5f,y-0.5f,z); // Bottom Left
	          gl.glEnd();										// Done Building Triangle Strip
	          texture.disable(gl);
	       
	          particle[loop].x+=particle[loop].xi/(slowdown*1000);// Move On The X Axis By X Speed
	          particle[loop].y+=particle[loop].yi/(slowdown*1000);// Move On The Y Axis By Y Speed
	          particle[loop].z+=particle[loop].zi/(slowdown*1000);// Move On The Z Axis By Z Speed
	       
	          particle[loop].xi+=particle[loop].xg;			// Take Pull On X Axis Into Account
	          particle[loop].yi+=particle[loop].yg;			// Take Pull On Y Axis Into Account
	          particle[loop].zi+=particle[loop].zg;			// Take Pull On Z Axis Into Account
	          particle[loop].life-=particle[loop].fade;		// Reduce Particles Life By 'Fade'
	       
	          if (particle[loop].life<0.0f)					// If Particle Is Burned Out
	          {
	             particle[loop].life=1.0f;					// Give It New Life
	             particle[loop].fade=(float)(100 * Math.random())/1000.0f+0.003f;	// Random Fade Value
	             particle[loop].x=0.0f;						// Center On X Axis
	             particle[loop].y=0.0f;						// Center On Y Axis
	             particle[loop].z=0.0f;						// Center On Z Axis
	             particle[loop].xi=xspeed+(float)((60 * Math.random())-32.0f);	// X Axis Speed And Direction
	             particle[loop].yi=yspeed+(float)((60 * Math.random())-30.0f);	// Y Axis Speed And Direction
	             particle[loop].zi=(float)((60 * Math.random())-30.0f);	// Z Axis Speed And Direction
	             particle[loop].r=colors[col][0];			// Select Red From Color Table
	             particle[loop].g=colors[col][1];			// Select Green From Color Table
	             particle[loop].b=colors[col][2];			// Select Blue From Color Table
	          }
	       }
	       
	    }
	    current_time = System.currentTimeMillis();
	    if (current_time-start_time>duration)
	    	   active = false;
	    gl.glPopMatrix();
    }
    public boolean isActive(){
    	return active;
    }
}
