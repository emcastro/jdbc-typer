# Test Roadmap

## Strategy

- All tests use **in-memory DuckDB** (`jdbc:duckdb:`) for real integration testing.
- **Mockito** is used for unit tests where a full database is unnecessary (registry logic, wrapper delegation).
- DuckDB **spatial extension** is loaded for geometry tests (`INSTALL spatial; LOAD spatial;`).
- DuckDB **JSON** is a core type (no extension needed), physically stored as `TEXT`.

## DuckDB Type Mapping

| App Type | Column Type | Read SQL Type | Write SQL Type | Write Strategy | Read Strategy |
|---|---|---|---|---|---|---|
| `JsonBox` | `JSON` | `JsonNode` | `String` | `.value()` → `String` via `setObject` | `JsonNode.toString()` → `JsonBox` via `getObject(int)` (no type hint) |
| `Point` / `LineString` | `GEOMETRY` | — (blocked) | `byte[]` (WKB) | `WKBWriter.write()` → `ST_GeomFromWKB(?)` | — (DuckDB `getObject(int, Class)` unsupported) |

## Test Suites

### 1. `TypeTransformerRegistryTest` (Mockito unit tests)
- `register()` adds transformers
- `toSql()` finds matching transformer, falls back to original value
- `fromSql()` finds matching transformer, throws on unsupported conversion
- `fromSqlDefaultType()` transforms or returns raw value
- `mapType()` returns SQL type for registered app type, falls back
- `fromSqlMap()` converts all values in a type map
- Null handling for all methods

### 2. `JsonBoxDuckDBTest` (in-memory DuckDB)
- Create table with `JSON` column
- Insert `JsonBox` via `RetyperPreparedStatement.setObject()`
- Read back via `RetyperResultSet.getObject(columnIndex, JsonBox.class)`
- Read back via `RetyperResultSet.getObject(columnIndex)` (default type)
- Null `JsonBox` round-trip
- Nested JSON object round-trip
- Prepared statement parameter binding

### 3. `GeometryDuckDBTest` (in-memory DuckDB with spatial extension)
- Load spatial extension
- Create table with `GEOMETRY` column
- Insert JTS `Point` via `RetyperPreparedStatement.setObject()`
- Read back via `RetyperResultSet.getObject(columnIndex, Point.class)`
- Insert JTS `LineString` and read back
- Null geometry round-trip
- Blob/WKB binary format compatibility

### 4. `RetyperResultSetTest` (Mockito unit tests)
- `getObject(int, Class)` delegates with `registry.mapType()` and `registry.fromSql()`
- `getObject(int, Map)` delegates with `registry.fromSqlMap()`
- `updateObject()` delegates with `registry.toSql()`
- `unwrap()` / `isWrapperFor()` behavior

### 5. `RetyperConnectionTest` (Mockito unit tests)
- `createStatement()` returns `RetyperStatement`
- `prepareStatement()` returns `RetyperPreparedStatement`
- `prepareCall()` returns `RetyperCallableStatement`
- `unwrap()` / `isWrapperFor()` behavior

### 6. `RetyperDatasourceTest` (Mockito unit tests)
- `getConnection()` returns `RetyperConnection`
- `unwrap()` / `isWrapperFor()` behavior
