package org.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private final Connection connection =
            Database.getInstance().getConnection();

    private void validateName(String name) {

        if (name == null) {
            throw new IllegalArgumentException("Name is null");
        }

        if (name.length() < 2 || name.length() > 1000) {
            throw new IllegalArgumentException("Invalid name");
        }
    }

    public long create(String name) {

        validateName(name);

        try {

            String sql =
                    "INSERT INTO CLIENT(NAME) VALUES(?)";

            PreparedStatement stmt =
                    connection.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );

            stmt.setString(1, name);
            stmt.executeUpdate();

            ResultSet rs =
                    stmt.getGeneratedKeys();

            rs.next();

            return rs.getLong(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getById(long id) {

        try {

            PreparedStatement stmt =
                    connection.prepareStatement(
                            "SELECT NAME FROM CLIENT WHERE ID=?"
                    );

            stmt.setLong(1, id);

            ResultSet rs =
                    stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("NAME");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void setName(long id, String name) {

        validateName(name);

        try {

            PreparedStatement stmt =
                    connection.prepareStatement(
                            "UPDATE CLIENT SET NAME=? WHERE ID=?"
                    );

            stmt.setString(1, name);
            stmt.setLong(2, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteById(long id) {

        try {

            PreparedStatement stmt =
                    connection.prepareStatement(
                            "DELETE FROM CLIENT WHERE ID=?"
                    );

            stmt.setLong(1, id);

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Client> listAll() {

        List<Client> clients =
                new ArrayList<>();

        try {

            Statement stmt =
                    connection.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT * FROM CLIENT"
                    );

            while (rs.next()) {

                clients.add(
                        new Client(
                                rs.getLong("ID"),
                                rs.getString("NAME")
                        )
                );
            }

            return clients;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
