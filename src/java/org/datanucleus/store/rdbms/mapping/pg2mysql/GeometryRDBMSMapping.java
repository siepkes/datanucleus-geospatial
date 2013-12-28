/**********************************************************************
 Copyright (c) 2006 Thomas Marti, Stefan Schmid and others. All rights reserved.
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Contributors:
 ...
 **********************************************************************/
package org.datanucleus.store.rdbms.mapping.pg2mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.datanucleus.store.rdbms.mapping.java.JavaTypeMapping;
import org.datanucleus.store.rdbms.mapping.mysql.MySQLSpatialRDBMSMapping;
import org.datanucleus.store.rdbms.schema.MySQLSpatialTypeInfo;
import org.datanucleus.store.rdbms.schema.SQLTypeInfo;
import org.datanucleus.store.rdbms.table.Column;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.postgis.Geometry;
import org.postgis.binary.BinaryParser;
import org.postgis.binary.BinaryWriter;
import org.postgis.binary.ValueSetter;

/**
 * Mapping for a PostGIS-JDBC Geometry object to MySQL.
 */
public class GeometryRDBMSMapping extends MySQLSpatialRDBMSMapping
{
    protected final BinaryWriter writer = new BinaryWriter();
    protected final BinaryParser parser = new BinaryParser();

    private static final SQLTypeInfo typeInfo;
    static
    {
        typeInfo = (SQLTypeInfo)MySQLSpatialTypeInfo.TYPEINFO_PROTOTYPE.clone();
        typeInfo.setTypeName("GEOMETRY");
        typeInfo.setLocalTypeName("GEOMETRY");
    }

    public GeometryRDBMSMapping(JavaTypeMapping mapping, RDBMSStoreManager storeMgr, Column col)
    {
        super(mapping, storeMgr, col);
    }

    public SQLTypeInfo getTypeInfo()
    {
        return typeInfo;
    }

    public Object getObject(ResultSet rs, int exprIndex)
    {
        Object value;

        try
        {
            byte[] mysqlBinary = ((ResultSet)rs).getBytes(exprIndex);
            if (((ResultSet)rs).wasNull() || mysqlBinary == null)
            {
                value = null;
            }
            else
            {
                value = parser.parse(mysqlBinaryToWkb(mysqlBinary));
                ((Geometry)value).setSrid(mysqlBinaryToSrid(mysqlBinary));
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("getObject", exprIndex, e), e);
        }

        return value;
    }

    public void setObject(PreparedStatement ps, int exprIndex, Object value)
    {
        try
        {
            if (value == null)
            {
                ((PreparedStatement) ps).setNull(exprIndex, getTypeInfo().getDataType(), getTypeInfo().getTypeName());
            }
            else
            {
                Geometry geom = (Geometry)value;
                
                int srid = geom.getSrid();
                if (srid != -1)
                {
                    geom.srid = -1; // hack: BinaryWriter must not encode srid into binary
                }
                byte[] wkb = writer.writeBinary(geom, ValueSetter.NDR.NUMBER);
                if (srid != -1)
                {
                    geom.srid = srid; // revert hack
                }
                ((PreparedStatement)ps).setBytes(exprIndex, wkbToMysqlBinary(wkb, srid));
            }
        }
        catch (SQLException e)
        {
            throw new NucleusDataStoreException(failureMessage("setObject", value, e), e);
        }
    }

}