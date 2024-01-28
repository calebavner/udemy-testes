package sw.planet.api.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import sw.planet.api.domain.Planet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static sw.planet.api.common.PlanetConstants.PLANET;

@DataJpaTest
class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository repo;
    @Autowired
    private TestEntityManager testManager;

    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {

        Planet p = repo.save(PLANET);
        Planet sut = testManager.find(Planet.class, p.getId());

        assertThat(sut).isNotNull();
        assertThat(sut.getName()).isEqualTo(p.getName());
        assertThat(sut.getClimate()).isEqualTo(p.getClimate());
        assertThat(sut.getTerrain()).isEqualTo(p.getTerrain());
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {

        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        assertThatThrownBy(() -> repo.save(emptyPlanet)).isInstanceOf(RuntimeException.class);
        assertThatThrownBy(() -> repo.save(invalidPlanet)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void createPlanet_WithExistingName_ThrowsException() {
        Planet p = testManager.persistFlushFind(PLANET);
        testManager.detach(p);
        p.setId(null);


        assertThatThrownBy(() -> repo.save(p)).isInstanceOf(RuntimeException.class);
    }
}