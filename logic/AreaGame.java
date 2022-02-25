package edu.illinois.cs.cs125.spring2020.mp.logic;

import android.content.Context;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.neovisionaries.ws.client.WebSocket;

import edu.illinois.cs.cs125.spring2020.mp.R;

/**
 * Represents an area mode game. Keeps track of cells and the player's most recent capture.
 * <p>
 * All these functions are stubs that you need to implement.
 * Feel free to add any private helper functions that would be useful.
 * See {@link TargetGame} for an example of how multiplayer games are handled.
 */
public final class AreaGame extends Game {

    // You will probably want some instance variables to keep track of the game state

    /**
     * the.
     */
    private JsonArray players;
    /**
     * the.
     */
    private JsonArray cells;
    /**
     * the.
     */
    private String owner;
    /**
     * the.
     */
    private int state;
    /**
     * the.
     */
    private String mode;
    /**
     * the.
     */
    private double areaNorth;
    /**
     * the.
     */
    private double areaEast;
    /**
     * the.
     */
    private double areaSouth;
    /**
     * the.
     */
    private double areaWest;
    /**
     * the.
     */
    private int cellSize;
    /**
     * the.
     */
    private AreaDivider areaDivider;
    /**
     * the.
     */
    private GoogleMap myMap;
    /**
     * the.
     */
    private int[][] visited;
    /**
     * the.
     */
    private int prevX = -1;
    /**
     * the.
     */
    private int prevY = -1;
    /**
     * the.
     */
    private boolean firstCapture = true;

