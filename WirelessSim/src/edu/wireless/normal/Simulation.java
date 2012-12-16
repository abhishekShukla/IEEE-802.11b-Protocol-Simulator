package edu.wireless.normal;

import java.util.ArrayList;
import java.util.Random;

import edu.wireless.normal.Node;

public class Simulation {

	
	public void initializeTimeSlots(ArrayList<TimeSlot> timeSlots, int numOfTimeSlots){
		
		for(int i = 0; i < numOfTimeSlots; i++){
			
			ArrayList<Node> sendingNodes = new ArrayList<Node>();
			
			for(int j = 0; j < SimulationTest.NUMBER_OF_NODES; j++){
				
				Node n = new Node();
				Node.setContWindowIndicator();
				
				if(j < SimulationTest.NUM_BAD_FISH){
					
					n.setNodeName(Integer.toString(j));
					n.setBitsPerSec(SimulationTest.BAD_FISH_SPEED);
					n.setPacketSize(2048);
					n.setPacketsToSend(SimulationTest.NUMBER_OF_TRANSMISSIONS);
					sendingNodes.add(n);
					
				}
				else{
			
					n.setNodeName(Integer.toString(j));
					n.setBitsPerSec(SimulationTest.STANDARD_SPEED);
					n.setPacketSize(2048);
					n.setPacketsToSend(SimulationTest.NUMBER_OF_TRANSMISSIONS);
					sendingNodes.add(n);
					
				}
				
			}
			
			TimeSlot timeSlot = new TimeSlot(false, false, true, sendingNodes);
			timeSlots.add(timeSlot);
		}
		
	}
	
	// Generates the number of hosts transmitting
	private int generateRandNumHosts(int numOfHosts){
		
		int randNumOfHostsTransmitting = 0;
		Random rand = new Random();
		
		for(int i = 0; i < SimulationTest.NUMBER_OF_NODES; i++){
			
			
			int randomNumber = rand.nextInt(2);
			System.out.println(randomNumber);
			
			if(randomNumber == 1){
				
				
				randNumOfHostsTransmitting++;
				
			}
			
		}
		/*
		Random rand = new Random();
		
		//numberOfHosts transmitting + no host transmitting
		int randNumOfHostsTransmitting = rand.nextInt(numOfHosts + 1);
		
		*/
		return randNumOfHostsTransmitting;
		
	}
	
	/**
	Performs simulation.
	@param sendingNodes The source Nodes.
	*/
	
	public ArrayList<TimeSlot> simulation(int numOfTimeSlots){
		
		
		ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
		initializeTimeSlots(timeSlots, numOfTimeSlots);
		//For each time slot
		
		for(int i = 0; i < numOfTimeSlots; i++){
			
			System.out.println("----------------------------IN TIME SLOT " + i + " --------------------------------");
			
			//Random number generator to see how many hosts might transmit
			int randNumHostsTransmitting = generateRandNumHosts(timeSlots.get(i).nodes.size());
			
			/* After this number is obtained, use random number generator
			 * to see which are the nodes that are transmitting
			 */
			System.out.println("Number of Hosts Transmitting " + randNumHostsTransmitting);
			
			/*
			generateSimData(randNumHostsTransmitting, timeSlots.get(i).nodes.size(), 
								timeSlots.get(i), timeSlots, i);
			*/
			
			timeSlots = generateData(randNumHostsTransmitting, timeSlots.get(i).nodes.size(), 
					timeSlots.get(i), timeSlots, i);
			
		}
		
		return timeSlots;
		
	}
	
