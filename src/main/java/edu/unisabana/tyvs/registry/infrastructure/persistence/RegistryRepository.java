package edu.unisabana.tyvs.registry.infrastructure.persistence;

import edu.unisabana.tyvs.registry.application.port.out.RegistryRepositoryPort;
import edu.unisabana.tyvs.registry.domain.model.Person;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RegistryRepository implements RegistryRepositoryPort {

    private final String jdbcUrl;

    public RegistryRepository(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    private Connection getConnection() throws Exception {
        return DriverManager.getConnection(jdbcUrl);
    }

    @Override
    public void initSchema() throws Exception {

        String sql = """
            CREATE TABLE IF NOT EXISTS voters(
                id INT PRIMARY KEY,
                name VARCHAR(100),
                age INT,
                gender VARCHAR(20),
                alive BOOLEAN
            )
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
        }
    }

    @Override
    public void deleteAll() throws Exception {

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM voters");
        }
    }

    @Override
    public boolean existsById(int id) throws Exception {

        String sql = "SELECT COUNT(*) FROM voters WHERE id=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            rs.next();

            return rs.getInt(1) > 0;
        }
    }

    @Override
    public void save(Person person) throws Exception {

        String sql =
                "INSERT INTO voters(id,name,age,gender,alive) VALUES(?,?,?,?,?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, person.getId());
            stmt.setString(2, person.getName());
            stmt.setInt(3, person.getAge());
            stmt.setString(4, person.getGender().name());
            stmt.setBoolean(5, person.isAlive());

            stmt.executeUpdate();
        }
    }
}