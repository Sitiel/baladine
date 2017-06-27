package com.balady.population;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.balady.data.Coordinates;
import com.balady.data.Drink;
import com.balady.data.Player;
import com.balady.data.Sale;
import com.balady.data.Zone;
import com.balady.data.utils.Intersection;

public class Consumer {

	private Coordinates coordinates;
	private Zone target;
	private float movement;
	private List<Zone> pubSeen;

	public Consumer(float xMin, float xMax, float yMin, float yMax) {
		float x = (xMax - xMin) * (float) Math.random();
		float y = (yMax - yMin) * (float) Math.random();
		coordinates = new Coordinates(x, y);
		movement = 15;
		target = null;
		pubSeen = new ArrayList<>();
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

	public Sale chooseDrink(int hour, String meteo, List<Drink> drinks) {

		Drink chosenDrink = null;
		Sale sale = null;

		for (Drink d : drinks) {
			// if hot they want cold drinks
			if (("SOLEIL".equals(meteo) || "CANICULE".equals(meteo)) && d.isCold()) {
				// if day they don't want alcholize drink
				if ((hour > 6 && hour < 19) && d.hasAlcholize()) {
				} else {
					if (chosenDrink == null || chosenDrink.getCost() > d.getCost())
						chosenDrink = d;
				}
			}
			// else they want hot drink
			else if (("PLUIE".equals(meteo) || "NUAGE".equals(meteo) || "ORAGE".equals(meteo)) && !d.isCold()) {
				// if day they don't want alcholize drink
				if ((hour > 7 && hour < 19) && d.hasAlcholize()) {
				} else if (chosenDrink == null || chosenDrink.getCost() > d.getCost()) {
					chosenDrink = d;
				}
			}
		}
		if (chosenDrink != null) {
			sale = new Sale(chosenDrink.getName(), nbDrinksOrder());
		}
		return sale;
	}

	public Drink chooseDrinkSimulate(int hour, String meteo, List<Drink> drinks) {

		Drink chosenDrink = null;

		for (Drink d : drinks) {
			// if hot they want cold drinks
			if (("SOLEIL".equals(meteo) || "CANICULE".equals(meteo)) && d.isCold()) {
				// if day they don't want alcholize drink
				if ((hour > 6 && hour < 19) && d.hasAlcholize()) {
				} else {
					if (chosenDrink == null || chosenDrink.getCost() > d.getCost())
						chosenDrink = d;
				}
			}
			// else they want hot drink
			else if (("PLUIE".equals(meteo) || "NUAGE".equals(meteo) || "ORAGE".equals(meteo)) && !d.isCold()) {
				// if day they don't want alcholize drink
				if ((hour > 7 && hour < 19) && d.hasAlcholize()) {
				} else if (chosenDrink == null || chosenDrink.getCost() > d.getCost()) {
					chosenDrink = d;
				}
			}
		}
		return chosenDrink;
	}

	private int nbDrinksOrder() {
		int nb = 1;
		while (ThreadLocalRandom.current().nextInt(1, nb + 2) == 1 && nb < 10) {
			nb++;
		}
		return nb;
	}

	public void findStand(List<Player> players) {
		double distanceMin = 0;
		for (Player p : players) {
			double distance = calculDistance(this.getCoordinates(), p.getStand().getCoordinates());
			if (target == null || distance < distanceMin) {
				target = p.getStand();
				distanceMin = distance;
			}
		}
	}

	private static double calculDistance(Coordinates p1, Coordinates p2) {
		double tmp = Math.sqrt(
				(p2.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));

		return tmp;
	}

	public void move(List<Player> players, int hour, String meteo) {
		double distance = calculDistance(this.getCoordinates(), target.getCoordinates());
		Intersection intersec = null;
		if (distance <= movement) {
			intersec = detectCollision(players, target.getCoordinates());
			if (intersec != null && conflictWinP2(hour, meteo, target.getOwner(), intersec.getPlayer())) {
				movement -= calculDistance(coordinates, intersec.getIntersecion());
				target = intersec.getPlayer().getStand();
				coordinates = intersec.getIntersecion();
				move(players, hour, meteo);
			} else {
				coordinates = target.getCoordinates();
				movement = 15;
			}
		} else {
			double nbToursRequis = distance / movement;
			float xTmp = (float) (coordinates.getX()
					+ (target.getCoordinates().getX() - coordinates.getX()) / nbToursRequis);
			float yTmp = (float) (coordinates.getY()
					+ (target.getCoordinates().getY() - coordinates.getY()) / nbToursRequis);
			intersec = detectCollision(players, new Coordinates(xTmp, yTmp));
			if (intersec != null && target.getOwner() != intersec.getPlayer() && conflictWinP2(hour, meteo, target.getOwner(), intersec.getPlayer())) {
				movement -= calculDistance(coordinates, intersec.getIntersecion());
				target = intersec.getPlayer().getStand();
				coordinates = intersec.getIntersecion();
				move(players, hour, meteo);
			} else {
				coordinates.setX(xTmp);
				coordinates.setY(yTmp);
				movement = 15;
			}
		}
	}

	public Intersection detectCollision(List<Player> players, Coordinates target) {
		Intersection res = null;
		Zone tmp = null;
		float distanceMin = -1;
		for (Player p : players) {
			for (Zone z : p.getPubs()) {
				if (!pubSeen.contains(z)) {
					if (z.isInInfluence(this)) {
						if (distanceMin == 0) {
							double dist1 = calculDistance(this.getCoordinates(), p.getStand().getCoordinates());
							double dist2 = calculDistance(this.getCoordinates(),
									res.getPlayer().getStand().getCoordinates());
							if (dist1 < dist2) {
								tmp = z;
								res.setIntersecion(this.getCoordinates());
								res.setPlayer(p);
							}
						} else {
							tmp = z;
							res = new Intersection(this.getCoordinates(), p);
						}
					} else if (distanceMin != 0) {
						List<Coordinates> coords = getCircleLineIntersectionPoint(target, z);
						if (!coords.isEmpty()) {
							for (Coordinates c : coords) {
								float tmpDistance = (float) calculDistance(this.getCoordinates(), c);
								if (tmpDistance < distanceMin) {
									tmp = z;
									res = new Intersection(c, p);
									distanceMin = tmpDistance;
								}
							}
						}
					}
				}
			}
		}
		if (tmp != null) {
			pubSeen.add(tmp);
		}
		return res;
	}

	public List<Coordinates> getCircleLineIntersectionPoint(Coordinates destination, Zone z) {
		float baX = destination.getX() - this.getCoordinates().getX();
		float baY = destination.getY() - this.getCoordinates().getY();
		float caX = z.getCoordinates().getX() - this.getCoordinates().getX();
		float caY = z.getCoordinates().getY() - this.getCoordinates().getY();

		float a = baX * baX + baY * baY;
		float bBy2 = baX * caX + baY * caY;
		float c = caX * caX + caY * caY - z.getInfluence() * z.getInfluence();

		float pBy2 = bBy2 / a;
		float q = c / a;

		float disc = (float) (pBy2 * pBy2 - q);
		if (disc < 0) {
			return Collections.emptyList();
		}
		// if disc == 0 ... dealt with later
		float tmpSqrt = (float) Math.sqrt(disc);
		float abScalingFactor1 = -pBy2 + tmpSqrt;
		float abScalingFactor2 = -pBy2 - tmpSqrt;

		Coordinates p1 = new Coordinates(this.getCoordinates().getX() - baX * abScalingFactor1,
				this.getCoordinates().getY() - baY * abScalingFactor1);
		if (disc == 0) { // abScalingFactor1 == abScalingFactor2
			return Collections.singletonList(p1);
		}
		Coordinates p2 = new Coordinates(this.getCoordinates().getX() - baX * abScalingFactor2,
				this.getCoordinates().getY() - baY * abScalingFactor2);
		return Arrays.asList(p1, p2);
	}

	private boolean conflictWinP2(int hour, String meteo, Player p1, Player p2) {
		Drink d1 = this.chooseDrinkSimulate(hour, meteo, p1.getDrinks());
		Drink d2 = this.chooseDrinkSimulate(hour, meteo, p2.getDrinks());
		if (d1 == null || (d2.getCost() < d1.getCost() && Math.random() * (100) + 0 < getLuckChange(meteo)))
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
