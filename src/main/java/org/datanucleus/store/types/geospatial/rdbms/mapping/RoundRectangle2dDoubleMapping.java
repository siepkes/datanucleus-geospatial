/**********************************************************************
Copyright (c) 2007 Thomas Marti and others. All rights reserved.
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
package org.datanucleus.store.types.geospatial.rdbms.mapping;

import java.awt.geom.RoundRectangle2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.ClassNameConstants;
import org.datanucleus.ExecutionContext;
import org.datanucleus.NucleusContext;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.rdbms.RDBMSStoreManager;
import org.datanucleus.store.rdbms.mapping.java.SingleFieldMultiMapping;
import org.datanucleus.store.rdbms.table.Table;

/**
 * Mapping for java.awt.geom.RoundRectangle2D.Double, maps the x, y, width, height, arc-width and arc-height
 * values to double-precision columns.
 */
public class RoundRectangle2dDoubleMapping extends SingleFieldMultiMapping
{
    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(AbstractMemberMetaData,
     * DatastoreContainerObject, ClassLoaderResolver)
     */
    public void initialize(AbstractMemberMetaData fmd, Table table, ClassLoaderResolver clr)
    {
        super.initialize(fmd, table, clr);
        addColumns();
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.rdbms.mapping.JavaTypeMapping#initialize(RDBMSStoreManager,
     * java.lang.String)
     */
    public void initialize(RDBMSStoreManager storeMgr, String type)
    {
        super.initialize(storeMgr, type);
        addColumns();
    }

    protected void addColumns()
    {
        addColumns(ClassNameConstants.DOUBLE); // X
        addColumns(ClassNameConstants.DOUBLE); // Y
        addColumns(ClassNameConstants.DOUBLE); // Width
        addColumns(ClassNameConstants.DOUBLE); // Height
        addColumns(ClassNameConstants.DOUBLE); // Arc-Width
        addColumns(ClassNameConstants.DOUBLE); // Arc-Height
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getJavaType()
     */
    public Class getJavaType()
    {
        return RoundRectangle2D.Double.class;
    }

    /**
     * Method to return the value to be stored in the specified datastore index given the overall value for
     * this java type.
     * @param index The datastore index
     * @param value The overall value for this java type
     * @return The value for this datastore index
     */
    public Object getValueForColumnMapping(NucleusContext nucleusCtx, int index, Object value)
    {
        RoundRectangle2D.Double rr = (RoundRectangle2D.Double) value;
        if (index == 0)
        {
            return rr.getX();
        }
        else if (index == 1)
        {
            return rr.getY();
        }
        else if (index == 2)
        {
            return rr.getWidth();
        }
        else if (index == 3)
        {
            return rr.getHeight();
        }
        else if (index == 4)
        {
            return rr.getArcWidth();
        }
        else if (index == 5)
        {
            return rr.getArcHeight();
        }
        throw new IndexOutOfBoundsException();
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#setObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[], java.lang.Object)
     */
    public void setObject(ExecutionContext ec, PreparedStatement ps, int[] exprIndex, Object value)
    {
        RoundRectangle2D roundRectangle = (RoundRectangle2D) value;
        if (roundRectangle == null)
        {
            for (int i = 0; i < exprIndex.length; i++)
            {
                getColumnMapping(i).setObject(ps, exprIndex[i], null);
            }
        }
        else
        {
            getColumnMapping(0).setDouble(ps, exprIndex[0], roundRectangle.getX());
            getColumnMapping(1).setDouble(ps, exprIndex[1], roundRectangle.getY());
            getColumnMapping(2).setDouble(ps, exprIndex[2], roundRectangle.getWidth());
            getColumnMapping(3).setDouble(ps, exprIndex[3], roundRectangle.getHeight());
            getColumnMapping(4).setDouble(ps, exprIndex[4], roundRectangle.getArcWidth());
            getColumnMapping(5).setDouble(ps, exprIndex[5], roundRectangle.getArcHeight());
        }
    }

    /*
     * (non-Javadoc)
     * @see org.datanucleus.store.mapping.JavaTypeMapping#getObject(org.datanucleus.ExecutionContext,
     * java.lang.Object, int[])
     */
    public Object getObject(ExecutionContext ec, ResultSet resultSet, int[] exprIndex)
    {
        // Check for null entries
        if (getColumnMapping(0).getObject(resultSet, exprIndex[0]) == null)
        {
            return null;
        }

        double x = getColumnMapping(0).getDouble(resultSet, exprIndex[0]);
        double y = getColumnMapping(1).getDouble(resultSet, exprIndex[1]);
        double width = getColumnMapping(2).getDouble(resultSet, exprIndex[2]);
        double height = getColumnMapping(3).getDouble(resultSet, exprIndex[3]);
        double arcwidth = getColumnMapping(4).getDouble(resultSet, exprIndex[4]);
        double archeight = getColumnMapping(5).getDouble(resultSet, exprIndex[5]);
        return new RoundRectangle2D.Double(x, y, width, height, arcwidth, archeight);
    }
}