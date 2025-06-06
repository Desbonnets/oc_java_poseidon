package org.oc.poseidon.service;

import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    private final TradeRepository repo;

    public TradeService(TradeRepository repo) {
        this.repo = repo;
    }

    public List<Trade> tradeAll() {
        return repo.findAll();
    }

    public boolean validTrade(Trade trade)
    {
        return (
                trade.getAccount() != null ||
                trade.getType() != null ||
                trade.getBuyQuantity() != null);
    }

    public boolean addTrade(Trade trade)
    {
        boolean result = false;

        if(validTrade(trade))
        {
            repo.save(trade);
            result = true;
        }

        return result;
    }

