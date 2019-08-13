package dev.foolen.playtime.utils;

public class Logger {

	private static final String PREFIX = "[PlayTime]";

	public static void info(String message) {
		System.out.println(PREFIX + "[INFO] " + message);
	}

	public static void warning(String message) {
		System.out.println(PREFIX + "[WARNING] " + message);
	}

	public static void error(String message) {
		System.out.println(PREFIX + "[ERROR] " + message);
	}
}
