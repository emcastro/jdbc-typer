package fr.emcastro.jdbcretyper.transform;

import fr.emcastro.jdbcretyper.JsonBox;

/**
 * Writes {@link JsonBox} to JDBC {@link String} columns.
 *
 * <p>DuckDB stores JSON physically as TEXT, so the write SQL type is {@code String.class}.
 */
public class JsonBoxWriteTransformer implements WriteTypeTransformer<JsonBox, String> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<String> getWriteSqlType() {
        return String.class;
    }

    @Override
    public String toSql(JsonBox appValue) {
        return appValue.value();
    }
}
