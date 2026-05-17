package fr.emcastro.jdbcretyper.transform;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.emcastro.jdbcretyper.exception.TypeConversionException;

class TypeTransformerRegistryTest {

    private TypeTransformerRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new TypeTransformerRegistry();
        registry.register(new TestTransformer());
    }

    @Test
    void toSql_findsMatchingTransformer() {
        Object result = registry.toSql(new TestAppValue("hello"));
        assertEquals("SQL:hello", result);
    }

    @Test
    void toSql_returnsOriginalWhenNoMatch() {
        Object result = registry.toSql("unregistered");
        assertEquals("unregistered", result);
    }

    @Test
    void toSql_handlesNull() {
        assertNull(registry.toSql(null));
    }

    @Test
    void fromSql_findsMatchingTransformer() {
        TestAppValue result = registry.fromSql("SQL:hello", TestAppValue.class);
        assertEquals("SQL:hello", result.getValue());
    }

    @Test
    void fromSql_returnsRawValueWhenAlreadyTargetType() {
        String result = registry.fromSql("raw", String.class);
        assertEquals("raw", result);
    }

    @Test
    void fromSql_throwsOnUnsupportedConversion() {
        assertThrows(TypeConversionException.class, () -> registry.fromSql(42, TestAppValue.class));
    }

    @Test
    void fromSql_handlesNull() {
        assertNull(registry.fromSql(null, TestAppValue.class));
    }

    @Test
    void fromSqlDefaultType_transformsWhenMatch() {
        Object result = registry.fromSqlDefaultType("SQL:hello");
        assertInstanceOf(TestAppValue.class, result);
        assertEquals("SQL:hello", ((TestAppValue) result).getValue());
    }

    @Test
    void fromSqlDefaultType_returnsRawWhenNoMatch() {
        Object result = registry.fromSqlDefaultType(42);
        assertEquals(42, result);
    }

    @Test
    void fromSqlDefaultType_handlesNull() {
        assertNull(registry.fromSqlDefaultType(null));
    }

    @Test
    void mapType_returnsSqlTypeForRegisteredAppType() {
        Class<?> result = registry.mapType(TestAppValue.class);
        assertEquals(String.class, result);
    }

    @Test
    void mapType_returnsOriginalWhenNoMatch() {
        Class<?> result = registry.mapType(Integer.class);
        assertEquals(Integer.class, result);
    }

    @Test
    void fromSqlMap_convertsAllEntries() {
        var map = java.util.Map.of("col1", TestAppValue.class, "col2", Integer.class);
        var result = registry.fromSqlMap(map);
        assertEquals(String.class, result.get("col1"));
        assertEquals(Integer.class, result.get("col2"));
    }

    @Test
    void fromSqlMap_handlesNull() {
        assertNull(registry.fromSqlMap(null));
    }

    /**
     * Test transformer: TestAppValue <-> String with "SQL:" prefix.
     */
    static class TestAppValue {
        private final String value;

        TestAppValue(String value) {
            this.value = value;
        }

        String getValue() {
            return value;
        }
    }

    static class TestTransformer implements TypeTransformer<TestAppValue, String> {
        @Override
        public Class<TestAppValue> getAppType() {
            return TestAppValue.class;
        }

        @Override
        public Class<String> getSqlType() {
            return String.class;
        }

        @Override
        public String toSql(TestAppValue appValue) {
            return "SQL:" + appValue.getValue();
        }

        @Override
        public TestAppValue fromSql(String sqlValue) {
            return new TestAppValue(sqlValue);
        }
    }
}
