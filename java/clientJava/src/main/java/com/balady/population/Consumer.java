package com.balady.population;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.balady.data.Coordinates;
import com.balady.data.Drink;
import com.balady.data.Player;
import com.balady.data.Sale;
import com.balady.data.Zone;

public class Consumer {

	private Coordinates coordinates;
	private Zone target;
	private float movement;
	private float budget;
	private List<Zone> pubSeen;

	public Consumer(float xMin, float xMax, float yMin, float yMax) {
		float x = (xMax - xMin) * (float) Math.random();
		float y = (yMax - yMin) * (float) Math.random();
		coordinates = new Coordinates(x, y);
		movement = 15;
		target = null;
		pubSeen = new ArrayList<>();
		budget = (float) (Math.random() * 15 + 1);
	}

	/**
	 * @return the coordinates
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates
	 *            the coordinates to set
	 */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * @return the target
	 */
	public Zone getTarget() {
		return target;
	}

	/**
	 * @param target
	 *            the target to set
	 */
	public void setTarget(Zone target) {
		this.target = target;
	}

	/**
	 * Choose the Drink to Customer
	 * @param hour
	 * @param meteo
	 * @param drinks
	 * @return Sale which contains the drink and the number selected by the customer
	 */
	public Sale chooseDrink(int hour, String meteo, List<Drink> drinks) {

		List<Drink> possibleDrinks = new ArrayList<>();

		for (Drink d : drinks) {
			if (d.getCost() <= this.budget) {

				if (!d.hasAlcholize())
					possibleDrinks.add(d);

				// if hot they want cold drinks
				if (("SOLEIL".equals(meteo) || "CANICULE".equals(meteo)) && d.isCold()) {
					possibleDrinks.add(d);
				}
				// else he want hot drink
				else if (("PLUIE".equals(meteo) || "NUAGE".equals(meteo) || "ORAGE".equals(meteo)) && !d.isCold()) {
					possibleDrinks.add(d);
				}
				// if day he don't want alcholize drink
				if ((hour > 6 && hour < 19) && !d.hasAlcholize()) {
					possibleDrinks.add(d);
				}
				// if night they want alcholize drink
				else if ((hour < 7 || hour > 19) && d.hasAlcholize()) {
					possibleDrinks.add(d);
					possibleDrinks.add(d);
					possibleDrinks.add(d);
				}
			}
		}
		if (possibleDrinks.isEmpty()) {
			return null;
		}
		else {
			Drink selected = possibleDrinks.get((int)Math.round(Math.random()*(possibleDrinks.size()-1)));
			return new Sale(selected.getName(),nbDrinksOrder(selected));
		}
	}

	/**
	 * Simulate the choice of drink to Customer
	 * @param hour
	 * @param meteo
	 * @param drinks
	 * @return drink choose
	 */
	public Drink chooseDrinkSimulate(int hour, String meteo, List<Drink> drinks) {

		List<Drink> possibleDrinks = new ArrayList<>();

		for (Drink d : drinks) {
			if (d.getCost() <= this.budget) {

				if (!d.hasAlcholize())
					possibleDrinks.add(d);

				// if hot they want cold drinks
				if (("SOLEIL".equals(meteo) || "CANICULE".equals(meteo)) && d.isCold()) {
					possibleDrinks.add(d);
				}
				// else he want hot drink
				else if (("PLUIE".equals(meteo) || "NUAGE".equals(meteo) || "ORAGE".equals(meteo)) && !d.isCold()) {
					possibleDrinks.add(d);
				}
				// if day he don't want alcholize drink
				if ((hour > 6 && hour < 19) && !d.hasAlcholize()) {
					possibleDrinks.add(d);
				}
				// if night they want alcholize drink
				else if ((hour < 7 || hour > 19) && d.hasAlcholize()) {
					possibleDrinks.add(d);
					possibleDrinks.add(d);
					possibleDrinks.add(d);
				}
			}
		}
		if (possibleDrinks.isEmpty()) {
			return null;
		}
		else {
			return possibleDrinks.get((int)Math.round(Math.random()*possibleDrinks.size()));
		}
	}

	/**
	 * Make the choose of number of drinks buy
	 * @param d
	 * @return number of drinks order
	 */
	private int nbDrinksOrder(Drink d) {
		int nb = 1;
		while (ThreadLocalRandom.current().nextInt(1, nb + 2) == 1 && nb < 10 && budget >= (nb + 1) * d.getCost()) {
			nb++;
		}
		return nb;
	}

	/**
	 * Choose the stand 
	 * @param players
	 */
	public void findStand(List<Player> players) {
		target = players.get((int) Math.round( (Math.random() * (players.size()-1)))).getStand();
	}

	private static double calculDistance(Coordinates p1, Coordinates p2) {
		double tmp = Math.sqrt(
				(p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));

		return tmp;
	}

	public void move(List<Player> players, int hour, String meteo) {
		double distance = calculDistance(this.getCoordinates(), target.getCoordinates());
		if (distance <= movement) {
			coordinates = target.getCoordinates();
			movement = 15;
		} else {
			double nbToursRequis = distance / movement;
			float xTmp = (float) (coordinates.getX()
					+ (target.getCoordinates().getX() - coordinates.getX()) / nbToursRequis);
			float yTmp = (float) (coordinates.getY()
					+ (target.getCoordinates().getY() - coordinates.getY()) / nbToursRequis);
			coordinates.setX(xTmp);
			coordinates.setY(yTmp);
		}
		List<Player> playersInConflict = detectConflict(players);
		if (!playersInConflict.isEmpty()) {
			for (Player p : playersInConflict) {
				if (conflictWinP2(hour, meteo, target.getOwner(), p)) {
					target = p.getStand();
				}
			}
		}
	}

	private List<Player> detectConflict(List<Player> players) {
		List<Player> res = new ArrayList<>();
		for (Player p : players) {
			for (Zone z : p.getPubs()) {
				if (!res.contains(p) && z.isInInfluence(this) && !pubSeen.contains(z)) {
					res.add(p);
					pubSeen.add(z);
				}
			}
		}
		return res;
	}

	private boolean conflictWinP2(int hour, String meteo, Player p1, Player p2) {
		Drink d1 = this.chooseDrinkSimulate(hour, meteo, p1.getDrinks());
		Drink d2 = this.chooseDrinkSimulate(hour, meteo, p2.getDrinks());

		if (d2 != null && (d1 == null && d2 != null
				|| (d2.getCost() < d1.getCost() && Math.random() * (100) + 0 < getLuckChange(meteo))))
			return true;
		else
			return false;
	}

	private int getLuckChange(String meteo) {
		int res;
		switch (meteo) {
		case ("SOLEIL"):
			res = 50;
			break;
		case ("ORAGE"):
			res = 0;
			break;
		case ("NUAGE"):
			res = 40;
			break;
		case ("CANICULE"):
			res = 20;
			break;
		default:
			res = 10;
			break;
		}
		return res;
	}
}
