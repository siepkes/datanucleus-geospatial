package com.esri.arcgis.geometry;

import java.io.IOException;

import com.esri.arcgis.system.IClone;
import com.linar.jintegra.AutomationException;

public class Multipoint implements IGeometry, IClone
{
    private static final long serialVersionUID = 1L;
    
    public Multipoint() {
        // constructor stub
    }
    
    public Multipoint(Object obj) {
        // constructor stub
    }
    
    public void setSpatialReferenceByRef(ISpatialReference spatialRef)
    {
        // method stub        
    }
    public void addGeometry(Point point, Object object, Object object2)
    {
        // method stub       
    }
    public int getGeometryType() throws IOException, AutomationException
    {
        // method stub
        return 0;
    }

    public ISpatialReference getSpatialReference()
    {
        // method stub
        return null;
    }
    
    public boolean isEqual(IClone clone) {
        return true;
    }
}