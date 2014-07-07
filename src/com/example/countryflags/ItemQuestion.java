package com.example.countryflags;

import java.io.Serializable;
import java.util.ArrayList;

import android.graphics.Bitmap;

public class ItemQuestion implements Serializable {

	private static final long serialVersionUID = 144L;
	private Question question;
	private ArrayList<String> countries;
	private Bitmap srcBitmapLocal;
	
	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public ArrayList<String> getCountries() {
		return countries;
	}

	public void setCountries(ArrayList<String> countries) {
		this.countries = countries;
	}

	public Bitmap getFLagBitmap() {
		return srcBitmapLocal;
	}

	public void setSrcBitmapLocal(Bitmap srcBitmapLocal) {
		this.srcBitmapLocal = srcBitmapLocal;
	}

	public ItemQuestion(Question question, ArrayList<String> countries,Bitmap bitmap) {
		super();
		this.question = question;
		this.countries = countries;
		this.srcBitmapLocal = bitmap;
	}
	
	
	
}
