package com.balady.data.utils;

import com.balady.data.Coordinates;
import com.balady.data.Player;

public class Intersection {
	private Coordinates intersecion;
	private Player player;
	
	/**
	 * @param intersecion
	 * @param player
	 */
	public Intersection(Coordinates intersecion, Player player) {
		this.intersecion = intersecion;
		this.player = player;
	}

	/**
	 * @return the intersecion
	 */
	public Coordinates getIntersecion() {
		return intersecion;
	}

	/**
	 * @param intersecion the intersecion to set
	 */
	public void setIntersecion(Coordinates intersecion) {
		this.intersecion = intersecion;
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
}
