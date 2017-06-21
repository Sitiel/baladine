package com.balady.data;

import com.balady.data.utils.TypeZone;

public class Zone {
	
	private float influence;
	private TypeZone typeZone;

	/**
	 * @param influence
	 * @param typeZone
	 */
	public Zone(float influence, TypeZone typeZone) {
		this.influence = influence;
		this.typeZone = typeZone;
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
