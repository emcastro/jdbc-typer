package fr.emcastro.jdbcretyper.transform;

import java.sql.Blob;
import java.sql.SQLException;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKBReader;

import fr.emcastro.jdbcretyper.exception.TypeConversionException;

/**
 * Reads JTS {@link Geometry} (Point, LineString, etc.) from DuckDB GEOMETRY columns.
 *
 * <p>DuckDB's JDBC driver currently does not support typed {@code getObject(int, Class)}
 * for GEOMETRY columns. The driver returns GEOMETRY values as a {@link Blob} containing
 * WKB (Well-Known Binary), so this transformer reads WKB from the Blob and parses it
 * into a JTS Geometry.
 */
public class GeometryReadTransformer implements ReadTypeTransformer<Geometry, Blob> {

    private final GeometryFactory geometryFactory = new GeometryFactory();

    @Override
    public Class<Geometry> getAppType() {
        return Geometry.class;
    }

    @Override
    public Class<Blob> getReadSqlType() {
        return Blob.class;
    }

    @Override
    public boolean supportsTypedGetObject() {
        return false;
    }

    @Override
    public Geometry fromSql(Blob sqlValue) {
        try {
            byte[] wkb = sqlValue.getBytes(1, (int) sqlValue.length());
            return new WKBReader(geometryFactory).read(wkb);
        } catch (SQLException | ParseException e) {
            throw new TypeConversionException("Failed to convert WKB Blob to Geometry", e);
        } finally {
            try {
                sqlValue.free();
            } catch (SQLException ignored) {
            }
        }
    }
}
