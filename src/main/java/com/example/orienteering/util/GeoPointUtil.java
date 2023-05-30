package com.example.orienteering.util;

import com.example.orienteering.entity.GeoPoint;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import com.vividsolutions.jts.io.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 地理空间转化工具
 */
public class GeoPointUtil {

    /**
     * 高/低位字节存放
     */
    private int byteOrder = ByteOrderValues.LITTLE_ENDIAN;

    /**
     * 准确率模型
     */
    private PrecisionModel precisionModel = new PrecisionModel();

    /**
     * 坐标序列工厂
     */
    private CoordinateSequenceFactory coordinateSequenceFactory = CoordinateArraySequenceFactory.instance();

    /**
     * 输出维度
     */
    private int outputDimension = 2;

    /**
     * 转换包含SRID + WKB地理坐标的字节数组为GeoPoint
     */
    public GeoPoint from(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes)) {
            // 读取SRID
            byte[] sridBytes = new byte[4];
            inputStream.read(sridBytes);
            int srid = ByteOrderValues.getInt(sridBytes, byteOrder);

            // 地理坐标工厂
            GeometryFactory geometryFactory = new GeometryFactory(precisionModel, srid, coordinateSequenceFactory);

            // 读取坐标
            WKBReader wkbReader = new WKBReader(geometryFactory);
            Geometry geometry = wkbReader.read(new InputStreamInStream(inputStream));
            Point point = (Point) geometry;

            // 转换为GeoPoint模型
            GeoPoint geoPoint = new GeoPoint(point.getX(), point.getY());
            return geoPoint;
        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Geometry转为SRID + WKB Geometry
     */
    public byte[] to(GeoPoint geoPoint) {
        if (geoPoint == null) {
            return null;
        }
        Coordinate coordinate = new Coordinate(geoPoint.getLat(), geoPoint.getLng());
        CoordinateArraySequence coordinateArraySequence = new CoordinateArraySequence(new Coordinate[]{coordinate}, 2);
        Point point = new Point(coordinateArraySequence, new GeometryFactory());
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] sridBytes = new byte[4];
            ByteOrderValues.putInt(point.getSRID(), sridBytes, byteOrder);
            outputStream.write(sridBytes);

            WKBWriter wkbWriter = new WKBWriter(outputDimension, byteOrder);
            wkbWriter.write(point, new OutputStreamOutStream(outputStream));
            return outputStream.toByteArray();
        } catch (IOException ioe) {
            throw new IllegalArgumentException(ioe);
        }
    }
}


