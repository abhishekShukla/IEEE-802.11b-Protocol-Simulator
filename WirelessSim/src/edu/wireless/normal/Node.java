package edu.wireless.normal;

import java.util.Random;
import java.lang.Math;

public class Node {
	
	private String nodeName;
	
	private int backoff;
	private int sendAttempts;
	private int contWindow;
	
	public static final int CMIN = 31;
	public static final int CMAX = 1023;

	private int packetsToSend;
	private int packetSize;
	
	//Indicate if it was a control packet
	private boolean controlPacket;
	
	//Indicate if it was data packet
	private boolean data;
	
	//Speed in MBPS
	private double bitsPerSec;   

	//Time required to send one packet for
	private double timePerPacket;  
	
	//To Indicate Contention Window. Static array so that it is a part of the class
	public static int[] contWindowIndicator = new int[SimulationTest.NUMBER_OF_NODES];
	
	public Node()
	{
		
		nodeName = "";
		backoff = 0;
		sendAttempts = 0;
		contWindow = CMIN; 
		packetsToSend = 0;
		bitsPerSec = 0.0;
		controlPacket = false;
		data = false;

	}
	
	public static int[] getContWindowIndicator() {
		return contWindowIndicator;
	}

	public static void setContWindowIndicator() {
		
		for(int i = 0; i < Node.contWindowIndicator.length; i++){
			Node.contWindowIndicator[i] = 0;
		}
		
	}

	/**
	Helper method to calculate sender backoff.
	@param sender The Node that wants to send a file
	@return	1 if MAX contention window/backoff value has been reached
			0 if new contention window and back off are calculated
	*/
	
	public int performBackoff(boolean backOffRepair)
	{
				
		System.out.println("Window " + contWindow + "NodeName " + nodeName);

			
		contWindowIndicator[Integer.parseInt(nodeName)]++;
		sendAttempts = contWindowIndicator[Integer.parseInt(nodeName)];
	
		// CALCULATION FOR Backoff/contention window
		
		contWindow = (int)Math.pow(2,sendAttempts) - 1;		// 2^n - 1
		
		if(backOffRepair){
			
			contWindow = (int)(contWindow * SimulationTest.STANDARD_SPEED / (bitsPerSec/1000000));
			
		}
		
		Random rand = new Random();
		int k;
		if(contWindow < CMAX){
			k = rand.nextInt(contWindow);
		}
		else{
			k = rand.nextInt(CMAX);
		}

		System.out.println("BackOff value = " + k);
		backoff = k;	
		
		return backoff;
						
	}
	
	public double calculateTimeSlots(int randNumOfPackets){
		
		double numOfTimeSlotsRequired = 0;
		
		numOfTimeSlotsRequired = randNumOfPackets * SimulationTest.STANDARD_BIT_RATE / bitsPerSec; 
		
		return numOfTimeSlotsRequired;
	}
	
	public double calculateBitsInSlot(){
		
		double bitsInSlot = 0;
		
		bitsInSlot = SimulationTest.PACKET_SIZE * bitsPerSec / SimulationTest.STANDARD_BIT_RATE;
		
		return bitsInSlot;
		
	}
	
	public void reset(String name){
		
		this.backoff = 0;
		this.sendAttempts = 0;
		this.contWindow = CMIN; 
		this.packetsToSend = 0;
		//Node.contWindowIndicator[Integer.parseInt(nodeName)] = 0; 
		
	}
	
	public boolean isControlPacket() {
		return controlPacket;
	}

	public void setControlPacket(boolean controlPacket) {
		this.controlPacket = controlPacket;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public void setNodeName(String name) {
		this.nodeName = name;
	}
	
	public double getBackoff() {
		return backoff;
	}
	
	public void setBackoff(int backoff) {
		this.backoff = backoff;
	}
	
	public int getSendAttempts() {
		return sendAttempts;
	}
	
	public void setSendAttempts(int sendAttempts) {
		this.sendAttempts = sendAttempts;
	}
	
	public int getContWindow() {
		return contWindow;
	}

	public void setContWindow(int contWindow) {
		this.contWindow = contWindow;
	}
	
	public double getBitsPerSec() {
		return bitsPerSec;
	}

	public void setBitsPerSec(double bitsPerSec) {
		
		//System.out.println(bitsPerSec * 1000000);
		this.bitsPerSec = bitsPerSec * 1000000;
		
		//Set Time to transmit each packet one bits/second is set
		//System.out.println("Time per packet = " + (Simulation.PACKET_SIZE / this.bitsPerSec));
		setTimePerPacket(SimulationTest.PACKET_SIZE / this.bitsPerSec);
		
	}
	
	public double getTimePerPacket() {
		return timePerPacket;
	}

	public void setTimePerPacket(double timePerPacket) {
		this.timePerPacket = timePerPacket;
	}
	
	public int getPacketSize() {
		return packetSize;
	}

	public void setPacketSize(int packetSize) {
		this.packetSize = packetSize;
	}
	
	public int getPacketsToSend() {
		return packetsToSend;
	}
	
	public void setPacketsToSend(int packetsToSend) {
		this.packetsToSend = packetsToSend;
	}	
	
}
