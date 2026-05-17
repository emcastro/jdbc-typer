package fr.emcastro.jdbcretyper;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

class GeometryDuckDBTest extends DuckDBTestBase {

    private final GeometryFactory gf = new GeometryFactory();

    @BeforeEach
    @Override
    protected void setUp() throws SQLException {
        super.setUp();
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE geom_test (id INTEGER, geom GEOMETRY)");
        }
    }

    @AfterEach
    @Override
    protected void tearDown() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS geom_test");
        }
        super.tearDown();
    }

    @Test
    void insertAndReadPoint() throws SQLException {
        Point point = gf.createPoint(new Coordinate(30.0, 10.0));

        try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO geom_test VALUES (1, ST_GeomFromWKB(?))")) {
            ps.setObject(1, point);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT geom FROM geom_test WHERE id = 1")) {
            assertTrue(rs.next());
            Point result = rs.getObject(1, Point.class);
            assertEquals(30.0, result.getX(), 0.001);
            assertEquals(10.0, result.getY(), 0.001);
        }
    }

    @Test
    void insertAndReadLineString() throws SQLException {
        LineString line = gf.createLineString(
                new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 0)});

        try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO geom_test VALUES (2, ST_GeomFromWKB(?))")) {
            ps.setObject(1, line);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT geom FROM geom_test WHERE id = 2")) {
            assertTrue(rs.next());
            LineString result = rs.getObject(1, LineString.class);
            assertEquals(3, result.getNumPoints());
            assertEquals(0, result.getCoordinateN(0).x, 0.001);
            assertEquals(2, result.getCoordinateN(2).x, 0.001);
        }
    }

    @Test
    void nullGeometryRoundTrip() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO geom_test VALUES (3, NULL)")) {
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT geom FROM geom_test WHERE id = 3")) {
            assertTrue(rs.next());
            Object result = rs.getObject(1);
            assertNull(result);
        }
    }

    @Test
    void geometryFromSqlWithType() throws SQLException {
        Point point = gf.createPoint(new Coordinate(45.0, -12.0));

        try (PreparedStatement ps =
                connection.prepareStatement("INSERT INTO geom_test VALUES (4, ST_GeomFromWKB(?))")) {
            ps.setObject(1, point);
            ps.execute();
        }

        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT geom FROM geom_test WHERE id = 4")) {
            assertTrue(rs.next());
            Point result = registry.fromSql(rs.getObject(1), Point.class);
            assertEquals(45.0, result.getX(), 0.001);
            assertEquals(-12.0, result.getY(), 0.001);
        }
    }
}
