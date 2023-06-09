package com.example.orienteering.typeHandler;

import com.example.orienteering.entity.GeoPoint;
import com.example.orienteering.util.GeoPointUtil;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 经维度坐标类型处理器
 */
@MappedTypes({GeoPoint.class})
public class GeoPointTypeHandler extends BaseTypeHandler<GeoPoint> {

    GeoPointUtil converter = new GeoPointUtil();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, GeoPoint parameter, JdbcType jdbcType) throws SQLException {
        ps.setBytes(i, converter.to(parameter));
    }

    @Override
    public GeoPoint getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return converter.from(rs.getBytes(columnName));
    }

    @Override
    public GeoPoint getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return converter.from(rs.getBytes(columnIndex));
    }

    @Override
    public GeoPoint getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return converter.from(cs.getBytes(columnIndex));
    }
}

