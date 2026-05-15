package fr.emcastro.jdbctyper.transform;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TypeTransformerRegistry {

    private static final List<TypeTransformer<?>> transformers = new CopyOnWriteArrayList<>();

    private TypeTransformerRegistry() {
    }

    public static <T> void register(TypeTransformer<T> transformer) {
        transformers.add(transformer);
    }

    @SuppressWarnings("unchecked")
    public static <T> Object toSql(T value) {
        if (value == null) {
            return null;
        }
        for (TypeTransformer<?> t : transformers) {
            if (t.getType().isInstance(value)) {
                return ((TypeTransformer<T>) t).toSql(value);
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromSql(Object value, Class<T> type) {
        if (value == null) {
            return null;
        }
        for (TypeTransformer<?> t : transformers) {
            if (t.getType() == type) {
                return ((TypeTransformer<T>) t).fromSql(value);
            }
        }
        if (type.isInstance(value)) {
            return (T) value;
        }
        throw new RuntimeException(
            "Unsupported conversion from " + value.getClass() + " to " + type.getName()
        );
    }
}
