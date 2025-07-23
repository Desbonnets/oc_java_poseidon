package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/access-denied")
    public String accessDenied(HttpServletRequest request, Model model) {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("errorMessage", "Accès refusé : vous n’avez pas les permissions nécessaires.");
        return "403";
    }
}
