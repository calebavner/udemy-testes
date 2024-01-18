package sw.planet.api.common;

import sw.planet.api.domain.Planet;

public class PlanetConstants {

    public static final Planet PLANET = new Planet("name", "climate", "terrain");
    public static final Planet INVALID_PLANET = new Planet(" ", " ", " ");
}
