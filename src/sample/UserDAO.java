package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class.getName());
    private static final String INSERT_USER_SQL = "INSERT INTO users (first_name, last_name) VALUES (?, ?)";
    private static final String SELECT_ALL_USERS_SQL = "SELECT * FROM users";
    private static final String UPDATE_USER_SQL = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";
    private static final String DELETE_USER_SQL = "DELETE FROM users WHERE id = ?";

    // Create
    public void addUser(String firstName, String lastName) throws SQLException {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER_SQL)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding user: " + e.getMessage(), e);
            throw e; // Rethrow the exception with context
        }
    }

    // Read
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseHelper.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS_SQL)) {
            while (rs.next()) {
                User user = new User(rs.getInt("id"),
                                     rs.getString("first_name"),
                                     rs.getString("last_name"));
                users.add(user);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving users: " + e.getMessage(), e);
            throw e; // Rethrow the exception with context
        }
        return users;
    }

    // Update
    public void updateUser(int id, String firstName, String lastName) throws SQLException {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER_SQL)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setInt(3, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating user: " + e.getMessage(), e);
            throw e; // Rethrow the exception with context
        }
    }

    // Delete
    public void deleteUser(int id) throws SQLException {
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER_SQL)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting user: " + e.getMessage(), e);
            throw e; // Rethrow the exception with context
        }
    }
}
