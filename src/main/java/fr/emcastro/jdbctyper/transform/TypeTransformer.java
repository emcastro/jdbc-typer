package fr.emcastro.jdbctyper.transform;

public interface TypeTransformer<T> {

    Class<T> getType();

    Object toSql(T value);

    T fromSql(Object value);
}
