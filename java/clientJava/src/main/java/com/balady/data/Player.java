package com.balady.data;

import java.util.List;

public class Player {
	private String name;
	private float cash;
	private float profit;
	private int sales;
	private List<Zone> pubs;
	private Zone stand;
	private List<Drink> drinks;
	/**
	 * @param name
	 * @param cash
	 * @param profit
	 * @param sales
	 * @param pubs
	 * @param stand
	 * @param drinks
	 */
	public Player(String name, float cash, float profit, int sales, List<Zone> pubs, Zone stand, List<Drink> drinks) {
		this.name = name;
		this.cash = cash;
		this.profit = profit;
		this.sales = sales;
		this.pubs = pubs;
		this.stand = stand;
		this.drinks = drinks;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public float getCash() {
		return cash;
	}
	public void setCash(float cash) {
		this.cash = cash;
	}
	public float getProfit() {
		return profit;
	}
	public void setProfit(float profit) {
		this.profit = profit;
	}
	public int getSales() {
		return sales;
	}
	public void setSales(int sales) {
		this.sales = sales;
	}
	public List<Zone> getPubs() {
		return pubs;
	}
	public void setPubs(List<Zone> pubs) {
		this.pubs = pubs;
	}
	public Zone getStand() {
		return stand;
	}
	public void setStand(Zone stand) {
		this.stand = stand;
	}
	public List<Drink> getDrinks() {
		return drinks;
	}
	public void setDrinks(List<Drink> drinks) {
		this.drinks = drinks;
	}

	
}