    // (similar to the area mode gameplay logic you previously wrote in GameActivity)
    /**
     * Creates a game in area mode.
     * <p>
     * Loads the current game state from JSON into instance variables and populates the map
     * to show existing cell captures.
     * @param email the user's email
     * @param map the Google Maps control to render to
     * @param webSocket the websocket to send updates to
     * @param fullState the "full" update from the server
     * @param context the Android UI context
     */
    public AreaGame(final String email, final GoogleMap map, final WebSocket webSocket,
                    final JsonObject fullState, final Context context) {
        super(email, map, webSocket, fullState, context);
        players = fullState.get("players").getAsJsonArray();
        cells = fullState.get("cells").getAsJsonArray();
        owner = fullState.get("owner").getAsString();
        state = fullState.get("state").getAsInt();
        mode = fullState.get("mode").getAsString();
        areaNorth = fullState.get("areaNorth").getAsDouble();
        areaEast = fullState.get("areaEast").getAsDouble();
        areaSouth = fullState.get("areaSouth").getAsDouble();
        areaWest = fullState.get("areaWest").getAsDouble();
        cellSize = fullState.get("cellSize").getAsInt();
        areaDivider = new AreaDivider(areaNorth, areaEast, areaSouth, areaWest, cellSize);
        visited = new int[areaDivider.getXCells()][areaDivider.getYCells()];
        areaDivider.renderGrid(map);
        myMap = map;
        for (int i = 0; i < cells.size(); i++) {
            int xCoord = cells.get(i).getAsJsonObject().get("x").getAsInt();
            int yCoord = cells.get(i).getAsJsonObject().get("y").getAsInt();
            visited[xCoord][yCoord] = cells.get(i).getAsJsonObject().get("team").getAsInt();
            if (cells.get(i).getAsJsonObject().get("team").getAsInt() != 0) {
                firstCapture = false;
                LatLng northEast = areaDivider.getCellBounds(xCoord, yCoord).northeast;
                LatLng southWest = areaDivider.getCellBounds(xCoord, yCoord).southwest;
                LatLng northWest = new LatLng(areaDivider.getCellBounds(xCoord, yCoord).northeast.latitude,
                        areaDivider.getCellBounds(xCoord, yCoord).southwest.longitude);
                LatLng southEast = new LatLng(areaDivider.getCellBounds(xCoord, yCoord).southwest.latitude,
                        areaDivider.getCellBounds(xCoord, yCoord).northeast.longitude);
                PolygonOptions options = new PolygonOptions();
                options.add(northWest, northEast, southEast, southWest);
                int[] colors = getContext().getResources().getIntArray(R.array.team_colors);
                options.fillColor(colors[cells.get(i).getAsJsonObject().get("team").getAsInt()]);
                map.addPolygon(options);
            }
        }
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getAsJsonObject().get("email").getAsString().equals(getEmail())) {
                JsonArray array = players.get(i).getAsJsonObject().get("path").getAsJsonArray();
                if (array.size() > 0) {
                    JsonObject object = array.get(array.size() - 1).getAsJsonObject();
                    prevX = object.get("x").getAsInt();
                    prevY = object.get("y").getAsInt();
                    visited[prevX][prevY] = players.get(i).getAsJsonObject().get("team").getAsInt();
                    firstCapture = false;
                }
            }
        }
    }

    /**
     * Called when the user's location changes.
     * <p>
     * Area mode games detect whether the player is in an uncaptured cell. Capture is possible if
     * the player has no captures yet or if the cell shares a side with the previous cell captured by
     * the player. If capture occurs, a polygon with the team color is added to the cell on the map
     * and a cellCapture update is sent to the server.
     * @param location the player's most recently known location
     */
    @Override
    public void locationUpdated(final LatLng location) {
        super.locationUpdated(location);
        int xCoord = areaDivider.getXIndex(location);
        int yCoord = areaDivider.getYIndex(location);
        int difX = Math.abs(xCoord - prevX);
        int difY = Math.abs(yCoord - prevY);
        if (xCoord >= 0 && yCoord >= 0 && xCoord < areaDivider.getXCells() && yCoord < areaDivider.getYCells()) {
            if (visited[xCoord][yCoord] == 0 && (difX + difY <= 1 || prevX == -1)) {
                LatLng northEast = areaDivider.getCellBounds(xCoord, yCoord).northeast;
                LatLng southWest = areaDivider.getCellBounds(xCoord, yCoord).southwest;
                LatLng northWest = new LatLng(areaDivider.getCellBounds(xCoord, yCoord).northeast.latitude,
                        areaDivider.getCellBounds(xCoord, yCoord).southwest.longitude);
                LatLng southEast = new LatLng(areaDivider.getCellBounds(xCoord, yCoord).southwest.latitude,
                        areaDivider.getCellBounds(xCoord, yCoord).northeast.longitude);
                PolygonOptions options = new PolygonOptions();
                options.add(northWest, northEast, southEast, southWest);
                int[] colors = getContext().getResources().getIntArray(R.array.team_colors);
                options.fillColor(colors[getMyTeam()]);
                getMap().addPolygon(options);
                JsonObject message = new JsonObject();
                message.addProperty("type", "cellCapture");
                message.addProperty("x", xCoord);
                message.addProperty("y", yCoord);
                sendMessage(message);
                firstCapture = false;
                visited[xCoord][yCoord] = getMyTeam();
                prevX = xCoord;
                prevY = yCoord;
            }
        }
        return;
    }
    /**
     * Processes an update from the server.
     * <p>
     * Since playerCellCapture events are specific to area mode games, this function handles those
     * by placing a polygon of the capturing player's team color on the newly captured cell and
     * recording the cell's new owning team.
     * All other message types are delegated to the superclass.
     * @param message JSON from the server (the "type" property indicates the update type)
     * @return whether the message type was recognized
     */
    @Override
    public boolean handleMessage(final JsonObject message) {
        String type = message.get("type").getAsString();
        if (type.equals("playerCellCapture")) {
            int xCoord = message.get("x").getAsInt();
            int yCoord = message.get("y").getAsInt();
            LatLng northEast = areaDivider.getCellBounds(xCoord, yCoord).northeast;
            LatLng southWest = areaDivider.getCellBounds(xCoord, yCoord).southwest;
            LatLng northWest = new LatLng(areaDivider.getCellBounds(xCoord, yCoord).northeast.latitude,
                    areaDivider.getCellBounds(xCoord, yCoord).southwest.longitude);
            LatLng southEast = new LatLng(areaDivider.getCellBounds(xCoord, yCoord).southwest.latitude,
                    areaDivider.getCellBounds(xCoord, yCoord).northeast.longitude);
            PolygonOptions options = new PolygonOptions();
            int[] colors = getContext().getResources().getIntArray(R.array.team_colors);
            int teamId = message.get("team").getAsInt();
            options.add(northWest, northEast, southEast, southWest);
            options.fillColor(colors[teamId]);
            myMap.addPolygon(options);
            visited[xCoord][yCoord] = teamId;
            return true;
        } else if (type.equals("playerExit") || type.equals("playerLocation")) {
            super.handleMessage(message);
            return true;
        } else {
            return false;
        }
    }
    /**
     * Gets a team's score in this area mode game.
     * @param teamId the team ID
     * @return the number of cells owned by the team
     */
    @Override
    public int getTeamScore(final int teamId) {
        int count = 0;
        for (int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                if (visited[i][j] == teamId) {
                    count++;
                }
            }
        }
        return count;
    }
}
