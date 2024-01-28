package sw.planet.api.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.apache.commons.lang3.builder.EqualsBuilder;

@Entity
@Table(name = "planets")
public class Planet {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;
    @Column(nullable = false)
    @NotEmpty
    private String climate;
    @Column(nullable = false)
    @NotEmpty
    private String terrain;

    public Planet(String name, String climate, String terrain) {
        this.name = name;
        this.climate = climate;
        this.terrain = terrain;
    }

    public Planet(String terrain, String climate) {
        this.terrain = terrain;
        this.climate = climate;
    }

    public Planet() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClimate() {
        return climate;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public String getTerrain() {
        return terrain;
    }

    public void setTerrain(String terrain) {
        this.terrain = terrain;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(obj, this);
    }
}
