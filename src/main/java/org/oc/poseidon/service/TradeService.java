package org.oc.poseidon.service;

import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.repositories.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des entités {@link Trade}.
 * Fournit les opérations de base : création, lecture, mise à jour et suppression.
 */
@Service
public class TradeService {

    private final TradeRepository repo;

    /**
     * Constructeur avec injection du dépôt {@link TradeRepository}.
     *
     * @param repo le dépôt de gestion des entités {@link Trade}
     */
    public TradeService(TradeRepository repo) {
        this.repo = repo;
    }

    /**
     * Récupère toutes les entités {@link Trade} enregistrées.
     *
     * @return liste de toutes les transactions
     */
    public List<Trade> tradeAll() {
        return repo.findAll();
    }

    /**
     * Valide les champs obligatoires d'une entité {@link Trade}.
     *
     * @param trade l'entité à valider
     * @return true si les champs requis sont valides, sinon false
     */
    public boolean validTrade(Trade trade) {
        return (
                trade.getAccount() != null ||
                        trade.getType() != null ||
                        trade.getBuyQuantity() != null
        );
    }

    /**
     * Ajoute une transaction si elle est valide.
     *
     * @param trade la transaction à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addTrade(Trade trade) {
        boolean result = false;

        if (validTrade(trade)) {
            repo.save(trade);
            result = true;
        }

        return result;
    }

    /**
     * Récupère une entité {@link Trade} par son identifiant.
     *
     * @param id identifiant de la transaction
     * @return l'entité {@link Trade} correspondante
     */
    public Trade tradeById(int id) {
        return repo.findByTradeId(id);
    }

    /**
     * Met à jour une entité {@link Trade} existante.
     *
     * @param formTrade les nouvelles données
     * @param id identifiant de la transaction à mettre à jour
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean updateTrade(Trade formTrade, int id) {
        boolean result = false;

        Trade trade = tradeById(id);

        if (validTrade(formTrade)) {
            trade.setAccount(formTrade.getAccount());
            trade.setType(formTrade.getType());
            trade.setBuyQuantity(formTrade.getBuyQuantity());

            repo.save(trade);
            result = true;
        }

        return result;
    }

    /**
     * Supprime une entité {@link Trade} à partir de son identifiant.
     *
     * @param id identifiant de la transaction à supprimer
     */
    public void deleteTrade(int id) {
        Trade trade = tradeById(id);
        repo.delete(trade);
    }
}
