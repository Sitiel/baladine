package com.balady.data;

public class Drink {
	private String name;
	private float cost;
	private boolean isAlcholize;
	private boolean isCold;
	
	/**
	 * @param name
	 * @param cost
	 * @param isAlcholize
	 * @param isCold
	 */
	public Drink(String name, float cost, boolean isAlcholize, boolean isCold) {
		this.name = name;
		this.cost = cost;
		this.isAlcholize = isAlcholize;
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
	 * @return the cost
	 */
	public float getCost() {
		return cost;
	}

	/**
	 * @param cost the cost to set
	 */
	public void setCost(float cost) {
		this.cost = cost;
	}

	/**
	 * @return the isAlcholize
	 */
	public boolean isAlcholize() {
		return isAlcholize;
	}

	/**
	 * @param isAlcholize the isAlcholize to set
	 */
	public void setAlcholize(boolean isAlcholize) {
		this.isAlcholize = isAlcholize;
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
	public void setCold(boolean isCold) {
		this.isCold = isCold;
	}
}
