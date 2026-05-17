package fr.emcastro.jdbcretyper.transform;

import fr.emcastro.jdbcretyper.JsonBox;
import org.duckdb.JsonNode;

/**
 * Reads {@link JsonBox} from JDBC {@link String} columns.
 *
 * <p>DuckDB stores JSON physically as TEXT, so the read SQL type is {@code String.class}.
 */
public class JsonBoxReadTransformer implements ReadTypeTransformer<JsonBox, JsonNode> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<JsonNode> getReadSqlType() {
        return JsonNode.class;
    }

    @Override
    public boolean jdbcDriverIsTypeAware() {
        return false;
    }

    @Override
    public JsonBox fromSql(JsonNode sqlValue) {
        return new JsonBox(sqlValue.toString());
    }
}
