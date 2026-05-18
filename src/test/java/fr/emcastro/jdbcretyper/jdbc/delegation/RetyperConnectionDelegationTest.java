package fr.emcastro.jdbcretyper.jdbc.delegation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.jdbc.RetyperConnection;
import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperConnectionDelegationTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private TypeTransformerRegistry registry;

    private RetyperConnection connection;

    @BeforeEach
    void setUp() {
        connection = new RetyperConnection(mockConnection, registry);
    }

    @Test
    // Check that getAutoCommit() delegates to the underlying Connection
    // without additional transformation.
    void getAutoCommit_delegates() throws SQLException {
        when(mockConnection.getAutoCommit()).thenReturn(true);
        assertTrue(connection.getAutoCommit());
        verify(mockConnection).getAutoCommit();
    }

    @Test
    // Check that setAutoCommit(boolean) delegates to the underlying
    // Connection without additional transformation.
    void setAutoCommit_delegates() throws SQLException {
        connection.setAutoCommit(false);
        verify(mockConnection).setAutoCommit(false);
    }

    @Test
    // Check that getCatalog() delegates to the underlying Connection
    // without additional transformation.
    void getCatalog_delegates() throws SQLException {
        when(mockConnection.getCatalog()).thenReturn("mydb");
        assertEquals("mydb", connection.getCatalog());
        verify(mockConnection).getCatalog();
    }

    @Test
    // Check that setCatalog(String) delegates to the underlying Connection
    // without additional transformation.
    void setCatalog_delegates() throws SQLException {
        connection.setCatalog("otherdb");
        verify(mockConnection).setCatalog("otherdb");
    }

    @Test
    // Check that getSchema() delegates to the underlying Connection
    // without additional transformation.
    void getSchema_delegates() throws SQLException {
        when(mockConnection.getSchema()).thenReturn("public");
        assertEquals("public", connection.getSchema());
        verify(mockConnection).getSchema();
    }

    @Test
    // Check that setSchema(String) delegates to the underlying Connection
    // without additional transformation.
    void setSchema_delegates() throws SQLException {
        connection.setSchema("other");
        verify(mockConnection).setSchema("other");
    }

    @Test
    // Check that getHoldability() delegates to the underlying Connection
    // without additional transformation.
    void getHoldability_delegates() throws SQLException {
        when(mockConnection.getHoldability()).thenReturn(1001);
        assertEquals(1001, connection.getHoldability());
        verify(mockConnection).getHoldability();
    }

    @Test
    // Check that setHoldability(int) delegates to the underlying
    // Connection without additional transformation.
    void setHoldability_delegates() throws SQLException {
        connection.setHoldability(1002);
        verify(mockConnection).setHoldability(1002);
    }

    @Test
    // Check that getTransactionIsolation() delegates to the underlying
    // Connection without additional transformation.
    void getTransactionIsolation_delegates() throws SQLException {
        when(mockConnection.getTransactionIsolation()).thenReturn(2);
        assertEquals(2, connection.getTransactionIsolation());
        verify(mockConnection).getTransactionIsolation();
    }

    @Test
    // Check that setTransactionIsolation(int) delegates to the underlying
    // Connection without additional transformation.
    void setTransactionIsolation_delegates() throws SQLException {
        connection.setTransactionIsolation(8);
        verify(mockConnection).setTransactionIsolation(8);
    }

    @Test
    // Check that isReadOnly() delegates to the underlying Connection
    // without additional transformation.
    void isReadOnly_delegates() throws SQLException {
        when(mockConnection.isReadOnly()).thenReturn(true);
        assertTrue(connection.isReadOnly());
        verify(mockConnection).isReadOnly();
    }

    @Test
    // Check that setReadOnly(boolean) delegates to the underlying
    // Connection without additional transformation.
    void setReadOnly_delegates() throws SQLException {
        connection.setReadOnly(true);
        verify(mockConnection).setReadOnly(true);
    }

    @Test
    // Check that isClosed() delegates to the underlying Connection
    // without additional transformation.
    void isClosed_delegates() throws SQLException {
        when(mockConnection.isClosed()).thenReturn(true);
        assertTrue(connection.isClosed());
        verify(mockConnection).isClosed();
    }

    @Test
    // Check that isValid(int) delegates to the underlying Connection
    // without additional transformation.
    void isValid_delegates() throws SQLException {
        when(mockConnection.isValid(5)).thenReturn(true);
        assertTrue(connection.isValid(5));
        verify(mockConnection).isValid(5);
    }

    @Test
    // Check that getWarnings() delegates to the underlying Connection
    // without additional transformation.
    void getWarnings_delegates() throws SQLException {
        SQLWarning warning = new SQLWarning("test");
        when(mockConnection.getWarnings()).thenReturn(warning);
        assertSame(warning, connection.getWarnings());
        verify(mockConnection).getWarnings();
    }

    @Test
    // Check that clearWarnings() delegates to the underlying Connection
    // without additional transformation.
    void clearWarnings_delegates() throws SQLException {
        connection.clearWarnings();
        verify(mockConnection).clearWarnings();
    }

    @Test
    // Check that commit() delegates to the underlying Connection without
    // additional transformation.
    void commit_delegates() throws SQLException {
        connection.commit();
        verify(mockConnection).commit();
    }

    @Test
    // Check that rollback() delegates to the underlying Connection without
    // additional transformation.
    void rollback_delegates() throws SQLException {
        connection.rollback();
        verify(mockConnection).rollback();
    }

    @Test
    // Check that nativeSQL(String) delegates to the underlying Connection
    // without additional transformation.
    void nativeSQL_delegates() throws SQLException {
        when(mockConnection.nativeSQL("SELECT 1")).thenReturn("SELECT 1");
        assertEquals("SELECT 1", connection.nativeSQL("SELECT 1"));
        verify(mockConnection).nativeSQL("SELECT 1");
    }

    @Test
    // Check that getMetaData() delegates to the underlying Connection
    // without additional transformation.
    void getMetaData_delegates() throws SQLException {
        DatabaseMetaData expected = mock(DatabaseMetaData.class);
        when(mockConnection.getMetaData()).thenReturn(expected);
        assertSame(expected, connection.getMetaData());
        verify(mockConnection).getMetaData();
    }

    @Test
    // Check that getClientInfo() delegates to the underlying Connection
    // without additional transformation.
    void getClientInfo_delegates() throws SQLException {
        Properties expected = new Properties();
        when(mockConnection.getClientInfo()).thenReturn(expected);
        assertEquals(expected, connection.getClientInfo());
        verify(mockConnection).getClientInfo();
    }

    @Test
    // Check that getNetworkTimeout() delegates to the underlying Connection
    // without additional transformation.
    void getNetworkTimeout_delegates() throws SQLException {
        when(mockConnection.getNetworkTimeout()).thenReturn(30000);
        assertEquals(30000, connection.getNetworkTimeout());
        verify(mockConnection).getNetworkTimeout();
    }
}
