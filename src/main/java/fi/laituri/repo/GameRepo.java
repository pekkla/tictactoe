package fi.laituri.repo;

import fi.laituri.game.GameState;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends CrudRepository<GameState, String> {}
