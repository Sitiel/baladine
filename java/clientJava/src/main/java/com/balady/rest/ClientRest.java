package com.balady.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.balady.data.Sale;
import com.balady.rest.receiptJson.ReceiptObject;
import com.balady.rest.receiptJson.TimeReceipt;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientRest {

	private String url;

	/**
	 * @param url
	 */
	public ClientRest(String url) {
		this.url = url;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * GET \map Get data of current day 
	 * @return JSONObject which contains all data
	 */
	public ReceiptObject getData() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			URL adress = new URL(url + "/map");
			HttpURLConnection conn = (HttpURLConnection) adress.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String ret = "";
			String output;
			while ((output = br.readLine()) != null) {
				ret += output;
			}
			conn.disconnect();
			ReceiptObject res = mapper.readValue(ret, ReceiptObject.class);
			return res;
		} catch (MalformedURLException e) {
			System.out.println("ERROR while trying to create url: " + url + "/map");
			return null;
		} catch (IOException e) {
			System.out.println("ERROR while Rest Connexion");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * GET \map Get data of current day 
	 * @return JSONObject which contains all data
	 */
	public TimeReceipt getMeteorologyAndTime() {
		try {
			ObjectMapper mapper = new ObjectMapper();
			URL adress = new URL(url + "/meteorology");
			HttpURLConnection conn = (HttpURLConnection) adress.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String ret = "";
			String output;
			while ((output = br.readLine()) != null) {
				ret += output;
			}
			conn.disconnect();
			System.out.println(ret);
			TimeReceipt res = mapper.readValue(ret, TimeReceipt.class);
			return res;
		} catch (MalformedURLException e) {
			System.out.println("ERROR while trying to create url: " + url + "/meteorology");
			return null;
		} catch (IOException e) {
			System.out.println("ERROR while Rest Connexion");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * POST \sales
	 * @param The list of sale 
	 */
	public void sendSales(List<Sale> sales) {
		try {
			URL adress = new URL(url + "/sales");
			HttpURLConnection conn = (HttpURLConnection) adress.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			
			String input = convertToJson(sales);
			
			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();
			
			if (conn.getResponseCode() != 200) {
				System.err.println(conn.getResponseCode());
			}
			
			conn.disconnect();
			
		} catch (MalformedURLException e) {
			System.out.println("ERROR while trying to create url: " + url + "/sales");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR while Rest Connexion");
		}
	}

	
	private class ListSales {
		private List<Sale> sales;

		/**
		 * @param sales
		 */
		public ListSales(List<Sale> sales) {
			this.sales = sales;
		}

		/**
		 * @return the sales
		 */
		public List<Sale> getSales() {
			return sales;
		}

		/**
		 * @param sales the sales to set
		 */
		public void setSales(List<Sale> sales) {
			this.sales = sales;
		}
	}
	
	String convertToJson(List<Sale> sales) {
		ListSales tmp = new ListSales(sales);
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(tmp);
		} catch (IOException e) {
			System.out.println("Error while trying to convert List<Sale> in json");
			return null;
		}
	}
}
