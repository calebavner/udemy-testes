package sw.planet.api.repo;

import org.springframework.data.domain.Example;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import sw.planet.api.domain.Planet;

import java.util.List;
import java.util.Optional;

public interface PlanetRepository extends CrudRepository<Planet, Long>, QueryByExampleExecutor<Planet> {

    Optional<Planet> findByName(String name);
    <S extends Planet> List<S> findAll(Example<S> example);
}
