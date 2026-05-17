package fr.emcastro.jdbcretyper.transform;

import org.locationtech.jts.geom.Geometry;

/**
 * Reads JTS {@link Geometry} (Point, LineString, etc.) from DuckDB GEOMETRY columns.
 *
 * <p>When DuckDB supports {@code getObject(int, Geometry.class)}, this transformer will
 * delegate directly. Currently DuckDB JDBC does not support typed {@code getObject} for
 * GEOMETRY, so this serves as a placeholder for when the driver catches up.
 */
public class GeometryReadTransformer implements ReadTypeTransformer<Geometry, Geometry> {

    @Override
    public Class<Geometry> getAppType() {
        return Geometry.class;
    }

    @Override
    public Class<Geometry> getReadSqlType() {
        return Geometry.class;
    }

    @Override
    public Geometry fromSql(Geometry sqlValue) {
        return sqlValue;
    }
}
