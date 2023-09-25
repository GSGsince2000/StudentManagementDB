package com.rest.services;


public interface StudentLoggerService  {
	
	public static final String Saved = "Saved The Data";
	public static final String Fetched = "Fetched The Data";
	public static final String FetchedById = "Fetched The Data By ID";
	public static final String Updated = "Updated The Data";
	public static final String Deleted = "Deleted The Data";
	
	void info(String message);
	
}
