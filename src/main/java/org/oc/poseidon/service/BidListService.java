package org.oc.poseidon.service;

import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.repositories.BidListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService {

    private final BidListRepository repo;

    public BidListService(BidListRepository repo) {
        this.repo = repo;
    }

    public List<BidList> bidListAll() {
        return repo.findAll();
    }

    public boolean validBidList(BidList bid)
    {
        return (bid.getAccount() != null || bid.getType() != null || bid.getBidQuantity() != null) &&
                (!bid.getAccount().trim().isEmpty() || !bid.getType().trim().isEmpty());
    }

    public boolean addBidList(BidList bid)
    {
        boolean result = false;

        if(validBidList(bid))
        {
            repo.save(bid);
            result = true;
        }

        return result;
    }

    public BidList bidListById(int id)
    {
        return repo.findById(id);
    }

    public boolean updateBidList(BidList formBid, int id)
    {
        boolean result = false;

        BidList bid = repo.findById(id);

        if(validBidList(formBid)){

            bid.setAccount(formBid.getAccount());
            bid.setType(formBid.getType());
            bid.setBidQuantity(formBid.getBidQuantity());

            repo.save(bid);

            result = true;
        }

        return result;
    }
}
