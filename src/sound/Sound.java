package sound;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.jogamp.openal.AL;
import com.jogamp.openal.ALFactory;
import com.jogamp.openal.util.ALut;

public class Sound {
	static AL al;

	// Buffers hold sound data.
	private int[] buffer = new int[1];;
	
	private IntBuffer intBuffer;
	
	// Sources are points emitting sound.
	private int[] source = new int[1];
	
	private IntBuffer intBufferSource;
	
	// Position of the source sound.
    private float[] sourcePos = { 0.0f, 0.0f, 0.0f };
    
    private FloatBuffer floatBufferSourcePos;

    // Velocity of the source sound.
    private float[] sourceVel = { 0.0f, 0.0f, 0.0f };
    
    private FloatBuffer floatBufferSourceVel;

    // Position of the listener.
    private float[] listenerPos = { 0.0f, 0.0f, 0.0f };
    
    private FloatBuffer floatBufferListenerPos;

    // Velocity of the listener.
    private float[] listenerVel = { 0.0f, 0.0f, 0.0f };
    
    private FloatBuffer floatBufferListenerVel;

    // Orientation of the listener. (first 3 elements are "at", second 3 are "up")
    private float[] listenerOri = { 0.0f, 0.0f, -1.0f,  0.0f, 1.0f, 0.0f };
    
    private FloatBuffer floatBufferListenerOri;
    
    public Sound(String fileName){
    	al = ALFactory.getAL();
    	loadALData(fileName);
    	setListenerValues();
    }
    
    private int loadALData(String fileName) {

        // variables to load into
   
        int[] format = new int[1];
        int[] size = new int[1];
        ByteBuffer[] data = new ByteBuffer[1];
        int[] freq = new int[1];
        int[] loop = new int[1];

        // Load wav data into a buffer.
        intBuffer = IntBuffer.wrap(buffer);
        al.alGenBuffers(1, intBuffer);
        if (al.alGetError() != AL.AL_NO_ERROR)
            return AL.AL_FALSE;

        ALut.alutLoadWAVFile(fileName, format, data, size, freq, loop);
        al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
        
        // Bind buffer with a source.
        intBufferSource = IntBuffer.wrap(source);
        al.alGenSources(1, intBufferSource);

        if (al.alGetError() != AL.AL_NO_ERROR)
            return AL.AL_FALSE;

        al.alSourcei (source[0], AL.AL_BUFFER,   buffer[0]   );
        al.alSourcef (source[0], AL.AL_PITCH,    1.0f     );
        al.alSourcef (source[0], AL.AL_GAIN,     1.0f     );
        floatBufferSourcePos = FloatBuffer.wrap(sourcePos);
        al.alSourcefv(source[0], AL.AL_POSITION, floatBufferSourcePos);
        floatBufferSourceVel = FloatBuffer.wrap(sourceVel);
        al.alSourcefv(source[0], AL.AL_VELOCITY, floatBufferSourceVel);
        al.alSourcei (source[0], AL.AL_LOOPING,  loop[0]     );
        
        // Do another error check and return.
        if(al.alGetError() == AL.AL_NO_ERROR)
            return AL.AL_TRUE;

        return AL.AL_FALSE;
    }
    private void setListenerValues() {
    	floatBufferListenerPos = FloatBuffer.wrap(listenerPos);
        al.alListenerfv(AL.AL_POSITION,	floatBufferListenerPos);
        floatBufferListenerVel = FloatBuffer.wrap(listenerVel);
        al.alListenerfv(AL.AL_VELOCITY,    floatBufferListenerVel);
        floatBufferListenerOri = FloatBuffer.wrap(listenerOri);
        al.alListenerfv(AL.AL_ORIENTATION, floatBufferListenerOri);
    }
    public void play(){
    	al.alSourcePlay(source[0]);
    }
    public void killALData() {
        al.alDeleteBuffers(1, intBuffer);
        al.alDeleteSources(1, intBufferSource);
        ALut.alutExit();
    }
    public static void init(){
    	ALut.alutInit();
    }
}
