/**********************************************************************
Copyright (c) 2009 Andy Jefferson and others. All rights reserved.
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
package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of "Spatial.within(expr, expr2)" or "expr.within(expr2)" method for Oracle.
 */
public class SpatialWithinMethod2 extends AbstractSQLMethod
{
    /*
     * (non-Javadoc)
     * @see
     * org.datanucleus.store.rdbms.sql.method.SQLMethod#getExpression(org.datanucleus.store.rdbms.sql.expression
     * .SQLExpression, java.util.List)
     */
    public SQLExpression getExpression(SQLExpression expr, List args)
    {
        if (args == null)
        {
            throw new NucleusUserException("Cannot invoke Spatial.within without arguments");
        }
        if (expr == null && args.size() != 2)
        {
            throw new NucleusUserException("Cannot invoke Spatial.within without 2 arguments");
        }
        else if (expr != null && args.size() != 1)
        {
            throw new NucleusUserException("Cannot invoke geom.within() without 1 argument");
        }

        SQLExpression argExpr1 = expr;
        SQLExpression argExpr2 = expr;
        if (expr == null)
        {
            // "Spatial." method
            argExpr1 = (SQLExpression) args.get(0); // Geometry 1
            argExpr2 = (SQLExpression) args.get(1); // Geometry 2
        }
        else
        {
            argExpr1 = expr; // Geometry 1
            argExpr2 = (SQLExpression) args.get(0); // Geometry 2
        }

        ArrayList geomFunc1Args = new ArrayList();
        geomFunc1Args.add(argExpr1);
        GeometryExpression geomExpr1 = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFunc1Args, null);

        ArrayList geomFunc2Args = new ArrayList();
        geomFunc2Args.add(argExpr2);
        GeometryExpression geomExpr2 = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFunc2Args, null);

        ArrayList funcArgs = new ArrayList();
        funcArgs.add(geomExpr1);
        funcArgs.add(geomExpr2);

        return SpatialMethodHelper.getBooleanExpression(stmt, "within", funcArgs, exprFactory);
    }
}