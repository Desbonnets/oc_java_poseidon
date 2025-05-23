package org.oc.poseidon.service;

import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.repositories.BidListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {

    private BidListRepository repo;

    public BidListService(BidListRepository repo) {
        this.repo = repo;
    }

    public List<BidList> bidListAll() {
        return repo.findAll();
    }
}
