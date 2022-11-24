package si.fri.rso.polnilnice.models.entities;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "polnilnice")
@NamedQueries(value =
        {
                @NamedQuery(name = "PolnilniceEntity.getAll",
                        query = "SELECT im FROM PolnilniceEntity im")
        })
public class PolnilniceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "coord_north")
    private Float coord_north;

    @Column(name = "coord_east")
    private Float coord_east;

    @Column(name = "chargers")
    private Integer chargers;

    @Column(name = "available")
    private Integer available;

    @Column(name = "created")
    private Instant created;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

}