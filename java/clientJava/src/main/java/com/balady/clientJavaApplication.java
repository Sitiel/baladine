package com.balady;

import java.util.Timer;
import java.util.TimerTask;

import com.balady.data.GameMap;
import com.balady.rest.ClientRest;

public class clientJavaApplication {
	/*public static void main(String[] args) {
		GameMap game = new GameMap("https://virtserver.swaggerhub.com/ValerianKang/Balady_API/1.0.0");
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				//game.refreshMap();
				if (game.getHour() == 0) {
					game.play();
					game.sendSales();
				}
			}
		}, 10000, 10000);
	}*/
	public static void main(String[] args) {
		ClientRest client = new ClientRest("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0");
		//System.out.println(client.getData());
		/*List<Sale> sales = new ArrayList<>();
		sales.add(new Sale("DarkSasuke69","limonade",5));
		client.sendSales(sales);*/
		client.getMeteorologyAndTime();
	}
}
