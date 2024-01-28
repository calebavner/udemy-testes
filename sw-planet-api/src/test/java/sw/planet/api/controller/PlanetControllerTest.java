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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static sw.planet.api.common.PlanetConstants.PLANET;

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
}