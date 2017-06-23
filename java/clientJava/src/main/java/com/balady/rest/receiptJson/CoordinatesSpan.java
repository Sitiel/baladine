package com.balady.rest.receiptJson;

public class CoordinatesSpan {
	private float latitudeSpan;
	private float longitudeSpan;
	
	/**
	 * 
	 */
	public CoordinatesSpan() {
	}

	/**
	 * @param latitudeSPan
	 * @param longitudeSpan
	 */
	public CoordinatesSpan(float latitudeSpan, float longitudeSpan) {
		this.latitudeSpan = latitudeSpan;
		this.longitudeSpan = longitudeSpan;
	}

	/**
	 * @return the latitudeSPan
	 */
	public float getLatitudeSpan() {
		return latitudeSpan;
	}

	/**
	 * @param latitudeSPan the latitudeSPan to set
	 */
	public void setLatitudeSPan(float latitudeSpan) {
		this.latitudeSpan = latitudeSpan;
	}

	/**
	 * @return the longitudeSpan
	 */
	public float getLongitudeSpan() {
		return longitudeSpan;
	}

	/**
	 * @param longitudeSpan the longitudeSpan to set
	 */
	public void setLongitudeSpan(float longitudeSpan) {
		this.longitudeSpan = longitudeSpan;
	}
	
	
}
