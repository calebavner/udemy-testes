package sw.planet.api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sw.planet.api.domain.Planet;
import sw.planet.api.service.PlanetService;

import java.util.List;

@RestController
@RequestMapping("/planets")
public class PlanetController {
    @Autowired
    private PlanetService service;

    @PostMapping
    public ResponseEntity<Planet> create(@RequestBody @Valid Planet p) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(service.create(p));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Planet> findById(@PathVariable Long id) {
        return service.findById(id).map(p -> ResponseEntity.ok(p))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Planet> getByName(@PathVariable String name) {
        return service.findByName(name).map(p -> ResponseEntity.ok(p))
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Planet>> listPlanets(@RequestParam(required = false) String terrain,
        @RequestParam(required = false) String climate) {

        List<Planet> p = service.list(terrain, climate);
        return ResponseEntity.ok(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable @Valid Long id) {
        service.remove(id);
        return ResponseEntity.noContent().build();
    }

}
