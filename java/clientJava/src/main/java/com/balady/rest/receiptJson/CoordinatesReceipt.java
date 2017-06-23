package com.balady.rest.receiptJson;

public class CoordinatesReceipt {
	private float latitude;
	private float longitude;
	
	/**
	 * 
	 */
	public CoordinatesReceipt() {
	}

	/**
	 * @param latitude
	 * @param longitude
	 */
	public CoordinatesReceipt(float latitude, float longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * @return the latitude
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public float getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	
}
