package sw.planet.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import sw.planet.api.domain.Planet;
import sw.planet.api.domain.QueryBuilder;
import sw.planet.api.repo.PlanetRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static sw.planet.api.common.PlanetConstants.INVALID_PLANET;
import static sw.planet.api.common.PlanetConstants.PLANET;

@ExtendWith(MockitoExtension.class)
class PlanetServiceTest {
    @InjectMocks
    private PlanetService service;
    @Mock
    private PlanetRepository repo;

    //operação_estado_retorno
    @Test
    public void createPlanet_WithValidData_ReturnsPlanet() {
        //AAA

        //Arrange
        when(repo.save(PLANET)).thenReturn(PLANET);

        //Act
        Planet sut = service.create(PLANET); //sut = system under test

        //Assert
        assertThat(sut).isEqualTo(PLANET);
    }

    @Test
    public void createPlanet_WithInvalidData_ThrowsException() {

        when(repo.save(INVALID_PLANET)).thenThrow(RuntimeException.class);
        assertThatThrownBy(() -> service.create(INVALID_PLANET)).isInstanceOf(RuntimeException.class);
    }

    @Test
    public void getPlanet_WithExistingId_ReturnsPlanet() {

        when(repo.findById(anyLong())).thenReturn(Optional.of(PLANET));

        Optional<Planet>sut = service.findById(1L);
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_WithUnexistingId_ReturnsPlanet() {

        when(repo.findById(1L)).thenReturn(Optional.empty());
        Optional<Planet>sut = service.findById(1L);
        assertThat(sut).isEmpty();
    }

    @Test
    public void getPlanet_WithExistingName_ReturnsPlanet() {

        when(repo.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        Optional<Planet>sut = service.findByName(PLANET.getName());
        assertThat(sut).isNotEmpty();
        assertThat(sut.get()).isEqualTo(PLANET);
    }

    @Test
    public void getPlanet_WithUnexistingName_ReturnsPlanet() {
        final String name = "Unexisting Name";

        when(repo.findByName(name)).thenReturn(Optional.empty());

        Optional<Planet>sut = service.findByName(name);
        assertThat(sut).isEmpty();
    }

    @Test
    public void listPlanets_ReturnsAllPlanets() {

        List<Planet> planets = new ArrayList<>(){{
            add(PLANET);
        }};
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(PLANET.getTerrain(), PLANET.getClimate()));

        when(repo.findAll(query)).thenReturn(planets);

        List<Planet> sut = service.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).hasSize(1);
        assertThat(sut).isNotEmpty();
        assertThat(sut.get(0)).isEqualTo(PLANET);
    }

    @Test
    public void listPlanets_ReturnsNoPlanets() {
        when(repo.findAll(any())).thenReturn(Collections.emptyList());
        List<Planet> sut = service.list(PLANET.getTerrain(), PLANET.getClimate());

        assertThat(sut).isEmpty();
    }

    @Test
    public void deletePlanet_WithValidId() {
        assertThatCode(() -> service.remove(1L)).doesNotThrowAnyException();
    }

    @Test
    public void deletePlanet_WithInvalidValidId_ThrowssException() {
        doThrow(new RuntimeException()).when(repo).deleteById(99L);
        assertThatThrownBy(() -> service.remove(99L)).isInstanceOf(RuntimeException.class);
    }
}