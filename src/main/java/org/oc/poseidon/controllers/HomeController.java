package org.oc.poseidon.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController
{
	@RequestMapping("/")
	public String home(Model model)
	{
		return "home";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping("/admin/home")
	public String adminHome(Model model)
	{
		return "redirect:/bidList/list";
	}

	@PreAuthorize("hasRole('USER')")
	@RequestMapping("/user/home")
	public String userHome(HttpServletRequest request, Model model)
	{
		model.addAttribute("remoteUser", request.getRemoteUser());
		return "userHome";
	}
}
