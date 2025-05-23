package org.oc.poseidon.repositories;

import org.oc.poseidon.domain.BidList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BidListRepository extends JpaRepository<BidList, Integer> {

    public List<BidList> findAll();
}