	private ArrayList<TimeSlot> generateData (int numOfHostsTransmitting, int numOfHosts, 
			 TimeSlot timeSlot, ArrayList<TimeSlot> timeSlots, int currentTimeSlot){
		
		boolean dataBeingTransmitted = false;
		
		if(timeSlot.isCollision()){
			System.out.println("collision");
		}
		if(timeSlot.isData()){
			System.out.println("data");
			dataBeingTransmitted = true;
		}
		if(timeSlot.isControl()){
			System.out.println("control");
		}
		if(timeSlot.isIdle()){
			System.out.println("idle");
		}
		
		for(int k = 0; k < timeSlot.nodes.size(); k++){
			
			if(timeSlot.nodes.get(k).isData()){
				
				System.out.println("Data2");
				dataBeingTransmitted = true;
				
			}
			
		}
		

		
		/* 
		 * if the number of transmitting hosts are more than 0,
		 * alter the state of only those hosts that are already 
		 * not doing anything (because of backoff)
		 */
		if(numOfHostsTransmitting != 0){
			
			int i = 0;
			while(i < numOfHostsTransmitting){
				//System.out.println("Here");
				//if all hosts are transmitting, then no point of the exercise
				int hostsTransmitting = 0;
				for(int k = 0; k < timeSlot.nodes.size(); k++){
					
					if(timeSlot.nodes.get(k).isData()){
						dataBeingTransmitted = true;
						System.out.println("DataBeingTransMitted");
						break;
					}
					
					if(timeSlot.nodes.get(k).isControlPacket()){
						hostsTransmitting++;
					}
					
				}
				if(dataBeingTransmitted){
					System.out.println("DataBeingTransMitted");
					break;
				}
				
				//System.out.println("Number Hosts Transmitting Now " + hostsTransmitting);
				if(hostsTransmitting == SimulationTest.NUMBER_OF_NODES){
					break;
				}
				
				Random rand = new Random();
				int hostIndex = rand.nextInt(numOfHosts);
				
				//System.out.println("i " + i + " Host Index " + hostIndex);
				
				if(timeSlot.nodes.get(hostIndex).isControlPacket() == false && 
				   timeSlot.nodes.get(hostIndex).isData() == false){
					
					//System.out.println("NO of transmitting " + numOfHostsTransmitting + " Rand " + hostIndex);
					
					timeSlot.nodes.get(hostIndex).setControlPacket(true);
					timeSlot.nodes.get(hostIndex).setData(false);
					
					i++;
				}
				
			}
			
		}
		else{
			if(dataBeingTransmitted == true){
				timeSlot.setIdle(false);
				timeSlot.setCollision(false);
				timeSlot.setData(true);
				timeSlot.setControl(false);
			}
			System.out.println("DataBeingTransMitted");
		}
		
		/*
		 * Now Lets Analyze this data and build our future time slots
		 */
		
		int controlPacketIndicator = 0; 
				
		for(int i = 0; i < timeSlot.nodes.size(); i++){
			
			if(timeSlot.nodes.get(i).isControlPacket() && timeSlot.nodes.get(i).isData() != true){
				controlPacketIndicator++;
			}
			
		}
		
		if(controlPacketIndicator == 0 && dataBeingTransmitted == false){
			System.out.println("Idle State");
			
			//These events can be successfully recorded
			timeSlot.setIdle(true);
			timeSlot.setCollision(false);
			timeSlot.setData(false);
			timeSlot.setControl(false);
			
		}
		else if(controlPacketIndicator == 1){
			System.out.println("Only one transmitted control signal");
			
			//I am allowed to send packets
			timeSlot.setIdle(false);
			timeSlot.setCollision(false);
			timeSlot.setData(false);
			timeSlot.setControl(true);

			int findContNode = 0;
			//Now find the node that is transmitting control data
			for(findContNode = 0; findContNode < timeSlot.nodes.size(); findContNode++){
				
				if(timeSlot.nodes.get(findContNode).isControlPacket()){
					System.out.println("Transmitting host " + timeSlot.nodes.get(findContNode).getNodeName());
					break;
				}
				
			}
			
			Random rand = new Random();
			int randNumOfPackets = rand.nextInt(SimulationTest.NUMBER_OF_TRANSMISSIONS);
			
			double numOfSlots = timeSlot.nodes.get(findContNode).calculateTimeSlots(randNumOfPackets);  
			System.out.println("Number of slots " + numOfSlots);
			double bitsInSlot = timeSlot.nodes.get(findContNode).calculateBitsInSlot();
			System.out.println("Number of bits in each slot " + bitsInSlot);
			
			System.out.println("------TRANSMITTING PACKETS " + randNumOfPackets);
			for(int j = 1; j <= numOfSlots; j++){
				System.out.println("J " + j);
				//To not cross time slots boundary
				if((currentTimeSlot + j) < SimulationTest.NUMBER_OF_TIME_SLOTS){
					timeSlots.get(currentTimeSlot + j).nodes.get(findContNode).setData(true);
					timeSlots.get(currentTimeSlot + j).setData(true);
					timeSlots.get(currentTimeSlot + j).setIdle(false);
					timeSlots.get(currentTimeSlot + j).setCollision(false);
					timeSlots.get(currentTimeSlot + j).setControl(false);
					timeSlots.get(currentTimeSlot + j).setBitsInSlot(bitsInSlot);
					timeSlots.get(currentTimeSlot + j).setSlotSpeed(timeSlot.nodes.get(findContNode).getBitsPerSec());
					timeSlots.get(currentTimeSlot + j).nodes.get(findContNode)
									 .reset(timeSlots.get(currentTimeSlot + j).nodes.get(findContNode).getNodeName());
				}
				
			}
			
		}
		else if(controlPacketIndicator >= 1){
			System.out.println("More than one host transmitted control signal");
			
			//These events can be successfully recorded
			timeSlot.setIdle(false);
			timeSlot.setCollision(true);
			timeSlot.setData(false);
			timeSlot.setControl(false);
			
			//Case where more than one host is transmitting
			for(int i = 0; i < timeSlot.nodes.size(); i++){
				
				if(timeSlot.nodes.get(i).isControlPacket()){
					
					//Backoff on all nodes that transmitted a control packet
					int backOff = timeSlot.nodes.get(i).performBackoff(SimulationTest.backOffRepair);
					timeSlot.nodes.get(i).setBackoff(backOff);
					
					//to take care of time slots overflow
					if(currentTimeSlot + backOff < SimulationTest.NUMBER_OF_TIME_SLOTS){
						
						timeSlots.get(currentTimeSlot + backOff).nodes.get(i).setControlPacket(true);
						
					}
					
				}
				
			}
			
		}
		else if(dataBeingTransmitted == true){
			timeSlot.setIdle(false);
			timeSlot.setCollision(false);
			timeSlot.setData(true);
			timeSlot.setControl(false);
		}
		else{
			System.out.println("I SHALL BE DAMNED IF THE CODE GETS HERE!");
		}
	
		return timeSlots;
	}	
}
