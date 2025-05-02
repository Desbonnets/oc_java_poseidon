package org.oc.poseidon.repositories;

import org.oc.poseidon.domain.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

}
