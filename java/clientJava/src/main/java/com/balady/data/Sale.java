package com.balady.data;

public class Sale {
	private String player;
	private String item;
	private int quantity;

	/**
	 * @param player
	 * @param item
	 * @param nb
	 */
	public Sale(String player, String item, int quantity) {
		this.player = player;
		this.item = item;
		this.quantity = quantity;
	}

	public Sale(String item, int quantity) {
		this.player = null;
		this.item = item;
		this.quantity = quantity;
	}

	/**
	 * @return the nb
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param nb the nb to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the player
	 */
	public String getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(String player) {
		this.player = player;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item the item to set
	 */
	public void setItem(String item) {
		this.item = item;
	}
}
