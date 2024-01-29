package sw.planet.api.common;

import sw.planet.api.domain.Planet;

import java.util.ArrayList;
import java.util.List;

public class PlanetConstants {

    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet(" ", " ", " ");
    public static final Planet TATOOINE = new Planet(1L, "Tatooine", "arid", "desert");
    public static final Planet ALDERAAN = new Planet(2L, "ALDERAAN", "temperate", "grasslands");
    public static final Planet YAVINIV = new Planet(3L, "Yaviniv", "temperate, tropical", "jungle");
    public static final List<Planet> PLANETS = new ArrayList<>(){
        {
        add(TATOOINE);
        add(ALDERAAN);
        add(YAVINIV);
        }
    };
}
