package edu.wireless.normal;

import java.util.ArrayList;

import edu.wireless.normal.Node;

public class SimulationTest {
	
	public static final int NUMBER_OF_NODES = 7;
	public static final int NUMBER_OF_TRANSMISSIONS = 100;
	public static final int NUMBER_OF_TIME_SLOTS = 100000;
	public static final boolean backOffRepair = false;
	public static final int NUM_BAD_FISH = 1;
	public static final double BAD_FISH_SPEED = 5.5;
	public static final double STANDARD_SPEED = 11;
	
	public static final int PACKET_SIZE = 2048;
	public static final int STANDARD_BIT_RATE = 11000000;


	public static void main(String[] args)
	{
		
		
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		
		Simulation sim = new Simulation();
		timeSlots = sim.simulation(NUMBER_OF_TIME_SLOTS);
		
		int collisions = 0;
		int idle = 0;
		int data = 0;
		int control = 0;
		int badFishCount = 0;
		int standardCount = 0;
		
		
		double slotThroughPut = 0;
		
		for(int i = 0; i < NUMBER_OF_TIME_SLOTS; i++){
			
			if(timeSlots.get(i).isCollision()){
				collisions++;
			}
			if (timeSlots.get(i).isIdle()){
				idle++;
			}
			if (timeSlots.get(i).isControl()){
				control++;
			}
			if(timeSlots.get(i).isData()){
				
				data++;
				//System.out.println("Bits in slot " + timeSlots.get(i).getBitsInSlot());
				slotThroughPut = slotThroughPut + (timeSlots.get(i).getBitsInSlot());
				
				//System.out.println("Slot Speed " + timeSlots.get(i).getSlotSpeed()/1000000);
				
				if(timeSlots.get(i).getSlotSpeed()/1000000 == BAD_FISH_SPEED){
					badFishCount++;
				}
				else if(timeSlots.get(i).getSlotSpeed()/1000000 == STANDARD_SPEED){
					standardCount++;
				}
				else{
					System.out.println("I SHALL BE DAMNED IF THE CODE GETS HERE!");
				}
				
			}			
			
		}
		
		
		System.out.println("Collisions = " + collisions);
		System.out.println("Data = " + data);
		System.out.println("Idle = " + idle);
		System.out.println("Control = " + control);
		
		System.out.println("Total " + (collisions + idle + data + control));
		
		
		System.out.println(" Bad Fish " + badFishCount + " Standard " + standardCount 
								+ " Total " + (badFishCount + standardCount));		

		System.out.println("ThroughPut % " + slotThroughPut / (NUMBER_OF_TIME_SLOTS * PACKET_SIZE));
		
	}
}
