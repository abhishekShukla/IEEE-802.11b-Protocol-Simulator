package edu.wireless.normal;

import java.util.ArrayList;

public class TimeSlot {
	
	//Indicate successful transmission
	private boolean data;
	
	//Indicate successful transmission
	private boolean control;

	//Indicate collision
	private boolean collision;
	
	//Indicate idle
	private boolean idle;
	
	//Number of bits transmitted in this slot
	private double bitsInSlot;
	
	//Slot speed
	private double slotSpeed;
	
	//Nodes for each time slot
	public ArrayList<Node> nodes = new ArrayList<Node>();
	
	public TimeSlot(boolean success, boolean collision, boolean idle,
			ArrayList<Node> nodes) {
		super();
		this.data = success;
		this.collision = collision;
		this.idle = idle;
		this.nodes = nodes;
		this.bitsInSlot = 0;
		this.slotSpeed = 0;
	}
	
	public boolean isControl() {
		return control;
	}

	public void setControl(boolean control) {
		this.control = control;
	}
	
	public double getSlotSpeed() {
		return slotSpeed;
	}

	public void setSlotSpeed(double slotSpeed) {
		this.slotSpeed = slotSpeed;
	}

	public double getBitsInSlot() {
		return bitsInSlot;
	}

	public void setBitsInSlot(double bitsInSlot) {
		this.bitsInSlot = bitsInSlot;
	}

	public boolean isData() {
		return data;
	}

	public void setData(boolean data) {
		this.data = data;
	}

	public boolean isCollision() {
		return collision;
	}

	public void setCollision(boolean collision) {
		this.collision = collision;
	}

	public boolean isIdle() {
		return idle;
	}

	public void setIdle(boolean idle) {
		this.idle = idle;
	}	
	
}
