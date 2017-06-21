package com.balady.data;

import java.util.List;

public class Player {
	private float cash;
	private float profit;
	private int sales;
	private List<Zone> zones;
	
	
	/**
	 * @param cash
	 * @param profit
	 * @param sales
	 * @param pubs
	 */
	public Player(float cash, float profit, int sales, List<Zone> zones) {
		this.cash = cash;
		this.profit = profit;
		this.sales = sales;
		this.zones = zones;
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
}
