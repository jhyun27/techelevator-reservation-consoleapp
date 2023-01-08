package com.techelevator.excelsior.model;

import java.math.BigDecimal;

public class Space {
	
	private int spaceId;
	private int venueId;
	private String name;
	private boolean isAccessible;
	private int openFrom;
	private int openTo;
	private BigDecimal rate;
	private int maxOccupancy;
	

	public int getSpaceId() {
		return spaceId;
	}
	public void setSpaceId(int spaceId) {
		this.spaceId = spaceId;
	}
	public int getVenueId() {
		return venueId;
	}
	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isAccessible() {
		return isAccessible;
	}
	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}
	public int getOpenFrom() {
		return openFrom;
	}
	public void setOpenFrom(int openFrom) {
		this.openFrom = openFrom;
	}
	public int getOpenTo() {
		return openTo;
	}
	public void setOpenTo(int openTo) {
		this.openTo = openTo;
	}
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal bigDecimal) {
		this.rate = bigDecimal;
	}
	public int getMaxOccupancy() {
		return maxOccupancy;
	}
	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isAccessible ? 1231 : 1237);
		result = prime * result + maxOccupancy;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + openFrom;
		result = prime * result + openTo;
		result = prime * result + ((rate == null) ? 0 : rate.hashCode());
		result = prime * result + spaceId;
		result = prime * result + venueId;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Space other = (Space) obj;
		if (isAccessible != other.isAccessible)
			return false;
		if (maxOccupancy != other.maxOccupancy)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (openFrom != other.openFrom)
			return false;
		if (openTo != other.openTo)
			return false;
		if (rate == null) {
			if (other.rate != null)
				return false;
		} else if (!rate.equals(other.rate))
			return false;
		if (spaceId != other.spaceId)
			return false;
		if (venueId != other.venueId)
			return false;
		return true;
	}
	
}
