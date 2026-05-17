package fr.emcastro.jdbcretyper.jdbc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.emcastro.jdbcretyper.transform.TypeTransformerRegistry;

@ExtendWith(MockitoExtension.class)
class RetyperResultSetTest {

    @Mock
    private ResultSet mockResultSet;

    private TypeTransformerRegistry registry;
    private RetyperResultSet retyperResultSet;

    @BeforeEach
    void setUp() {
        registry = spy(new TypeTransformerRegistry());
        retyperResultSet = new RetyperResultSet(mockResultSet, registry);
    }

    @Test
    void getObject_withClass_delegatesWithMapType() throws SQLException {
        when(mockResultSet.getObject(1, String.class)).thenReturn("raw-value");

        retyperResultSet.getObject(1, String.class);

        verify(registry).mapType(String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @Test
    void getObject_withClass_byLabel_delegatesWithMapType() throws SQLException {
        when(mockResultSet.getObject("col", String.class)).thenReturn("raw-value");

        retyperResultSet.getObject("col", String.class);

        verify(registry).mapType(String.class);
        verify(registry).fromSql("raw-value", String.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    void getObject_withMap_delegatesWithFromSqlMap() throws SQLException {
        Map<String, Class<?>> inputMap = Map.of("col", String.class);
        Map<String, Class<?>> convertedMap = Map.of("col", String.class);
        when(registry.fromSqlMap(any())).thenReturn(convertedMap);
        when(mockResultSet.getObject(anyInt(), any(Map.class))).thenReturn("raw-value");

        retyperResultSet.getObject(1, inputMap);

        verify(registry).fromSqlMap(inputMap);
        verify(mockResultSet).getObject(anyInt(), any(Map.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    void getObject_withMap_byLabel_delegatesWithFromSqlMap() throws SQLException {
        Map<String, Class<?>> inputMap = Map.of("col", String.class);
        Map<String, Class<?>> convertedMap = Map.of("col", String.class);
        when(registry.fromSqlMap(any())).thenReturn(convertedMap);
        when(mockResultSet.getObject(anyString(), any(Map.class))).thenReturn("raw-value");

        retyperResultSet.getObject("col", inputMap);

        verify(registry).fromSqlMap(inputMap);
        verify(mockResultSet).getObject(anyString(), any(Map.class));
    }

    @Test
    void getObject_defaultType_usesFromSqlDefaultType() throws SQLException {
        when(mockResultSet.getObject(1)).thenReturn("raw-value");

        retyperResultSet.getObject(1);

        verify(registry).fromSqlDefaultType("raw-value");
    }

    @Test
    void updateObject_usesToSql() throws SQLException {
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        retyperResultSet.updateObject(1, value);

        verify(registry).toSql(value);
    }

    @Test
    void updateObject_withScale_usesToSql() throws SQLException {
        Object value = new Object();
        when(registry.toSql(value)).thenReturn(value);

        retyperResultSet.updateObject(1, value, 2);

        verify(registry).toSql(value);
    }

    @Test
    void unwrap_returnsWrappedIfInstance() throws SQLException {
        ResultSet result = retyperResultSet.unwrap(ResultSet.class);

        assertSame(mockResultSet, result);
    }

    @Test
    void isWrapperFor_trueForWrappedInstance() throws SQLException {
        assertTrue(retyperResultSet.isWrapperFor(ResultSet.class));
    }

    @Test
    void next_delegatesToWrapped() throws SQLException {
        when(mockResultSet.next()).thenReturn(true);

        assertTrue(retyperResultSet.next());
    }

    @Test
    void wasNull_delegatesToWrapped() throws SQLException {
        when(mockResultSet.wasNull()).thenReturn(true);

        assertTrue(retyperResultSet.wasNull());
    }
}
