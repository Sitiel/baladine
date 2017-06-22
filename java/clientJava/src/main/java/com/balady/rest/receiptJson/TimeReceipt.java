package com.balady.rest.receiptJson;

import java.util.List;

public class TimeReceipt {
	private int timestamp;
	private List<Forecast> weather;
	
	/**
	 * @param timeStamp
	 * @param weather
	 */
	public TimeReceipt(int timestamp, List<Forecast> weather) {
		this.timestamp = timestamp;
		this.weather = weather;
	}

	/**
	 * 
	 */
	public TimeReceipt() {
	}

	/**
	 * @return the timeStamp
	 */
	public int getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the weather
	 */
	public List<Forecast> getWeather() {
		return weather;
	}

	/**
	 * @param weather the weather to set
	 */
	public void setWeather(List<Forecast> weather) {
		this.weather = weather;
	}
}
