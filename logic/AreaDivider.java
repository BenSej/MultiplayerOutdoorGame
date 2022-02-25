package edu.illinois.cs.cs125.spring2020.mp.logic;

//import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * .
 */
public class AreaDivider {
    /**
     * .
     */
    private double north;
    /**
     * .
     */
    private double east;
    /**
     * .
     */
    private double south;
    /**
     * .
     */
    private double west;
    /**
     * .
     */
    private int cellSize;
    /**
     * .
     */
    private LatLngBounds boundary;

    /**
     * @param setNorth north
     * @param setEast east
     * @param setSouth south
     * @param setWest west
     * @param setCellSize cell size
     */
    public AreaDivider(final double setNorth, final double setEast, final double setSouth,
                       final double setWest, final int setCellSize) {
        north = setNorth;
        east = setEast;
        south = setSouth;
        west = setWest;
        cellSize = setCellSize;
    }
    /**
     * @return return
     */
    public final int getXCells() {
        double numCells = LatLngUtils.distance(south, west, south, east) / cellSize;
        return (int) Math.ceil(numCells);
    }
    /**
     * @return return
     */
    public final int getYCells() {
        double numCells = LatLngUtils.distance(south, east, north, east) / cellSize;
        return (int) Math.ceil(numCells);
    }
    /**
     * @return return
     */
    public final boolean isValid() {
        return cellSize > 0 && east > west && north > south;
    }

    /**
     * @param location asdfadsf
     * @return asdf
     */
    public final int getXIndex(final com.google.android.gms.maps.model.LatLng location) {
        double latitude = location.latitude;
        double longitude = location.longitude;
        double distance = LatLngUtils.distance(latitude, longitude,
                latitude, west);
        double newCellSize = LatLngUtils.distance(south, west, south, east) / getXCells();
        if (longitude <= east && longitude >= west && latitude >= south && latitude <= north) {
            double index = distance / newCellSize;
            return (int) index;
        }
        return -1;
    }

    /**
     * @param location asdf
     * @return asdf
     */
    public final int getYIndex(final com.google.android.gms.maps.model.LatLng location) {
        double longitude = location.longitude;
        double latitude = location.latitude;
        double distance = LatLngUtils.distance(latitude, longitude,
                south, longitude);
        double newCellSize = LatLngUtils.distance(south, west, north, west) / getYCells();
        if (longitude <= east && longitude >= west && latitude >= south && latitude <= north) {
            double index = distance / newCellSize;
            return (int) index;
        }
        return -1;
    }

    /**
     * @param x asdf
     * @param y asdf
     * @return asdf
     */
    public final com.google.android.gms.maps.model.LatLngBounds
        getCellBounds(final int x, final int y) {
        double newYCellSize = (north - south) / getYCells();
        double newXCellSize = (east - west) / getXCells();
        LatLng southWest = new LatLng(south + newYCellSize * y, west + newXCellSize * x);
        LatLng northEast = new LatLng(south + newYCellSize * y + newYCellSize,
                west + newXCellSize * x + newXCellSize);
        boundary = new LatLngBounds(southWest, northEast);
        return boundary;
    }

    /**
     * @param map asdf
     */
    public void renderGrid(final com.google.android.gms.maps.GoogleMap map) {
        LatLng leftStart = new LatLng(south, west);
        LatLng leftEnd = new LatLng(north, west);
        PolylineOptions left = new PolylineOptions().add(leftStart, leftEnd);

        LatLng rightStart = new LatLng(south, east);
        LatLng rightEnd = new LatLng(north, east);
        PolylineOptions right = new PolylineOptions().add(rightStart, rightEnd);

        LatLng botStart = new LatLng(south, west);
        LatLng botEnd = new LatLng(south, east);
        PolylineOptions bot = new PolylineOptions().add(botStart, botEnd);

        LatLng topStart = new LatLng(north, west);
        LatLng topEnd = new LatLng(north, east);
        PolylineOptions top = new PolylineOptions().add(topStart, topEnd);

        map.addPolyline(left);
        map.addPolyline(right);
        map.addPolyline(top);
        map.addPolyline(bot);

        for (int i = 1; i <= getXCells() - 1; i++) {
            LatLng start = new LatLng(north, west + i * ((east - west) / getXCells()));
            LatLng end = new LatLng(south, west + i * ((east - west) / getXCells()));
            PolylineOptions line = new PolylineOptions().add(start, end);
            map.addPolyline(line);
        }

        for (int j = 1; j <= getYCells() - 1; j++) {
            LatLng start = new LatLng(south + j * ((north - south) / getYCells()), west);
            LatLng end = new LatLng(south + j * ((north - south) / getYCells()), east);
            PolylineOptions line = new PolylineOptions().add(start, end);
            map.addPolyline(line);
        }
    }
}
