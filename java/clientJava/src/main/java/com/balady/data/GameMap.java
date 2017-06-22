package com.balady.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.balady.data.utils.TypeZone;
import com.balady.rest.ClientRest;
import com.balady.rest.receiptJson.DrinkInfo;
import com.balady.rest.receiptJson.Forecast;
import com.balady.rest.receiptJson.MapItem;
import com.balady.rest.receiptJson.PlayerInfo;
import com.balady.rest.receiptJson.ReceiptObject;
import com.balady.rest.receiptJson.TimeReceipt;

public class GameMap {
	
	private String meteo;
	private long hour;
	private List<Player> players;
	private Coordinates start;
	private Coordinates end;
	private ClientRest rest;
	private Map<String,Sale> sales;
	
	public GameMap(String url) {
		rest = new ClientRest(url);
		refreshMap();
	}
	
	

	/**
	 * @return the sales
	 */
	public Map<String, Sale> getSales() {
		return sales;
	}



	/**
	 * @param sales the sales to set
	 */
	public void setSales(Map<String, Sale> sales) {
		this.sales = sales;
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
	
	/**
	 * @return the hour
	 */
	public long getHour() {
		return hour;
	}

	/**
	 * @param hour the hour to set
	 */
	public void setHour(long hour) {
		this.hour = hour;
	}

	/**
	 * @return the rest
	 */
	public ClientRest getRest() {
		return rest;
	}

	/**
	 * @param rest the rest to set
	 */
	public void setRest(ClientRest rest) {
		this.rest = rest;
	}

	public void refreshMap() {
		players = new ArrayList<>();
		
		// Fill Hour and Meteo
		TimeReceipt time = rest.getMeteorologyAndTime();
		for (Forecast f: time.getWeather()) {
			if (f.getDfn() == 0)
				meteo = f.getWeather();
		}
		hour = time.getTimestamp()/24;
		ReceiptObject receipt = rest.getData();
		
		// Fill Position of Map
		start = new Coordinates(receipt.getRegion().getCenter().getLongitude(), receipt.getRegion().getCenter().getLatitude());
		end = new Coordinates(receipt.getRegion().getspan().getLongitudeSpan(), receipt.getRegion().getspan().getLatitudeSpan());
		
		// Fill PlayersInfo
		for (String name: receipt.getRanking()) {
			if (receipt.getPlayerInfo().containsKey(name)) {
				PlayerInfo player = receipt.getPlayerInfo().get(name);
				
				// Fill Zones info to Player
				List<Zone> zones = new ArrayList<>();
				if (receipt.getItemsByPlayer().containsKey(name)) {
					for (MapItem item: receipt.getItemsByPlayer().get(name)) {
						TypeZone typeZone;
						if ("ad".equals(item.getKind()))
							typeZone = TypeZone.PUB;
						else
							typeZone = TypeZone.STAND;
						zones.add(new Zone(item.getInfluencce(), typeZone,new Coordinates(item.getLocation().getLongitude(),item.getLocation().getLatitude())));
					}
				}
				
				// Fill Drinks info to Player
				List<Drink> drinks = new ArrayList<>();
				if (receipt.getDrinksByPlayer().containsKey(name)) {
					for (DrinkInfo drink : receipt.getDrinksByPlayer().get(name)) {
						drinks.add(new Drink(drink.getName(),drink.getPrice(), drink.HasAlcohol(),drink.isCold()));
					}
				}
				players.add(new Player(name, player.getCash(), player.getProfit(), player.getSales(), zones,drinks));
			}
		}
	}
}
