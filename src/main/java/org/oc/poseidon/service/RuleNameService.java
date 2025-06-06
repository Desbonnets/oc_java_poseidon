package org.oc.poseidon.service;

import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.repositories.RuleNameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService {

    private final RuleNameRepository repo;

    public RuleNameService(RuleNameRepository repo) {
        this.repo = repo;
    }

    public List<RuleName> ruleNameAll() {
        return repo.findAll();
    }

}
