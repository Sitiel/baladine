package com.balady.data;

public class Sale {
	private String player;
	private String item;
	private int nb;

	/**
	 * @param player
	 * @param item
	 * @param nb
	 */
	public Sale(String player, String item, int nb) {
		this.player = player;
		this.item = item;
		this.nb = nb;
	}

	/**
	 * @return the nb
	 */
	public int getNb() {
		return nb;
	}

	/**
	 * @param nb the nb to set
	 */
	public void setNb(int nb) {
		this.nb = nb;
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
