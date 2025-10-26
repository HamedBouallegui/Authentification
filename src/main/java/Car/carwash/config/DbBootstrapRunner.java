package Car.carwash.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@Profile("db-bootstrap")
public class DbBootstrapRunner implements CommandLineRunner {

    @Value("${app.mysql.admin.url:jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC}")
    private String adminUrl;

    @Value("${app.mysql.admin.username:root}")
    private String adminUsername;

    @Value("${app.mysql.admin.password:}")
    private String adminPassword;

    @Value("${app.db.name:carwash}")
    private String dbName;

    @Value("${app.db.user:carwash_user}")
    private String appUser;

    @Value("${app.db.password:a-strong-password}")
    private String appPassword;

    @Override
    public void run(String... args) throws Exception {
        try (Connection conn = DriverManager.getConnection(adminUrl, adminUsername, adminPassword)) {
            try (Statement st = conn.createStatement()) {
                st.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
                st.executeUpdate("CREATE USER IF NOT EXISTS '" + appUser + "'@'localhost' IDENTIFIED BY '" + appPassword + "'");
                st.executeUpdate("GRANT ALL PRIVILEGES ON `" + dbName + "`.* TO '" + appUser + "'@'localhost'");
                st.execute("FLUSH PRIVILEGES");
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("Failed to bootstrap MySQL: " + ex.getMessage(), ex);
        }
    }
}


