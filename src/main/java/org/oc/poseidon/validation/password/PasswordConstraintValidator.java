package org.oc.poseidon.validation.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validateur de contrainte personnalisée pour valider la robustesse d’un mot de passe.
 * Cette classe implémente l’annotation personnalisée {@link ValidPassword}.
 *
 * <p>Les règles de validation du mot de passe sont les suivantes :
 * <ul>
 *     <li>Minimum 8 caractères</li>
 *     <li>Au moins une lettre majuscule</li>
 *     <li>Au moins un chiffre</li>
 *     <li>Au moins un caractère spécial (ex. @, $, %, etc.)</li>
 * </ul>
 */
public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    /**
     * Expression régulière définissant les règles du mot de passe.
     */
    private static final String PASSWORD_PATTERN =
            "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#~()_+=\\-{}\\[\\]:;\"'<>,./]).{8,}$";

    /**
     * Vérifie si le mot de passe donné est valide selon le motif défini.
     *
     * @param password le mot de passe à valider
     * @param context le contexte de validation (non utilisé ici)
     * @return true si le mot de passe est conforme aux règles, sinon false
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password != null && password.matches(PASSWORD_PATTERN);
    }
}
