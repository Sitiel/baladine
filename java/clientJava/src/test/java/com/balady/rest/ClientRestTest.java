package com.balady.rest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.balady.data.Sale;

public class ClientRestTest {
	
	private ClientRest clientRest;

	@Test
	public void testConvertToJson() {
		clientRest = new ClientRest("test");
		List<Sale> sales = new ArrayList<>();
		sales.add(new Sale("toto","limonade"));
		sales.add(new Sale("michel","eau"));
		System.out.println(clientRest.convertToJson(sales));	
	}

}
