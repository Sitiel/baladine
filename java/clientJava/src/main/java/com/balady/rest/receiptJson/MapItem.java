package com.balady.rest.receiptJson;

public class MapItem {
	private String kind;
	private String owner;
	private CoordinatesReceipt location;
	private float influence;
	
	/**
	 * 
	 */
	public MapItem() {
	}

	/**
	 * @param kind
	 * @param owner
	 * @param location
	 * @param influence
	 */
	public MapItem(String kind, String owner, CoordinatesReceipt location, float influence) {
		this.kind = kind;
		this.owner = owner;
		this.location = location;
		this.influence = influence;
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
	 * @return the influence
	 */
	public float getInfluence() {
		return influence;
	}

	/**
	 * @param influence the influence to set
	 */
	public void setInfluence(float influence) {
		this.influence = influence;
	}
}
