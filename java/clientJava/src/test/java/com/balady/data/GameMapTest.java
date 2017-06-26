package com.balady.data;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.balady.population.Consumer;

public class GameMapTest {

	@Test
	public void testPlay() {
		GameMap game = new GameMap();
		game.setStart(new Coordinates(0, 0));
		game.setEnd(new Coordinates(150, 150));
		game.setMeteo("Soleil");
		
		Zone z1 = new Zone(10,  new Coordinates(50, 50));
		Zone z2 = new Zone(10,  new Coordinates(100, 100));
		
		List<Drink> drinks = new ArrayList<>();
		drinks.add(new Drink("limonade",5,false,true));
		drinks.add(new Drink("eau",5,false,false));
		drinks.add(new Drink("biere",2,true,true));
		drinks.add(new Drink("vin chaud",2,true,false));
		
		List<Player> players = new ArrayList<>();
		Player j1 = new Player("jean", 10, 0, 0, null,z1, drinks);
		players.add(j1);
		Player j2 = new Player("michel", 10, 0, 0, null,z2, drinks);
		players.add(j2);
		
		List<Consumer> consumers = new ArrayList<>();
		Consumer c1 = new Consumer(0,150,0,150);
		c1.setCoordinates(new Coordinates(52,54));
		consumers.add(c1);
		Consumer c2 = new Consumer(0,150,0,150);
		c2.setCoordinates(new Coordinates(100,101));
		consumers.add(c2);
		Consumer c3 = new Consumer(0,150,0,150);
		c3.setCoordinates(new Coordinates(10,50));
		consumers.add(c3);
		c3.setCoordinates(new Coordinates(10,100));
		consumers.add(c3);
		
		game.setPlayers(players);
		game.setConsumers(consumers);
		game.setSales(new HashMap<>());
	
		game.play();
	}

}
