package com.balady.data;

public class Sale {
	private String player;
	private String item;
	
	/**
	 * @param player
	 * @param item
	 */
	public Sale(String player, String item) {
		this.player = player;
		this.item = item;
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
