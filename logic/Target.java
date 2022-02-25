package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//import edu.illinois.cs.cs125.spring2020.mp.GameActivity;

/** asdf.
 */
public class Target {
    /** the. */
    private int team;
    /** the. */
    private com.google.android.gms.maps.GoogleMap map;
    /** the. */
    private com.google.android.gms.maps.model.LatLng position;
    /** the. */
    private BitmapDescriptor icon;
    /** the. */
    private MarkerOptions options;
    /** the. */
    private Marker marker;

    /**
     * @param setMap asdf
     * @param setPosition  asdf
     * @param setTeam asdf
     * asdf
     */
    public Target(final com.google.android.gms.maps.GoogleMap setMap,
           final com.google.android.gms.maps.model.LatLng setPosition, final int setTeam) {
        this.team = setTeam;
        this.map = setMap;
        this.position = setPosition;
        double latitude = setPosition.latitude;
        double longitude = setPosition.longitude;
        this.options = new MarkerOptions().position(setPosition);
        this.marker = setMap.addMarker(options);
        if (setTeam == TeamID.TEAM_BLUE) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            this.marker.setIcon(icon);
        } else if (setTeam == TeamID.TEAM_GREEN) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            this.marker.setIcon(icon);
        } else if (setTeam == TeamID.TEAM_RED) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            this.marker.setIcon(icon);
        } else if (setTeam == TeamID.TEAM_YELLOW) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            this.marker.setIcon(icon);
        } else {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET);
            this.marker.setIcon(icon);
        }
    }

    /**
     * @return asdf
     * asdf
     */
    public final com.google.android.gms.maps.model.LatLng getPosition() {
        return this.position;
    }

    /**
     * @return asdf
     * asdf
     */
    public final int getTeam() {
        return this.team;
    }

    /**
     * @param newTeam asdf
     * asdf
     */
    public final void setTeam(final int newTeam) {
        this.team = newTeam;
        if (newTeam == TeamID.TEAM_BLUE) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
            this.marker.setIcon(icon);
        } else if (newTeam == TeamID.TEAM_GREEN) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
            this.marker.setIcon(icon);
        } else if (newTeam == TeamID.TEAM_RED) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
            this.marker.setIcon(icon);
        } else if (newTeam == TeamID.TEAM_YELLOW) {
            this.icon =
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
            this.marker.setIcon(icon);
        }
    }
}
