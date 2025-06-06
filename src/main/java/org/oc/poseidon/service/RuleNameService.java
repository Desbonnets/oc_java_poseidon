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

    public boolean validRuleName(RuleName ruleName)
    {
        return (
                ruleName.getName() != null ||
                ruleName.getDescription() != null ||
                ruleName.getJson() != null ||
                ruleName.getTemplate() != null ||
                ruleName.getSqlStr() != null ||
                ruleName.getSqlPart() != null);
    }

    public boolean addRuleName(RuleName ruleName)
    {
        boolean result = false;

        if(validRuleName(ruleName))
        {
            repo.save(ruleName);
            result = true;
        }

        return result;
    }

    public RuleName ruleNameById(int id)
    {
        return repo.findById(id);
    }

    public boolean updateRuleName(RuleName formRuleName, int id)
    {
        boolean result = false;

        RuleName ruleName = ruleNameById(id);

        if(validRuleName(formRuleName)){

            ruleName.setName(formRuleName.getName());
            ruleName.setDescription(formRuleName.getDescription());
            ruleName.setJson(formRuleName.getJson());
            ruleName.setTemplate(formRuleName.getTemplate());
            ruleName.setSqlStr(formRuleName.getSqlStr());
            ruleName.setSqlPart(formRuleName.getSqlPart());

            repo.save(ruleName);

            result = true;
        }

        return result;
    }

