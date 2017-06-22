package com.balady.population;

import com.balady.data.Coordinates;

public class Consumer {
	
	private Coordinates coordinates;

	/**
	 * @param coordinates
	 */
	public Consumer(float xMin,float xMax,float yMin,float yMax) {
		float x = (xMax-xMin)*(float) Math.random();
		float y = (yMax-yMin)*(float) Math.random();
		coordinates = new Coordinates(x,y);
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
}
