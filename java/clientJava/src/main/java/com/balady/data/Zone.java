package com.balady.data;


import com.balady.population.Consumer;

public class Zone {
	
	private float influence;
	private Coordinates coordinates;

	/**
	 * @param influence
	 * @param typeZone
	 * @param coordinates
	 */
	public Zone(float influence, Coordinates coordinates) {
		this.influence = influence;
		this.coordinates = coordinates;
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
