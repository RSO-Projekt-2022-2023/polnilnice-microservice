package si.fri.rso.polnilnice.lib;

import java.time.Instant;

public class Polnilnice {

    private Integer polnilniceId;
    private Float coord_north;
    private Float coord_east;
    private Integer chargers;
    private Integer available;
    private Instant created;

    public Float getCoord_north() {
        return coord_north;
    }

    public void setCoord_north(Float coord_north) {
        this.coord_north = coord_north;
    }

    public Float getCoord_east() {
        return coord_east;
    }

    public void setCoord_east(Float coord_east) {
        this.coord_east = coord_east;
    }

    public Integer getChargers() {
        return chargers;
    }

    public void setChargers(Integer chargers) {
        this.chargers = chargers;
    }

    public Integer getAvailable() {
        return available;
    }

    public void setAvailable(Integer available) {
        this.available = available;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }

    public Integer getPolnilniceId() {
        return polnilniceId;
    }

    public void setPolnilniceId(Integer polnilniceId) {
        this.polnilniceId = polnilniceId;
    }


}
