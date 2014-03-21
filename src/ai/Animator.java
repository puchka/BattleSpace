package ai;

import math.Vector3D;
import engine.Spacecraft;
import engine.Torpedo;
import java.util.ArrayList;

public class Animator {
	Spacecraft spacecraft;
	Vector3D initialPos;
	double vy;
	
	ArrayList<Torpedo> torpedos;
	long movingTime;
	
	public Animator(Spacecraft spacecraft){
		this.spacecraft = spacecraft;
		initialPos = new Vector3D(spacecraft.getPosition());
		vy = 0.02;
		movingTime = 0;
		
	}
	public void run(long timestep){
		movingTime+=timestep;
		if (Math.abs(spacecraft.getPosition().y)>10){
			vy = -vy;
			
		}
		
		if (vy*timestep<10)
			spacecraft.setPosition(Vector3D.add(spacecraft.getPosition(),
								   new Vector3D(0,	vy*timestep, 0)));
		
		if (movingTime>20000){
			for (int i=0;i<torpedos.size();i++){
				if (torpedos.get(i).getState()==1){
					// find the first active torpedo
					
					torpedos.get(0).setVelocity(0.1);
					torpedos.get(0).setState(2); // moving torpedo
					
					if ((i+1)<torpedos.size())
						// active the next torpedo
						torpedos.get(i+1).setState(1);
					
					movingTime = 0;
					
					break;
					
				}
			}
			
		}
		
	}
	
	public void setSpacecraft(Spacecraft spacecraft){
		this.spacecraft = spacecraft;
	}
	
	public void setTorpedos(ArrayList<Torpedo> torpedos){
		this.torpedos = torpedos;
	}
	
}
