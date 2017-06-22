package com.balady.data;

public class Drink {
	private String name;
	private float cost;
	private boolean hasAlcholize;
	private boolean isCold;

	/**
	 * @param name
	 * @param cost
	 * @param hasAlcholize
	 * @param isCold
	 */
	public Drink(String name, float cost, boolean hasAlcholize, boolean isCold) {
		this.name = name;
		this.cost = cost;
		this.hasAlcholize = hasAlcholize;
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
	 * @return the hasAlcholize
	 */
	public boolean isHasAlcholize() {
		return hasAlcholize;
	}

	/**
	 * @param hasAlcholize the hasAlcholize to set
	 */
	public void setHasAlcholize(boolean hasAlcholize) {
		this.hasAlcholize = hasAlcholize;
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
