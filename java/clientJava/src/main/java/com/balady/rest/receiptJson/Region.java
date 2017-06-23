package com.balady.rest.receiptJson;

public class Region {
	private CoordinatesReceipt center;
	private CoordinatesSpan span;
	
	/**
	 * 
	 */
	public Region() {
	}

	/**
	 * @param center
	 * @param span
	 */
	public Region(CoordinatesReceipt center, CoordinatesSpan span) {
		this.center = center;
		this.span = span;
	}

	/**
	 * @return the center
	 */
	public CoordinatesReceipt getCenter() {
		return center;
	}

	/**
	 * @param center the center to set
	 */
	public void setCenter(CoordinatesReceipt center) {
		this.center = center;
	}

	/**
	 * @return the span
	 */
	public CoordinatesSpan getspan() {
		return span;
	}

	/**
	 * @param span the span to set
	 */
	public void setspan(CoordinatesSpan span) {
		this.span = span;
	}
	
	
}
