package edu.illinois.cs.cs125.spring2020.mp.logic;

//import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import com.google.gson.JsonArray;

/**.
 * the
 */
public class GameSummary {
    /**
     *
     */
    private String gameID;
    /**
     *
     */
    private String owner;
    /**
     *
     */
    private int state;
    /**
     *
     */
    private String mode;
    /**
     *
     */
    private JsonArray players;
    /**
     * @param infoFromServer the
     */
    public GameSummary(final com.google.gson.JsonObject infoFromServer) {
        gameID = infoFromServer.get("id").getAsString();
        owner = infoFromServer.get("owner").getAsString();
        state = infoFromServer.get("state").getAsInt();
        mode = infoFromServer.get("mode").getAsString();
        players = infoFromServer.get("players").getAsJsonArray();
    }
    /**
     * The.
     * @return the.
     */
    public final java.lang.String getId() {
        return gameID;
    }
    /**
     * The.
     * @return the.
     */
    public final java.lang.String getMode() {
        return mode;
    }
    /**
     * The.
     * @return the.
     */
    public final java.lang.String getOwner() {
        return owner;
    }
    /**
     * The.
     * @param userEmail the.
     * @param context the.
     * @return the.
     */
    public java.lang.String getPlayerRole(final java.lang.String userEmail,
                                          final android.content.Context context) {
        for (int i = 0; i <= players.size() - 1; i++) {
            JsonObject attributes = players.get(i).getAsJsonObject();
            if (attributes.get("email").getAsString().equals(userEmail)) {
                int teamID = attributes.get("team").getAsInt();
                if (teamID == TeamID.OBSERVER) {
                    return "Observer";
                } else if (teamID == TeamID.TEAM_YELLOW) {
                    return "Yellow";
                } else if (teamID == TeamID.TEAM_GREEN) {
                    return "Green";
                } else if (teamID == TeamID.TEAM_BLUE) {
                    return "Blue";
                } else if (teamID == TeamID.TEAM_RED) {
                    return "Red";
                }
            }
        }
        return "";
    }
    /**
     * The.
     * @param userEmail the.
     * @return the.
     */
    public final boolean isInvitation(final java.lang.String userEmail) {
        if (state == GameStateID.ENDED) {
            return false;
        }
        for (int i = 0; i <= players.size() - 1; i++) {
            JsonObject attributes = players.get(i).getAsJsonObject();
            int playerState = attributes.get("state").getAsInt();
            if (attributes.get("email").getAsString().equals(userEmail) && playerState == PlayerStateID.INVITED) {
                return true;
            }
        }
        return false;
    }
    /**
     * The.
     * @param userEmail the.
     * @return the.
     */
    public final boolean isOngoing(final java.lang.String userEmail) {
        if (state == GameStateID.ENDED) {
            return false;
        }
        for (int i = 0; i <= players.size() - 1; i++) {
            JsonObject attributes = players.get(i).getAsJsonObject();
            int playerState = attributes.get("state").getAsInt();
            if (attributes.get("email").getAsString().equals(userEmail)) {
                if (playerState == PlayerStateID.ACCEPTED || playerState == PlayerStateID.PLAYING) {
                    return true;
                }
            }
        }
        return false;
    }
}
