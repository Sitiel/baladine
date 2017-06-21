package com.balady.data;

import java.util.ArrayList;
import java.util.List;

import com.balady.rest.ClientRest;

public class testClientRest {
	public static void main(String[] args) {
		ClientRest client = new ClientRest("https://virtserver.swaggerhub.com/ValerianKang/Balady_API/1.0.0");
		System.out.println(client.getData());
		List<Sale> sales = new ArrayList<>();
		sales.add(new Sale("toto","limonade"));
		sales.add(new Sale("michel","eau"));
		client.sendSale(sales);
	}
}
