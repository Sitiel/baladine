package com.balady.data;

import java.util.List;

public class Player {
	private String name;
	private float cash;
	private float profit;
	private int sales;
	private List<Zone> zones;
	private List<Drink> drinks;

	/**
	 * @param name
	 * @param cash
	 * @param profit
	 * @param sales
	 * @param zones
	 * @param drinks
	 */
	public Player(String name, float cash, float profit, int sales, List<Zone> zones, List<Drink> drinks) {
		this.name = name;
		this.cash = cash;
		this.profit = profit;
		this.sales = sales;
		this.zones = zones;
		this.drinks = drinks;
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
	 * @return the cash
	 */
	public float getCash() {
		return cash;
	}


	/**
	 * @param cash the cash to set
	 */
	public void setCash(float cash) {
		this.cash = cash;
	}


	/**
	 * @return the profit
	 */
	public float getProfit() {
		return profit;
	}


	/**
	 * @param profit the profit to set
	 */
	public void setProfit(float profit) {
		this.profit = profit;
	}


	/**
	 * @return the sales
	 */
	public int getSales() {
		return sales;
	}


	/**
	 * @param sales the sales to set
	 */
	public void setSales(int sales) {
		this.sales = sales;
	}


	/**
	 * @return the zones
	 */
	public List<Zone> getZones() {
		return zones;
	}


	/**
	 * @param zones the zones to set
	 */
	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}


	/**
	 * @return the drinks
	 */
	public List<Drink> getDrinks() {
		return drinks;
	}


	/**
	 * @param drinks the drinks to set
	 */
	public void setDrinks(List<Drink> drinks) {
		this.drinks = drinks;
	}
}
