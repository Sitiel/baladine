package com.balady.rest.receiptJson;

public class PlayerInfo {
	float cash;
	int sales;
	float profit;
	
	/**
	 * 
	 */
	public PlayerInfo() {
	}

	/**
	 * @param cash
	 * @param sales
	 * @param profit
	 */
	public PlayerInfo(float cash, int sales, float profit) {
		this.cash = cash;
		this.sales = sales;
		this.profit = profit;
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
}
