package fr.emcastro.jdbcretyper;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonBoxDuckDBTest extends DuckDBTestBase {

    @BeforeEach
    @Override
    protected void setUp() throws SQLException {
        super.setUp();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE json_test (id INTEGER, data JSON)");
        }
    }

    @AfterEach
    @Override
    protected void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS json_test");
        }
        super.tearDown();
    }

    @Test
    void insertAndReadJsonBox() throws SQLException {
        JsonBox box = new JsonBox("{\"name\":\"test\",\"value\":42}");

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (1, ?)")) {
            ps.setObject(1, box);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data::VARCHAR FROM json_test WHERE id = 1")) {
            assertTrue(rs.next());
            String jsonStr = rs.getString(1);
            JsonBox result = new JsonBox(jsonStr);
            assertEquals(box, result);
        }
    }

    @Test
    void insertAndReadJsonBoxDefaultType() throws SQLException {
        JsonBox box = new JsonBox("{\"nested\":{\"a\":1,\"b\":2}}");

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (2, ?)")) {
            ps.setObject(1, box);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data::VARCHAR FROM json_test WHERE id = 2")) {
            assertTrue(rs.next());
            String jsonStr = rs.getString(1);
            Object result = registry.fromSqlDefaultType(jsonStr);
            assertInstanceOf(JsonBox.class, result);
            assertEquals(box, result);
        }
    }

    @Test
    void nullJsonBoxRoundTrip() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (3, ?)")) {
            ps.setNull(1, java.sql.Types.VARCHAR);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data::VARCHAR FROM json_test WHERE id = 3")) {
            assertTrue(rs.next());
            rs.getString(1);
            assertTrue(rs.wasNull());
        }
    }

    @Test
    void jsonBoxToObject() throws SQLException {
        JsonBox box = new JsonBox("{\"name\":\"DuckDB\",\"version\":1}");

        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO json_test VALUES (4, ?)")) {
            ps.setObject(1, box);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT data::VARCHAR FROM json_test WHERE id = 4")) {
            assertTrue(rs.next());
            String jsonStr = rs.getString(1);
            JsonBox result = new JsonBox(jsonStr);
            TestData data = result.toObject(TestData.class);
            assertEquals("DuckDB", data.name);
            assertEquals(1, data.version);
        }
    }

    static class TestData {
        public String name;
        public int version;
    }
}
