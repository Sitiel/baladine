package com.balady.population;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.balady.data.Coordinates;
import com.balady.data.Drink;
import com.balady.data.Sale;

public class Consumer {
	
	private Coordinates coordinates;

	/**
	 * @param coordinates
	 */
	public Consumer(float xMin,float xMax,float yMin,float yMax) {
		float x = (xMax-xMin)*(float) Math.random();
		float y = (yMax-yMin)*(float) Math.random();
		coordinates = new Coordinates(x,y);
	}

	/**
	 * @return the coordinates
	 */
	public Coordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * @param coordinates the coordinates to set
	 */
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Sale chooseDrink (int hour, String meteo, List<Drink> drinks) {
		
		Drink chosenDrink = null;
		Sale sale = null;
		
		for (Drink d: drinks) {	
			// if hot they want cold drinks
			if (("SOLEIL".equals(meteo)  || "CANICULE".equals(meteo)) && d.isCold()) {
				// if day they don't want alcholize drink
				if ((hour > 6 && hour < 19) && d.hasAlcholize()) {}
				else  {
					if (chosenDrink == null || chosenDrink.getCost() > d.getCost())
						chosenDrink = d;
				}
			}
			// else they want hot drink
			else if (("PLUIE".equals(meteo) || "NUAGE".equals(meteo) || "ORAGE".equals(meteo)) && !d.isCold()) {
				// if day they don't want alcholize drink
				if ((hour > 7 && hour < 19) && d.hasAlcholize()) {}
				else if (chosenDrink == null || chosenDrink.getCost() > d.getCost()) {
					chosenDrink = d;
				}
			}
		}
		if (chosenDrink != null) {
			sale = new Sale(chosenDrink.getName(),nbDrinksOrder());
		}
		return sale;
	}
	
	private int nbDrinksOrder () {
		int nb = 1;
		while (ThreadLocalRandom.current().nextInt(1, nb+2) == 1 && nb < 10) {
			nb++;
		}
		return nb;
	}
}
