package com.balady.rest.receiptJson;

import java.util.List;

public class PlayerInfo {
	float cash;
	int sales;
	float profit;
	//private List<DrinkInfo> drinksOffered;
	
	/**
	 * 
	 */
	public PlayerInfo() {
	}

	/**
	 * @param cash
	 * @param sales
	 * @param profit
	 * @param drinksOffered
	 */
	public PlayerInfo(float cash, int sales, float profit) {
		this.cash = cash;
		this.sales = sales;
		this.profit = profit;
		//this.drinksOffered = drinksOffered;
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

	/*public List<DrinkInfo> getDrinksOffered() {
		return drinksOffered;
	}

	public void setDrinksOffered(List<DrinkInfo> drinksOffered) {
		this.drinksOffered = drinksOffered;
	}*/
	
}
