package com.balady.population;

import static org.junit.Assert.*;

import org.junit.Test;

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

}
