package com.moberella.model;

public final class Operation {
	
	public static Operation getInstance(String id, String englishDescription, String arabicDescription) {
		return new Operation(id, englishDescription, arabicDescription);
	}
	
	private Operation(String id, String englishDescription, String arabicDescription) {
		this.id = id;
		this.englishDescription = englishDescription;
		this.arabicDescription = arabicDescription;
	}
	
	public String getId() {
		return id;
	}

	public String getEnglishDescription() {
		return englishDescription;
	}

	public String getArabicDescription() {
		return arabicDescription;
	}

	private final String id;
	private final String englishDescription;
	private final String arabicDescription;

}
