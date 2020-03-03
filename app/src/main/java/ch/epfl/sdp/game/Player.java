package ch.epfl.sdp.game;

import androidx.annotation.NonNull;

public class Player {
    private int id;
    private Faction faction;
    private Location lastKnownLocation;

    public Player(int id, Faction faction) {
        this.id = id;
        this.faction = faction;
        lastKnownLocation = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(Location lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    @NonNull
    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", faction=" + faction +
                ", lastKnownLocation=" + lastKnownLocation +
                '}';
    }
}
