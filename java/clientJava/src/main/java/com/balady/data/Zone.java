package com.balady.data;

import com.balady.data.utils.TypeZone;

public class Zone {
	
	private float influence;
	private TypeZone typeZone;
	private Coordinates coordinates;

	/**
	 * @param influence
	 * @param typeZone
	 * @param coordinates
	 */
	public Zone(float influence, TypeZone typeZone, Coordinates coordinates) {
		this.influence = influence;
		this.typeZone = typeZone;
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
	 * @return the typeZone
	 */
	public TypeZone getTypeZone() {
		return typeZone;
	}

	/**
	 * @param typeZone the typeZone to set
	 */
	public void setTypeZone(TypeZone typeZone) {
		this.typeZone = typeZone;
	}

	/**
	 * Test if Coordiantes c are in Influence of ObjectPlayer
	 * @param c
	 * @return true if is in Influencce
	 */
	public boolean isInInfluence(Coordinates c) {
		// TODO
		return false;
	}
}
