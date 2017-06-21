package com.balady.data;

import java.util.List;

public class GameMap {
	
	private String meteo;
	private List<Player> players;
	private Coordinates start;
	private Coordinates end;
	
	/**
	 * @param meteo
	 * @param players
	 * @param start
	 * @param end
	 */
	public GameMap(String meteo, List<Player> players, Coordinates start, Coordinates end) {
		this.meteo = meteo;
		this.players = players;
		this.start = start;
		this.end = end;
	}
	
	public GameMap(String json) {
		// TODO
	}

	/**
	 * @return the meteo
	 */
	public String getMeteo() {
		return meteo;
	}

	/**
	 * @param meteo the meteo to set
	 */
	public void setMeteo(String meteo) {
		this.meteo = meteo;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * @return the start
	 */
	public Coordinates getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Coordinates start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Coordinates getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Coordinates end) {
		this.end = end;
	}
	
	
}
