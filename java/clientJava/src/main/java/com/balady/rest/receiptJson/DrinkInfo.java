package com.balady.rest.receiptJson;

public class DrinkInfo {
	private String name;
	private float price;
	private boolean hasAlcohol;
	private boolean isCold;
	
	/**
	 * 
	 */
	public DrinkInfo() {
	}

	/**
	 * @param name
	 * @param price
	 * @param hasAlcohol
	 * @param isCold
	 */
	public DrinkInfo(String name, float price, boolean hasAlcohol, boolean isCold) {
		this.name = name;
		this.price = price;
		this.hasAlcohol = hasAlcohol;
		this.isCold = isCold;
	}



	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	public boolean isHasAlcohol() {
		return hasAlcohol;
	}

	public void setHasAlcohol(boolean hasAlcohol) {
		this.hasAlcohol = hasAlcohol;
	}

	/**
	 * @return the isCold
	 */
	public boolean isCold() {
		return isCold;
	}

	/**
	 * @param isCold the isCold to set
	 */
	public void setIsCold(boolean isCold) {
		this.isCold = isCold;
	}
	
	
}
