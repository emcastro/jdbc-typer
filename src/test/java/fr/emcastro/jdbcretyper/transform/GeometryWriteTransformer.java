package fr.emcastro.jdbcretyper.transform;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKBWriter;

/**
 * Writes JTS {@link Geometry} (Point, LineString, etc.) to DuckDB GEOMETRY columns as
 * WKB {@code byte[]}.
 *
 * <p>Insertion uses {@code ST_GeomFromWKB(?)} via
 * {@code PreparedStatement.setObject()}. WKB is a portable binary representation that
 * avoids the textual ambiguity of WKT.
 */
public class GeometryWriteTransformer implements WriteTypeTransformer<Geometry, byte[]> {

    private final WKBWriter wkbWriter = new WKBWriter();

    @Override
    public Class<Geometry> getAppType() {
        return Geometry.class;
    }

    @Override
    public Class<byte[]> getWriteSqlType() {
        return byte[].class;
    }

    @Override
    public byte[] toSql(Geometry appValue) {
        return wkbWriter.write(appValue);
    }
}
