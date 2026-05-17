package fr.emcastro.jdbcretyper.transform;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.locationtech.jts.io.WKTWriter;

/**
 * Transforms JTS {@link Geometry} (Point, LineString, etc.) to/from WKT {@link String}
 * for JDBC storage in DuckDB GEOMETRY columns.
 *
 * <p>DuckDB returns geometry as WKT via {@code ResultSet.getString()}, so the SQL type
 * is {@code String.class}. Insertion uses {@code ST_GeomFromText(?)} via setObject.
 */
public class GeometryTypeTransformer implements TypeTransformer<Geometry, String> {

    private final WKTReader wktReader = new WKTReader();
    private final WKTWriter wktWriter = new WKTWriter();

    @Override
    public Class<Geometry> getAppType() {
        return Geometry.class;
    }

    @Override
    public Class<String> getSqlType() {
        return String.class;
    }

    @Override
    public String toSql(Geometry appValue) {
        return wktWriter.write(appValue);
    }

    @Override
    public Geometry fromSql(String sqlValue) {
        try {
            return wktReader.read(sqlValue);
        } catch (ParseException e) {
            throw new RuntimeException("Failed to parse WKT: " + sqlValue, e);
        }
    }
}
