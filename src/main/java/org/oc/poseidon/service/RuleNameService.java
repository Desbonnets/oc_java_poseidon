package org.oc.poseidon.service;

import org.oc.poseidon.domain.RuleName;
import org.oc.poseidon.repositories.RuleNameRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service de gestion des entités {@link RuleName}.
 * Fournit les opérations métier pour ajouter, mettre à jour, valider, récupérer et supprimer des règles.
 */
@Service
public class RuleNameService {

    private final RuleNameRepository repo;

    /**
     * Constructeur avec injection du repository.
     *
     * @param repo le dépôt des règles {@link RuleName}
     */
    public RuleNameService(RuleNameRepository repo) {
        this.repo = repo;
    }

    /**
     * Récupère toutes les règles enregistrées.
     *
     * @return liste de toutes les entités {@link RuleName}
     */
    public List<RuleName> ruleNameAll() {
        return repo.findAll();
    }

    /**
     * Vérifie qu'une entité {@link RuleName} contient au moins une propriété non nulle.
     *
     * @param ruleName l'entité à valider
     * @return true si l'entité est valide, sinon false
     */
    public boolean validRuleName(RuleName ruleName) {
        return (
                ruleName.getName() != null ||
                        ruleName.getDescription() != null ||
                        ruleName.getJson() != null ||
                        ruleName.getTemplate() != null ||
                        ruleName.getSqlStr() != null ||
                        ruleName.getSqlPart() != null
        );
    }

    /**
     * Ajoute une nouvelle règle si elle est valide.
     *
     * @param ruleName la règle à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addRuleName(RuleName ruleName) {
        boolean result = false;

        if (validRuleName(ruleName)) {
            repo.save(ruleName);
            result = true;
        }

        return result;
    }

    /**
     * Récupère une règle par son identifiant.
     *
     * @param id identifiant de la règle
     * @return l'entité {@link RuleName} correspondante
     */
    public RuleName ruleNameById(int id) {
        return repo.findById(id);
    }

    /**
     * Met à jour une règle existante avec les données fournies.
     *
     * @param formRuleName les nouvelles données
     * @param id identifiant de la règle à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateRuleName(RuleName formRuleName, int id) {
        boolean result = false;

        RuleName ruleName = ruleNameById(id);

        if (validRuleName(formRuleName)) {
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

    /**
     * Supprime une règle par son identifiant.
     *
     * @param id identifiant de la règle à supprimer
     */
    public void deleteRuleName(int id) {
        RuleName ruleName = ruleNameById(id);
        repo.delete(ruleName);
    }
}
