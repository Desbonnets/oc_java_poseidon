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

    public boolean addBidList(BidList bid)
    {
        boolean result = false;

        if(
                (bid.getAccount() != null || bid.getType() != null || bid.getBidQuantity() != null) &&
                (!bid.getAccount().trim().isEmpty() || !bid.getType().trim().isEmpty())
        )
        {
                repo.save(bid);
                result = true;
            }


        return result;
    }
}
