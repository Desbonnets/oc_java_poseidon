package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.CurvePoint;
import org.oc.poseidon.service.CurvePointService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
public class CurvePointController {

    private final CurvePointService curvePointService;
    private static final String REDIRECT_CURVEPOINTLIST = "redirect:/curvePoint/list";

    public CurvePointController(CurvePointService curvePointService) {
        this.curvePointService = curvePointService;
    }

    @RequestMapping("/curvePoint/list")
    public String home(HttpServletRequest request, Model model)
    {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("curvePoints", curvePointService.curveAll());

        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint, Model model)
    {

        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result) {

        if (!result.hasErrors() && curvePointService.addCurvePoint(curvePoint)){
            return REDIRECT_CURVEPOINTLIST;
        }

        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        CurvePoint curvePoint = curvePointService.curvePointById(id);

        model.addAttribute("curvePoint", curvePoint);
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {

        if (!result.hasErrors() && curvePointService.updateCurvePoint(curvePoint, id)){
            return REDIRECT_CURVEPOINTLIST;
        }

        return "curvePoint/update";
    }

    @DeleteMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        curvePointService.deleteCurvePoint(id);
        return REDIRECT_CURVEPOINTLIST;
    }
}
