package com.balady.rest.receiptJson;

public class MapItem {
	private String kind;
	private String owner;
	private CoordinatesReceipt location;
	private float influencce;
	
	/**
	 * 
	 */
	public MapItem() {
	}

	/**
	 * @param kind
	 * @param owner
	 * @param location
	 * @param influencce
	 */
	public MapItem(String kind, String owner, CoordinatesReceipt location, float influencce) {
		this.kind = kind;
		this.owner = owner;
		this.location = location;
		this.influencce = influencce;
	}

	/**
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}

	/**
	 * @return the location
	 */
	public CoordinatesReceipt getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(CoordinatesReceipt location) {
		this.location = location;
	}

	/**
	 * @return the influencce
	 */
	public float getInfluencce() {
		return influencce;
	}

	/**
	 * @param influencce the influencce to set
	 */
	public void setInfluencce(float influencce) {
		this.influencce = influencce;
	}
}
