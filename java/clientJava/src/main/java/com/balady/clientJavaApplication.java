package com.balady;

import java.util.Timer;
import java.util.TimerTask;

import com.balady.data.GameMap;

public class clientJavaApplication {
	public static void main(String[] args) {
		GameMap game = new GameMap("https://virtserver.swaggerhub.com/ValerianKang/Balady_API/1.0.0");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				//game.refreshMap();
				if (game.getHour() == 0) {
				}
			}
		}, 10000, 10000);
	}
}
