package com.balady.data;


import com.balady.population.Consumer;

public class Zone {
	
	private float influence;
	private Coordinates coordinates;
	private Player owner;

	/**
	 * @param influence
	 * @param coordinates
	 * @param owner
	 */
	public Zone(float influence, Coordinates coordinates) {
		this.influence = influence;
		this.coordinates = coordinates;
		this.owner = null;
	}

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}



	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}



	/**
	 * @return the coordinates
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}



	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}



	/**
	 * @return the influence
	 */
	public float getInfluence() {
		return influence;
	}

	/**
	 * @param influence the influence to set
	 */
	public void setInfluence(float influence) {
		this.influence = influence;
	}

	/**
	 * Test if Coordiantes c are in Influence of ObjectPlayer
	 * @param c
	 * @return true if is in Influencce
	 */
	public boolean isInInfluence(Consumer c) {
		double tmp = Math.sqrt((c.getCoordinates().getX()-this.getCoordinates().getX())*(c.getCoordinates().getX()-this.getCoordinates().getX())+(c.getCoordinates().getY()-this.getCoordinates().getY())*(c.getCoordinates().getY()-this.getCoordinates().getY()));
		boolean res = tmp < this.getInfluence();
		return res;
	}
}
