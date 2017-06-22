package com.balady.rest.receiptJson;

public class Forecast {
	private int dfn;
	private String weather;
	
	/**
	 * @param dfn
	 * @param weather
	 */
	public Forecast(int dfn, String weather) {
		this.dfn = dfn;
		this.weather = weather;
	}

	/**
	 * 
	 */
	public Forecast() {
	}

	/**
	 * @return the dfn
	 */
	public int getDfn() {
		return dfn;
	}

	/**
	 * @param dfn the dfn to set
	 */
	public void setDfn(int dfn) {
		this.dfn = dfn;
	}

	/**
	 * @return the weather
	 */
	public String getWeather() {
		return weather;
	}

	/**
	 * @param weather the weather to set
	 */
	public void setWeather(String weather) {
		this.weather = weather;
	}
	
	
}
