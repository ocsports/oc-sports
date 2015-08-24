package com.ocsports.core;

import java.util.Calendar;

public class DateHelper {
	public static int getCurrentYear() {
		return Calendar.getInstance().get( Calendar.YEAR );	
	}
}
