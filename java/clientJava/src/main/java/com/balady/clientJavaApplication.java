package com.balady;

import java.util.Timer;
import java.util.TimerTask;

import com.balady.data.GameMap;
import com.balady.rest.ClientRest;

public class clientJavaApplication {
	public static void main(String[] args) {
		GameMap game = new GameMap("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			int current_hour = 0;
			
			@Override
			public void run() {
				game.refreshMap();
				if (game.getHour() == 0) {
					game.addConsumers();
				}
				if (game.getHour() != current_hour) {
					game.play();
					game.moveConsumers();
					game.sendSales();
					current_hour = game.getHour();
				}
			}
		}, 1000, 1000);
	}
}
