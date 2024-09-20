package de.mayer.backendspringpostgres.adventure.persistence;

import de.mayer.backendspringpostgres.adventure.persistence.dto.AdventureJpa;
import de.mayer.backendspringpostgres.adventure.persistence.jparepo.AdventureJpaRepository;
import de.mayer.penandpaperdmhelperjcore.adventure.domainservice.AdventureRepository;
import de.mayer.penandpaperdmhelperjcore.adventure.model.Adventure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class AdventureRepositoryWithJpa implements AdventureRepository {

    private static final Logger log = LoggerFactory.getLogger(AdventureRepositoryWithJpa.class);

    private final AdventureJpaRepository adventureJpaRepository;

    public AdventureRepositoryWithJpa(AdventureJpaRepository adventureJpaRepository) {
        this.adventureJpaRepository = adventureJpaRepository;
    }

    @Override
    public Optional<Adventure> findByName(String adventureName) {
        return adventureJpaRepository.findByName(adventureName)
                .map(jpa -> new Adventure(jpa.getName(), null));
    }

    @Override
    public void save(Adventure adventure) {
        adventureJpaRepository.save(new AdventureJpa(adventure.name()));
    }

    @Override
    public void changeName(Adventure adventure, String newAdventureName) {
        var optionalAdventure = adventureJpaRepository.findByName(adventure.name());
        if (optionalAdventure.isPresent()) {
            var adventureJpa = optionalAdventure.get();
            adventureJpa.setName(newAdventureName);

            adventureJpa = adventureJpaRepository.saveAndFlush(adventureJpa);
            log.debug("Changed name of Adventure: {}", adventureJpa);
        }
    }

    @Override
    public void delete(Adventure adventure) {
        var adventureJpa = adventureJpaRepository.findByName(adventure.name());
        adventureJpa.ifPresent(adventureJpaRepository::delete);
    }

    @Override
    public List<Adventure> findAll() {
        return adventureJpaRepository.findAll(Sort.by("name"))
                .stream()
                .map(jpa -> new Adventure(jpa.getName(), null))
                .collect(Collectors.toList());
    }
}
