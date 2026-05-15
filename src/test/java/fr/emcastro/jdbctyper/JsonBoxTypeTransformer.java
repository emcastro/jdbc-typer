package fr.emcastro.jdbctyper;

import fr.emcastro.jdbctyper.JsonBox;
import fr.emcastro.jdbctyper.transform.TypeTransformer;

public class JsonBoxTypeTransformer implements TypeTransformer<JsonBox> {

    @Override
    public Class<JsonBox> getType() {
        return JsonBox.class;
    }

    @Override
    public Object toSql(JsonBox value) {
        return value.value();
    }

    @Override
    public JsonBox fromSql(Object value) {
        if (value instanceof String str) {
            return new JsonBox(str);
        }
        throw new RuntimeException("Cannot convert " + value.getClass() + " to JsonBox");
    }
}
