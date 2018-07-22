package com.example.util;

public class Log {

	public static boolean enabled = true;
	
	public static void debug(Object message) {
		if (!enabled) {
			return;
		}
		
		System.out.println(message == null ? null : message.toString());
	}
}
