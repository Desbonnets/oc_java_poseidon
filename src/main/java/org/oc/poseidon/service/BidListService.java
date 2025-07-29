package org.oc.poseidon.service;

import org.oc.poseidon.domain.BidList;
import org.oc.poseidon.repositories.BidListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des entités {@link BidList}.
 * <p>
 * Fournit des méthodes pour effectuer des opérations CRUD
 * ainsi que des validations spécifiques sur les objets {@code BidList}.
 */
@Service
public class BidListService {

    private final BidListRepository repo;

    /**
     * Constructeur injectant le dépôt {@link BidListRepository}.
     *
     * @param repo le dépôt de données des {@link BidList}
     */
    public BidListService(BidListRepository repo) {
        this.repo = repo;
    }

    /**
     * Récupère la liste de tous les {@link BidList} enregistrés.
     *
     * @return la liste des {@code BidList}
     */
    public List<BidList> bidListAll() {
        return repo.findAll();
    }

    /**
     * Vérifie que le {@link BidList} fourni est valide.
     * Les champs {@code account}, {@code type} ou {@code bidQuantity}
     * doivent être non nuls, et {@code account} ou {@code type} non vides.
     *
     * @param bid l'objet à valider
     * @return true si l'objet est considéré comme valide
     */
    public boolean validBidList(BidList bid) {
        return (bid.getAccount() != null || bid.getType() != null || bid.getBidQuantity() != null) &&
                (!bid.getAccount().trim().isEmpty() || !bid.getType().trim().isEmpty());
    }

    /**
     * Ajoute un {@link BidList} s’il est valide.
     *
     * @param bid l'objet à ajouter
     * @return true si l'ajout a réussi
     */
    public boolean addBidList(BidList bid) {
        boolean result = false;

        if (validBidList(bid)) {
            repo.save(bid);
            result = true;
        }

        return result;
    }

    /**
     * Récupère un {@link BidList} à partir de son identifiant.
     *
     * @param id l'identifiant du {@code BidList}
     * @return l'objet trouvé, ou null s'il n'existe pas
     */
    public BidList bidListById(int id) {
        return repo.findByBidListId(id);
    }

    /**
     * Met à jour un {@link BidList} existant à partir des données fournies.
     *
     * @param formBid les nouvelles valeurs à appliquer
     * @param id      l'identifiant du bid à mettre à jour
     * @return true si la mise à jour a été effectuée
     */
    public boolean updateBidList(BidList formBid, int id) {
        boolean result = false;

        BidList bid = repo.findByBidListId(id);

        if (validBidList(formBid)) {
            bid.setAccount(formBid.getAccount());
            bid.setType(formBid.getType());
            bid.setBidQuantity(formBid.getBidQuantity());

            repo.save(bid);
            result = true;
        }

        return result;
    }

    /**
     * Supprime un {@link BidList} à partir de son identifiant.
     *
     * @param id l'identifiant du bid à supprimer
     */
    public void deleteBidList(int id) {
        BidList bid = bidListById(id);
        repo.delete(bid);
    }
}
