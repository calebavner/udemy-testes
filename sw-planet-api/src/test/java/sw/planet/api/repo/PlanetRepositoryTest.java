package sw.planet.api.repo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Example;
import org.springframework.test.context.jdbc.Sql;
import sw.planet.api.domain.Planet;
import sw.planet.api.domain.QueryBuilder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sw.planet.api.common.PlanetConstants.PLANET;
import static sw.planet.api.common.PlanetConstants.TATOOINE;

@DataJpaTest
class PlanetRepositoryTest {
    @Autowired
    private PlanetRepository repo;
    @Autowired
    private TestEntityManager testManager;

    @AfterEach
    public void afterEach() {
        PLANET.setId(null);
    }

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

    @Test
    public void getPlanet_WithExistingdId_ReturnPlanet() {

        Planet p = testManager.persistFlushFind(PLANET);
        Optional<Planet> planetOptional = repo.findById(p.getId());

        assertThat(planetOptional).isNotEmpty();
        assertThat(planetOptional.get()).isEqualTo(p);
    }

    @Test
    public void getPlanet_WithUnexistingdId_ReturnNotFound() {

        Optional<Planet> planetOptional = repo.findById(1L);
        assertThat(planetOptional).isEmpty();
    }

    @Test void getPlanet_WithValidName_ReturnsPlanet() {

        Planet p = testManager.persistFlushFind(PLANET);
        Optional<Planet> planetOptional = repo.findByName(p.getName());

        assertThat(planetOptional).isNotEmpty();
        assertThat(planetOptional.get()).isEqualTo(p);
    }

    @Test void getPlanet_WithInvalidName_ReturnsNotFound() {

        Optional<Planet> planetOptional = repo.findByName("name");
        assertThat(planetOptional).isEmpty();
    }

    @Sql(scripts = "/import_planets.sql")
    @Test
    public void listPlanet_ReturnsFilteredPlanets() {

        Example<Planet> queryWithoutFilters = QueryBuilder.makeQuery(new Planet());
        Example<Planet> queryWithFilters = QueryBuilder.makeQuery(new Planet(TATOOINE.getTerrain(), TATOOINE.getClimate()));

        List<Planet> responseWithoutFilters = repo.findAll(queryWithoutFilters);
        List<Planet> responseWithFilters = repo.findAll(queryWithFilters);

        assertThat(responseWithoutFilters).isNotEmpty();
        assertThat(responseWithoutFilters).hasSize(3);
        assertThat(responseWithFilters).isNotEmpty();
        assertThat(responseWithFilters).hasSize(1);
        assertThat(responseWithFilters.get(0)).isEqualTo(TATOOINE);
    }

    @Test
    public void listPlanet_ReturnsNoPlanets() {

        Example<Planet> query = QueryBuilder.makeQuery(new Planet());
        List<Planet> response = repo.findAll(query);

        assertThat(response).isEmpty();
    }
}