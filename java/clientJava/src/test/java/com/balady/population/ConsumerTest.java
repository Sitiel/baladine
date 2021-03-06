package com.balady.population;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.balady.data.Coordinates;
import com.balady.data.Drink;
import com.balady.data.Player;
import com.balady.data.Sale;
import com.balady.data.Zone;

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
	
	/*@Test
	public void testMove () {
		Consumer c = new Consumer(100,100,100,100);
		c.setCoordinates(new Coordinates(100, 100));
		Zone z = new Zone(10, new Coordinates(100, 300));
		c.setTarget(z);
		//c.move();
		assertEquals(100f, c.getCoordinates().getX(),0);
		assertEquals(150f, c.getCoordinates().getY(),0);
		c.setCoordinates(new Coordinates(50, 50));
		z.setCoordinates(new Coordinates(0, 0));
		//c.move();
		assertEquals(14.644661f, c.getCoordinates().getX(),0);
		assertEquals(14.644661f, c.getCoordinates().getY(),0);
	}*/
	
	@Test
	public void detectCollision () {
		List<Player> players = new ArrayList<>();
		
		List<Drink> d1 = new ArrayList<>();
	}
}
