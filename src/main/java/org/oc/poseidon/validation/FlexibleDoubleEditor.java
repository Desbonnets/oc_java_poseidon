package org.oc.poseidon.validation;

import java.beans.PropertyEditorSupport;

/**
 * {@code FlexibleDoubleEditor} est un éditeur personnalisé pour les champs {@code Double}
 * qui accepte à la fois les formats numériques avec point (ex. {@code "12.5"}) et avec virgule
 * (ex. {@code "12,5"}), afin de faciliter la saisie des utilisateurs francophones.
 *
 * <p>Cette classe est utile dans les formulaires HTML lorsque les utilisateurs peuvent saisir
 * des nombres en utilisant la virgule comme séparateur décimal, ce qui provoquerait normalement
 * une erreur de conversion avec le parseur Java par défaut.</p>
 *
 * <p>Exemple d'utilisation dans un contrôleur Spring :</p>
 *
 * <pre>{@code
 * @InitBinder
 * public void initBinder(WebDataBinder binder) {
 *     binder.registerCustomEditor(Double.class, new FlexibleDoubleEditor());
 * }
 * }</pre>
 */
public class FlexibleDoubleEditor extends PropertyEditorSupport {

    /**
     * Convertit une chaîne de caractères en {@code Double}, en acceptant à la fois les points et les virgules
     * comme séparateurs décimaux.
     *
     * @param text la chaîne à convertir
     * @throws IllegalArgumentException si la chaîne ne peut pas être convertie en {@code Double}
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.trim().isEmpty()) {
            setValue(null);
        } else {
            String cleaned = text.replace(",", ".");
            try {
                setValue(Double.parseDouble(cleaned));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Format de nombre invalide");
            }
        }
    }
}

