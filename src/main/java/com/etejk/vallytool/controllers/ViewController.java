package com.etejk.vallytool.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {
	
	@GetMapping("")
	public String redirect() {
		return "redirect:/inicio";
	}
	
	@GetMapping("redefinir")
	public String redefinir() {
		return "site/redefinir_senha";
	}
	
	@GetMapping("configuracoes")
	public String configuracoes() {
		return "site/configuracoes";
	}
	
	
	@GetMapping("inicio")
	public String inicio() {
		return "site/inicio";
	}
	
	@GetMapping("login")
	public String login() {
		return "site/login";
	}
}
