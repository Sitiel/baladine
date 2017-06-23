package com.balady.population;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.balady.data.Drink;
import com.balady.data.Sale;

public class ConsumerTest {

	@Test
	public void testConstructor() {
		for (int i = 0; i < 30000; i++) {
			Consumer c = new Consumer(0,500,0,400);
			float x = c.getCoordinates().getX();
			float y = c.getCoordinates().getY();
			assertTrue(x >= 0 && x <= 500);
			assertTrue(y >= 0 && y <= 400);
		}
	}
	
	
	@Test
	public void testChooseDrink () {
		Consumer c = new Consumer(0,500,0,400);
		List<Drink> drinks = new ArrayList<>();
		drinks.add(new Drink("limonade",5,false,true));
		drinks.add(new Drink("eau",5,false,false));
		drinks.add(new Drink("biere",2,true,true));
		drinks.add(new Drink("vin chaud",2,true,false));
		Sale tmp = c.chooseDrink(10, "Soleil", drinks);
		assertEquals("limonade",tmp.getItem());
		tmp = c.chooseDrink(14, "Nuage", drinks);
		assertEquals("eau",tmp.getItem());
		tmp = c.chooseDrink(3, "Soleil", drinks);
		assertEquals("biere",tmp.getItem());
		tmp = c.chooseDrink(23, "Pluie", drinks);
		assertEquals("vin chaud",tmp.getItem());
	}
}
