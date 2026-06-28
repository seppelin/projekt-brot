package src.leaderboard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class DB {
    private static final String url = "jdbc:mysql://selectrolution.de:3306/brot"
            + "?useSSL=true"
            + "&verifyServerCertificate=false"
            + "&allowPublicKeyRetrieval=true";
    private static Connection conn;

    public static void asyncInit() {
        CompletableFuture.supplyAsync(() -> {
                    var envs = loadEnv(".env");
                    // Enforce SSL, but skip checking if the certificate is self-signed

                    var user = envs.get("DB_USER");
                    var pass = envs.get("DB_PASSWORD");
                    try {
                        return DriverManager.getConnection(url, user, pass);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }, Executors.newVirtualThreadPerTaskExecutor())
                .thenAccept(connection -> conn = connection)
                .exceptionally(ex -> {
                    throw new RuntimeException(ex);
                });
    }

    public static void init() {
        var envs = loadEnv(".env");
        // Enforce SSL, but skip checking if the certificate is self-signed

        var user = envs.get("DB_USER");
        var pass = envs.get("DB_PASSWORD");
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void printMatchEntries() {
        try {
            Statement stmt = conn.createStatement();
            // Explicitly list the columns instead of using *
            ResultSet rs = stmt.executeQuery("SELECT mapName, playerName, goldLeft FROM leaderboard");

            while (rs.next()) {
                // This loops and prints the safe data exactly as before
                System.out.println(rs.getString("mapName") + ", " + rs.getString("playerName") + ": " + rs.getInt("goldLeft"));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<String> getCompMaps() {
        try {
            var names = new ArrayList<String>();
            var stmt = conn.createStatement();
            var rs = stmt.executeQuery("SELECT name FROM maps");
            while (rs.next()) {
                names.add(rs.getString("name"));
            }
            return names;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addLeaderBoard(String mapName, String playerName, int goldLeft, int mapHash) {
        String query = "INSERT INTO leaderboard (mapName, playerName, goldLeft, mapHash) VALUES (?, ?, ?, ?)";
        try (var stmt = conn.prepareStatement(query)) {
            stmt.setString(1, mapName);
            stmt.setString(2, playerName);
            stmt.setInt(3, goldLeft);
            stmt.setInt(4, mapHash);

            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                System.out.println("Map leaderboard skipped: bad hash");
                return;
            }
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<MatchEntry> getMatchEntries(String mapName, int count) {
        ArrayList<MatchEntry> entries = new ArrayList<>();
        String query = "SELECT mapName, playerName, goldLeft FROM leaderboard " +
                "WHERE mapName = ? " +
                "ORDER BY goldLeft DESC LIMIT ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, mapName); // Handles quotes around string automatically
            stmt.setInt(2, count);      // Safely appends the limit count

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    entries.add(new MatchEntry(Optional.empty(), rs.getString("mapName"), rs.getString("playerName"), rs.getInt("goldLeft"), 0));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return entries;
    }

    public static boolean addCompMap(String mapName, int mapHash, String password) {
        try {
            var adminConn = DriverManager.getConnection(url, "brot_admin", password);
            var pstmt = adminConn.prepareStatement("INSERT INTO maps (name, hash) VALUES (?, ?)");
            pstmt.setString(1, mapName);
            pstmt.setInt(2, mapHash);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            // 1062 is the standard MySQL/MariaDB error code for "Duplicate entry"
            if (e.getErrorCode() == 1062) {
                System.out.println("Map insertion skipped: " + mapName + " already exists.");
                return false;
            }
            throw new RuntimeException(e);
        }
    }

    public static void saveMatch(MatchEntry matchEntry) {
        String sql = "INSERT INTO leaderboard (mapName, playerName, goldLeft, mapHash) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, matchEntry.mapName());
            pstmt.setString(2, matchEntry.playerName());
            pstmt.setInt(3, matchEntry.goldLeft());
            pstmt.setInt(4, matchEntry.mapHash());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Query OK, " + rowsAffected + " row affected.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> loadEnv(String filePath) {
        Map<String, String> envMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                // Ignore comments and empty lines
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Split only on the first '='
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    envMap.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not read .env file: " + e.getMessage());
        }
        return envMap;
    }
}
