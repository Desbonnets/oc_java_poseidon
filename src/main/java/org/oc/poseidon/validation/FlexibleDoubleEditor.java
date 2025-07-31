package org.oc.poseidon.validation;

import java.beans.PropertyEditorSupport;

public class FlexibleDoubleEditor extends PropertyEditorSupport {
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

