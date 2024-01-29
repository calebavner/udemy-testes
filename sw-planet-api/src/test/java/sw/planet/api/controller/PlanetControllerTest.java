package sw.planet.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import sw.planet.api.domain.Planet;
import sw.planet.api.service.PlanetService;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sw.planet.api.common.PlanetConstants.*;

@WebMvcTest(PlanetController.class)
class PlanetControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private PlanetService service;

    @Test
    public void createPlanet_WithValidData_ReturnsCreated() throws Exception{

        when(service.create(PLANET)).thenReturn(PLANET);

        mockMvc.perform(
                    post("/planets").content(mapper.writeValueAsString(PLANET)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void createPlanet_WithInvalidData_ReturnsBadRequest() throws Exception{

        Planet emptyPlanet = new Planet();
        Planet invalidPlanet = new Planet("", "", "");

        mockMvc.perform(
                post("/planets").content(mapper.writeValueAsString(emptyPlanet))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());

        mockMvc.perform(
                post("/planets").content(mapper.writeValueAsString(invalidPlanet))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void createPlanet_WithExistingName_ReturnsConflict() throws Exception{

        when(service.create(any())).thenThrow(DataIntegrityViolationException.class);

        mockMvc.perform(
                post("/planets").content(mapper.writeValueAsString(PLANET))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict());
    }

    @Test
    public void getPlanet_WithExistingdId_ReturnPlanet() throws Exception{

        when(service.findById(anyLong())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test
    public void getPlanet_WithUnexistingdId_ReturnNotFound() throws Exception{
        mockMvc.perform(get("/planets/1"))
                .andExpect(status().isNotFound());
    }

    @Test void getPlanet_WithValidName_ReturnsPlanet() throws Exception{

        when(service.findByName(PLANET.getName())).thenReturn(Optional.of(PLANET));

        mockMvc.perform(get("/planets/name/" + PLANET.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(PLANET));
    }

    @Test void getPlanet_WithInvalidName_ReturnsNotFound() throws Exception{

        mockMvc.perform(get("/planets/name/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void listPlanet_ReturnsFilteredPlanets() throws Exception{

        when(service.list(null, null)).thenReturn(PLANETS);
        when(service.list(TATOOINE.getTerrain(), TATOOINE.getClimate())).thenReturn(List.of(TATOOINE));

        mockMvc.perform(get("/planets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/planets?" + String.format("terrain=%s&climate=%s", TATOOINE.getTerrain(), TATOOINE.getClimate())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(TATOOINE))
                .andExpect(jsonPath("$[0]", hasSize(1)));
    }

    @Test
    public void listPlanet_ReturnsNoPlanets() throws Exception{

    }
}