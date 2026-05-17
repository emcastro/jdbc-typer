package fr.emcastro.jdbcretyper.demo.transform;

import fr.emcastro.jdbcretyper.demo.JsonBox;
import fr.emcastro.jdbcretyper.transform.ReadTypeTransformer;

/**
 * Reads {@link JsonBox} from JDBC {@link String} columns for the demo.
 *
 * <p>DuckDB stores JSON physically as TEXT, so the read SQL type is {@code String.class}.
 */
public class JsonBoxReadTransformer implements ReadTypeTransformer<JsonBox, org.duckdb. JsonNode    > {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<String> getReadSqlType() {
        return String.class;
    }

    @Override
    public JsonBox fromSql(String sqlValue) {
        return new JsonBox(sqlValue);
    }
}
