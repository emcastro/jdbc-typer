package fr.emcastro.jdbcretyper.transform;

import fr.emcastro.jdbcretyper.JsonBox;

/**
 * Transforms {@link JsonBox} to/from {@link String} for JDBC storage.
 *
 * <p>DuckDB stores JSON physically as VARCHAR, so the SQL type is {@code String.class}.
 */
public class JsonBoxTypeTransformer implements TypeTransformer<JsonBox, String> {

    @Override
    public Class<JsonBox> getAppType() {
        return JsonBox.class;
    }

    @Override
    public Class<String> getSqlType() {
        return String.class;
    }

    @Override
    public String toSql(JsonBox appValue) {
        return appValue.getJson();
    }

    @Override
    public JsonBox fromSql(String sqlValue) {
        return new JsonBox(sqlValue);
    }
}
