package com.balady.data;

import static org.junit.Assert.*;

import org.junit.Test;

import com.balady.data.utils.TypeZone;
import com.balady.population.Consumer;

public class ZoneTest {

	@Test
	public void testIsInInfluence() {
		Zone z = new Zone(10,TypeZone.PUB,new Coordinates(30, 30));
		Consumer c = new Consumer(0,100,0,100);
		c.setCoordinates(new Coordinates(10, 15));
		assertFalse(z.isInInfluence(c));
		c.setCoordinates(new Coordinates(29, 28));
		assertTrue(z.isInInfluence(c));
		c.setCoordinates(new Coordinates(20, 20));
		assertFalse(z.isInInfluence(c));
	}

}
