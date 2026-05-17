package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperConnectionTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private TypeTransformerRegistry registry;

    @Mock
    private Statement mockStatement;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private CallableStatement mockCallableStatement;

    private RetyperConnection connection;

    @BeforeEach
    void setUp() {
        connection = new RetyperConnection(mockConnection, registry);
    }

    @Test
    // Check that createStatement() wraps the driver's Statement in a
    // RetyperStatement carrying the registry and connection reference.
    void createStatement_returnsRetyperStatement() throws SQLException {
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        assertInstanceOf(RetyperStatement.class, connection.createStatement());
    }

    @Test
    // Check that prepareStatement() wraps the driver's PreparedStatement
    // in a RetyperPreparedStatement for parameter transformation.
    void prepareStatement_returnsRetyperPreparedStatement() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        assertInstanceOf(RetyperPreparedStatement.class, connection.prepareStatement("SELECT 1"));
    }

    @Test
    // Check that prepareCall() wraps the driver's CallableStatement in
    // a RetyperCallableStatement for OUT parameter transformation.
    void prepareCall_returnsRetyperCallableStatement() throws SQLException {
        when(mockConnection.prepareCall(anyString())).thenReturn(mockCallableStatement);

        assertInstanceOf(RetyperCallableStatement.class, connection.prepareCall("{call proc()}"));
    }

    @Test
    // Check that unwrap(Connection.class) returns the underlying driver
    // Connection when the requested type matches the delegate.
    void unwrap_returnsWrappedIfInstance() throws SQLException {
        assertSame(mockConnection, connection.unwrap(Connection.class));
    }

    @Test
    // Check that isWrapperFor(Connection.class) is true because the
    // wrapped Connection implements that interface.
    void isWrapperFor_trueForWrappedInstance() throws SQLException {
        assertTrue(connection.isWrapperFor(Connection.class));
    }

    @Test
    // Check that close() delegates to the wrapped Connection without
    // additional cleanup or transformation.
    void close_delegatesToWrapped() throws SQLException {
        connection.close();

        verify(mockConnection).close();
    }
}
