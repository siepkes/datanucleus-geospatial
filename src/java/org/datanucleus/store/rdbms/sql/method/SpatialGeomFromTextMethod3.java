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
   2013 barisergun75@gmail.com - NUCSPATIAL-28 Adding postgis support
 **********************************************************************/
package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;

import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of Spatial "geomFromText" method.
 */
public class SpatialGeomFromTextMethod3 extends AbstractSQLMethod
{
    public SQLExpression getExpression(SQLExpression expr, List args)
    {
        if (args == null || args.size() < 1 || args.size() > 2)
        {
            throw new NucleusUserException(
                    "Cannot invoke Spatial.geomFromText without 1 or 2 arguments. Spatial.geomFromText(String textualrep, int srid) " + "or Spatial.geomFromText(String textualrep) where textualrep contains srid information as well");
        }

        SQLExpression wktExpr = (SQLExpression) args.get(0);

        ArrayList<SQLExpression> funcArgs = new ArrayList<SQLExpression>();
        funcArgs.add(wktExpr);
        if (args.size() == 2)
        {
            SQLExpression sridExpr = (SQLExpression) args.get(1);
            funcArgs.add(sridExpr);
        }

        return new GeometryExpression(stmt, null, "st_geometryfromtext", funcArgs, null);
    }
}
