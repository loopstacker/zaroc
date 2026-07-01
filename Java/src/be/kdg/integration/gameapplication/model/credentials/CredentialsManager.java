package be.kdg.integration.gameapplication.model.credentials;

// This class is an alternative to .env file for easy credential management
public final class CredentialsManager {
    public final static String DB_URL = "jdbc:postgresql://YOUR_IP:5432/game";
    public final static String DB_USER = "YOUR_DB_USER";
    public final static String DB_PASSWORD = "YOUR_DB_PASSWORD";
    public final static String SMTP_USER = "YOUR_EMAIL";
    public final static String SMTP_PASSWORD = "YOUR_SMTP_PASSWORD";
}
