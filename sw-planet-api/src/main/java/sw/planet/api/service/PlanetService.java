package sw.planet.api.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import sw.planet.api.domain.Planet;
import sw.planet.api.domain.QueryBuilder;
import sw.planet.api.repo.PlanetRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlanetService {

    private final PlanetRepository repo;

    public PlanetService(PlanetRepository repo) {
        this.repo = repo;
    }

    public Planet create(Planet p) {
        return repo.save(p);
    }

    public Optional<Planet> findById(Long id) {
        return repo.findById(id);
    }

    public Optional<Planet> findByName(String name) {
        return repo.findByName(name);
    }

    public List<Planet> list(String terrain, String climate) {
        Example<Planet> query = QueryBuilder.makeQuery(new Planet(terrain, climate));
        return repo.findAll(query);
    }

    public void remove(Long id) {
        repo.deleteById(id);
    }
}
