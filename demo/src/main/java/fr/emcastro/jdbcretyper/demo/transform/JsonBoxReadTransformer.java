package fr.emcastro.jdbcretyper.demo.transform;

import fr.emcastro.jdbcretyper.demo.JsonBox;
import fr.emcastro.jdbcretyper.transform.ReadTypeTransformer;
import org.duckdb.JsonNode;

/**
 * Reads {@link JsonBox} from JDBC {@link JsonNode} columns for the demo.
 *
 * <p>DuckDB stores JSON physically as TEXT, so the read SQL type is {@code JsonNode.class}.
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
