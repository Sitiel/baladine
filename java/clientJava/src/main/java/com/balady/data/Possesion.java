package com.balady.data;

public class Possesion {
	private Drink drink;
	private Player player;
	private int nbPrepared;
	private int nbSelled;

	/**
	 * @param drink
	 * @param player
	 * @param nbPrepared
	 * @param nbSelled
	 */
	public Possesion(Drink drink, Player player, int nbPrepared, int nbSelled) {
		this.drink = drink;
		this.player = player;
		this.nbPrepared = nbPrepared;
		this.nbSelled = nbSelled;
	}

	/**
	 * @return the drink
	 */
	public Drink getDrink() {
		return drink;
	}

	/**
	 * @param drink the drink to set
	 */
	public void setDrink(Drink drink) {
		this.drink = drink;
	}

	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @return the nbPrepared
	 */
	public int getNbPrepared() {
		return nbPrepared;
	}

	/**
	 * @param nbPrepared the nbPrepared to set
	 */
	public void setNbPrepared(int nbPrepared) {
		this.nbPrepared = nbPrepared;
	}

	/**
	 * @return the nbSelled
	 */
	public int getNbSelled() {
		return nbSelled;
	}

	/**
	 * @param nbSelled the nbSelled to set
	 */
	public void setNbSelled(int nbSelled) {
		this.nbSelled = nbSelled;
	}
	
	
}
