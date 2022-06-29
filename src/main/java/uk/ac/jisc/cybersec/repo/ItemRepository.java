package uk.ac.jisc.cybersec.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import uk.ac.jisc.cybersec.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{


}
