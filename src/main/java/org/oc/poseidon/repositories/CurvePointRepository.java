package org.oc.poseidon.repositories;

import org.oc.poseidon.domain.CurvePoint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CurvePointRepository extends JpaRepository<CurvePoint, Integer> {
    CurvePoint findById(int id);
}
