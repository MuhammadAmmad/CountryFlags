package com.example.countryflags;

public class Question {
	private String country;
	private int flagResId;
	private int id;
	
	public Question(String country, int flagResId,int id) {
		super();
		this.country = country;
		this.flagResId = flagResId;
		this.id = id;
	}
	
	public boolean isRight(String country){
		return this.country.equals(country);
	}
	
	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
	}


	public int getFlagResId() {
		return flagResId;
	}



	public void setFlagResId(int flagResId) {
		this.flagResId = flagResId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	



	



	
	
	
}
