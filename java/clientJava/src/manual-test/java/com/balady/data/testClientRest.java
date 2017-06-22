package com.balady.data;

import com.balady.rest.ClientRest;

public class testClientRest {
	public static void main(String[] args) {
		ClientRest client = new ClientRest("http://balady.herokuapp.com/ValerianKang/Balady_API/1.0.0");
		//System.out.println(client.getData());
		/*List<Sale> sales = new ArrayList<>();
		sales.add(new Sale("DarkSasuke69","limonade",5));
		client.sendSales(sales);*/
		client.getMeteorologyAndTime();
	}
}
