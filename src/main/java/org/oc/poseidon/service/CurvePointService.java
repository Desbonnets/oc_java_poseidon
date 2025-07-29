package org.oc.poseidon.service;

import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.repositories.CurvePointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service métier pour la gestion des entités {@link CurvePoint}.
 * <p>
 * Fournit des méthodes pour la récupération, la validation, l'ajout,
 * la mise à jour et la suppression des objets {@code CurvePoint}.
 */
@Service
public class CurvePointService {

    private final CurvePointRepository repo;

    /**
     * Constructeur injectant le dépôt {@link CurvePointRepository}.
     *
     * @param repo le dépôt de données des {@link CurvePoint}
     */
    public CurvePointService(CurvePointRepository repo) {
        this.repo = repo;
    }

    /**
     * Récupère la liste de tous les {@link CurvePoint} enregistrés.
     *
     * @return la liste des {@code CurvePoint}
     */
    public List<CurvePoint> curveAll() {
        return repo.findAll();
    }

    /**
     * Vérifie que l'objet {@link CurvePoint} est valide.
     * Il doit avoir un {@code curveId} non nul et un {@code term}
     * ou {@code value} numérique valide (non NaN).
     *
     * @param curvePoint l'objet à valider
     * @return true si l'objet est valide, false sinon
     */
    public boolean validCurvePoint(CurvePoint curvePoint) {
        return (curvePoint.getCurveId() != null) &&
                (!curvePoint.getValue().isNaN() || !curvePoint.getTerm().isNaN());
    }

    /**
     * Enregistre un nouveau {@link CurvePoint} s’il est valide.
     *
     * @param curvePoint l'objet à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean addCurvePoint(CurvePoint curvePoint) {
        boolean result = false;

        if (validCurvePoint(curvePoint)) {
            repo.save(curvePoint);
            result = true;
        }

        return result;
    }

    /**
     * Récupère un {@link CurvePoint} par son identifiant.
     *
     * @param id l'identifiant du {@code CurvePoint}
     * @return l'objet trouvé ou {@code null} s'il n'existe pas
     */
    public CurvePoint curvePointById(int id) {
        return repo.findById(id);
    }

    /**
     * Met à jour un {@link CurvePoint} existant avec de nouvelles données.
     *
     * @param formCurvePoint les nouvelles valeurs
     * @param id             l'identifiant de l'objet à modifier
     * @return true si la mise à jour a été effectuée avec succès
     */
    public boolean updateCurvePoint(CurvePoint formCurvePoint, int id) {
        boolean result = false;

        CurvePoint curvePoint = repo.findById(id);

        if (validCurvePoint(formCurvePoint)) {
            curvePoint.setCurveId(formCurvePoint.getCurveId());
            curvePoint.setTerm(formCurvePoint.getTerm());
            curvePoint.setValue(formCurvePoint.getValue());

            repo.save(curvePoint);
            result = true;
        }

        return result;
    }

    /**
     * Supprime un {@link CurvePoint} par son identifiant.
     *
     * @param id l'identifiant du {@code CurvePoint} à supprimer
     */
    public void deleteCurvePoint(int id) {
        CurvePoint curvePoint = curvePointById(id);
        repo.delete(curvePoint);
    }
}
