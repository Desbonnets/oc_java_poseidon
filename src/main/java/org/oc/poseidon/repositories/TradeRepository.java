package org.oc.poseidon.repositories;

import org.oc.poseidon.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TradeRepository extends JpaRepository<Trade, Integer> {
}
