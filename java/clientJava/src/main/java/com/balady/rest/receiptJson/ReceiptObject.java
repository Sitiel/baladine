package com.balady.rest.receiptJson;

import java.util.List;
import java.util.Map;

public class ReceiptObject {
	
		private Region region;
		private List<String> ranking;
		private Map<String, List<MapItem>> itemsByPlayer;
		private Map<String, PlayerInfo> playerInfo;
		private Map<String, List<DrinkInfo>> drinksByPlayer;
		
		/**
		 * 
		 */
		public ReceiptObject() {
			
		}

		/**
		 * @param region
		 * @param ranking
		 * @param itemsByPlayer
		 * @param playerInfo
		 * @param drinksByPlayer
		 */
		public ReceiptObject(Region region, List<String> ranking, Map<String, List<MapItem>> itemsByPlayer,
				Map<String, PlayerInfo> playerInfo, Map<String, List<DrinkInfo>> drinksByPlayer) {
			this.region = region;
			this.ranking = ranking;
			this.itemsByPlayer = itemsByPlayer;
			this.playerInfo = playerInfo;
			this.drinksByPlayer = drinksByPlayer;
		}

		public Region getRegion() {
			return region;
		}

		public void setRegion(Region region) {
			this.region = region;
		}

		/**
		 * @return the ranking
		 */
		public List<String> getRanking() {
			return ranking;
		}

		/**
		 * @param ranking the ranking to set
		 */
		public void setRanking(List<String> ranking) {
			this.ranking = ranking;
		}

		/**
		 * @return the itemsByPlayer
		 */
		public Map<String, List<MapItem>> getItemsByPlayer() {
			return itemsByPlayer;
		}

		/**
		 * @param itemsByPlayer the itemsByPlayer to set
		 */
		public void setItemsByPlayer(Map<String, List<MapItem>> itemsByPlayer) {
			this.itemsByPlayer = itemsByPlayer;
		}

		/**
		 * @return the playerInfo
		 */
		public Map<String, PlayerInfo> getPlayerInfo() {
			return playerInfo;
		}

		/**
		 * @param playerInfo the playerInfo to set
		 */
		public void setPlayerInfo(Map<String, PlayerInfo> playerInfo) {
			this.playerInfo = playerInfo;
		}

		/**
		 * @return the drinkInfo
		 */
		public Map<String, List<DrinkInfo>> getDrinksByPlayer() {
			return drinksByPlayer;
		}

		/**
		 * @param drinkInfo the drinkInfo to set
		 */
		public void setDrinksByPlayer(Map<String, List<DrinkInfo>> drinksByPlayer) {
			this.drinksByPlayer = drinksByPlayer;
		}
		
		
}
