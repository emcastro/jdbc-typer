package fr.emcastro.jdbcretyper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.GeometryTypeTransformer;
import fr.emcastro.jdbcretyper.transform.JsonBoxTypeTransformer;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

/**
 * Base class for DuckDB integration tests. Creates an in-memory DuckDB connection
 * wrapped with {@link RetyperConnection} and a pre-configured {@link TypeTransformerRegistry}.
 */
public abstract class DuckDBTestBase {

    protected TypeTransformerRegistry registry;
    protected RetyperConnection connection;

    protected void setUp() throws SQLException {
        registry = new TypeTransformerRegistry();
        registry.register(new JsonBoxTypeTransformer());
        registry.register(new GeometryTypeTransformer());

        Connection raw = DriverManager.getConnection("jdbc:duckdb:");
        connection = new RetyperConnection(raw, registry);

        // Load spatial extension for geometry tests
        try (Statement stmt = raw.createStatement()) {
            stmt.execute("INSTALL spatial");
            stmt.execute("LOAD spatial");
        }
    }

    protected void tearDown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
