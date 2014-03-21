package view;

import static constants.Constants.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import math.Vector3D;

import com.jogamp.opengl.util.FPSAnimator;

import controller.Controller;

@SuppressWarnings("serial")
public class MainWindow extends JFrame {
	
	public static void main(String[] args){
		// Run the GUI codes in the event-dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create the OpenGL rendering canvas
	            Canvas canvas = new Canvas();
	 
	            // Create a animator that drives canvas' display() at the specified FPS.
	            final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
	            
	            // General controller
	            Controller controller = new Controller(new Vector3D(30, 0, 0));
	            controller.setAnimator(animator);
	            controller.setSensivity(0.1);
	            
	            canvas.setController(controller);
	 
	            // Create the top-level container
	            final JFrame frame = new MainWindow(); // Swing's JFrame
	            frame.getContentPane().add(canvas);
	            
	            canvas.addKeyListener(controller);
	            canvas.addMouseListener(controller);
	            canvas.addMouseMotionListener(controller);
	            canvas.addMouseWheelListener(controller);
	            canvas.setFocusable(true);
	            controller.setWidthCanvas(canvas.getWidthCanvas());
	            controller.setHeightCanvas(canvas.getHeightCanvas());
	            frame.addWindowListener(new WindowAdapter() {
	            	@Override
	            	public void windowClosing(WindowEvent e) {
	            		// Use a dedicate thread to run the stop() to ensure that the
	            		// animator stops before program exits.
	            		new Thread() {
	            			@Override
	            			public void run() {
	            				if (animator.isStarted()) animator.stop();
	            				System.exit(0);
	            			}
	            		}.start();
	            	}
	            });
	            
	            frame.setTitle(TITLE);
	            frame.pack();
	            frame.setVisible(true);
	            animator.start(); // start the animation loop
			}
		});
	}
}
