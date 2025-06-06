package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.oc.poseidon.domain.Rating;
import org.oc.poseidon.domain.Trade;
import org.oc.poseidon.service.TradeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;

@Controller
public class TradeController {

    private final TradeService tradeService;
    private static final String REDIRECT_TRADE = "redirect:/trade/list";

    public TradeController(TradeService tradeService) {
        this.tradeService = tradeService;
    }

    @RequestMapping("/trade/list")
    public String home(HttpServletRequest request, Model model)
    {
        model.addAttribute("remoteUser", request.getRemoteUser());
        model.addAttribute("trades", tradeService.tradeAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade trade, Model model)
    {
        model.addAttribute("trade", trade);
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result)
    {
        if(!result.hasErrors() && tradeService.addTrade(trade)){
            return REDIRECT_TRADE;
        }
        return "trade/add";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model)
    {
        Trade trade = tradeService.tradeById(id);
        model.addAttribute("trade", trade);
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade, BindingResult result)
    {
        if(!result.hasErrors() && tradeService.updateTrade(trade, id)){
            return REDIRECT_TRADE;
        }
        return "trade/update";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model)
    {
        tradeService.deleteTrade(id);
        return REDIRECT_TRADE;
    }
}
