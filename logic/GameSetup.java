package edu.illinois.cs.cs125.spring2020.mp.logic;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/** .*/
public class GameSetup {

    /** .
     */
    public GameSetup() { }

    /**
     * @param invitees asdf
     * @param area  asdf
     * @param cellSize asdf
     * @return asdf
     * asdf
     */
    public static com.google.gson.JsonObject areaMode(
            final java.util.List<Invitee> invitees,
             final com.google.android.gms.maps.model.LatLngBounds area,
             final int cellSize) {
        if (invitees.size() == 0 || cellSize <= 0) {
            return null;
        }
        double north = area.northeast.latitude;
        double east = area.northeast.longitude;
        double south = area.southwest.latitude;
        double west = area.southwest.longitude;
        JsonObject toReturn = new JsonObject();
        JsonArray inv = new JsonArray();
        for (Invitee invs : invitees) {
            JsonObject x = new JsonObject();
            x.addProperty("email", invs.getEmail());
            x.addProperty("team", invs.getTeamId());
            inv.add(x);
        }
        toReturn.add("invitees", inv);
        toReturn.addProperty("mode", "area");
        toReturn.addProperty("cellSize", cellSize);
        toReturn.addProperty("areaNorth", north);
        toReturn.addProperty("areaEast", east);
        toReturn.addProperty("areaSouth", south);
        toReturn.addProperty("areaWest", west);
        return toReturn;
    }
    /**
     * @param invitees asdf
     * @param proximityThreshold  asdf
     * @param targets asdf
     * @return asdf
     * asdf
     */
    public static com.google.gson.JsonObject targetMode(
            final java.util.List<Invitee> invitees,
            final java.util.List<com.google.android.gms.maps.model.LatLng> targets,
            final int proximityThreshold) {
        if (invitees.size() == 0 || targets.size() == 0 || proximityThreshold <= 0) {
            return null;
        }
        JsonObject toReturn = new JsonObject();
        JsonArray invs = new JsonArray();
        JsonArray targs = new JsonArray();
        for (Invitee inv : invitees) {
            JsonObject x = new JsonObject();
            x.addProperty("email", inv.getEmail());
            x.addProperty("team", inv.getTeamId());
            invs.add(x);
        }
        for (com.google.android.gms.maps.model.LatLng latLng : targets) {
            JsonObject x = new JsonObject();
            x.addProperty("latitude", latLng.latitude);
            x.addProperty("longitude", latLng.longitude);
            targs.add(x);
        }
        toReturn.addProperty("mode", "target");
        toReturn.add("invitees", invs);
        toReturn.addProperty("proximityThreshold", proximityThreshold);
        toReturn.add("targets", targs);
        return toReturn;
    }
}
