package com.harborspace.spacecraft.new_mail;

public class Constants {

    public static String host = "smtp.gmail.com";
    public static String username = requiredEnvironment("MAIL_USERNAME");
    public static String port = "587";
    public static String password = requiredEnvironment("MAIL_PASSWORD");
	public static String from = requiredEnvironment("MAIL_FROM");
    private static String requiredEnvironment(String name) {
        String value = System.getenv(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing required environment variable: " + name);
        }
        return value;
    }
}
