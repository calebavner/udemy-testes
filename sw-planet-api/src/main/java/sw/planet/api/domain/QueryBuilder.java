package sw.planet.api.domain;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

public class QueryBuilder {

    public static Example<Planet> makeQuery(Planet p) {
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
            .withIgnoreCase()
            .withIgnoreNullValues();

        return Example.of(p, exampleMatcher);
    }
}
