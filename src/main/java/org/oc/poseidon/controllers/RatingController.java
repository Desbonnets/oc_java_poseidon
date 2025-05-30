package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.service.RatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class RatingController {

    private final RatingService ratingService;
    private static final String REDIRECT_RATING = "redirect:/rating/list";

    public RatingController(final RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @RequestMapping("/rating/list")
    public String home(HttpServletRequest request, Model model)
    {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("ratings", ratingService.ratingAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating, Model model)
    {
        model.addAttribute("rating", rating);
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model)
    {
        if(!result.hasErrors() && ratingService.addRating(rating)){
            return REDIRECT_RATING;
        }
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Rating by Id and to model then show to the form
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Rating and return Rating list
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Rating by Id and delete the Rating, return to Rating list
        return "redirect:/rating/list";
    }
}
