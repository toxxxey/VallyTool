 package com.etejk.vallytool.controllers;

import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.etejk.vallytool.dao.UsuarioDAO;
import com.etejk.vallytool.entities.RoleModel;
import com.etejk.vallytool.entities.RoleName;
import com.etejk.vallytool.entities.Usuario;
import com.etejk.vallytool.repositories.RoleRepository;
import com.etejk.vallytool.repositories.UsuarioRepository;

@Controller
public class UsuarioController {
	
	@Autowired
	UsuarioRepository ur;
	
	@Autowired
	RoleRepository rr;
	
	@PostMapping("update")
	public String updateUsuario(Model model,@RequestParam(name = "id") String id,
								@RequestParam(name = "role") String role) {
		
		Optional<Usuario> usuario = ur.findById(Integer.parseInt(id));
		if(!usuario.isPresent()) {
			return "redirect:/dados";
		}
		List<RoleModel> roles = new ArrayList<>();
		RoleModel roleModel = rr.findByRoleName(RoleName.valueOf(role));
		if(roleModel == null) {
			return "redirect:/dados";
		}
		roles.add(roleModel);
		usuario.get().setRoles(roles);
		
		ur.save(usuario.get());
		return "redirect:/dados?id=" + id;
		
	}
	@PostMapping("usuario")
	public String saveUsuario(@Valid UsuarioDAO usuarioDAO) {
		System.out.println(usuarioDAO);
		
		
		List<RoleModel> findAllRoles = rr.findAll();
		List<RoleModel> roles = new ArrayList<>();
		for (RoleModel role : findAllRoles) {
			if(role.getRoleName().name().equals(usuarioDAO.getRole())) {
				roles.add(role);
			}
		}
		
		System.out.println(roles.get(0).getRoleName().name());
		
		Usuario usuarioOriginal = new Usuario(
				usuarioDAO.getCpf().replace(".", "").replace("-", ""),
				usuarioDAO.getNome(),
				usuarioDAO.getEmail(),
				usuarioDAO.senha(),
				roles
				);
		try {
		ur.save(usuarioOriginal);
		} catch (Exception e){
			return "redirect:/usuario_error";
		}
		return "redirect:/inicio";
	}
	
	@GetMapping("inicio")
	public String inicio(Model model, @Param("search") String search) {
		if(search != null) {
			model.addAttribute("usuarios", ur.search(search));
		}else {
		model.addAttribute("usuarios", ur.findAll());
		}
		
		return "site/inicio";
	}
	
	@GetMapping("usuario_error")
	public ModelAndView usuarioError(ModelMap model){
		model.addAttribute("error", "Usuario já cadastrado!");
		return new ModelAndView("redirect:/inicio", model);
	}
	
	@GetMapping("vinculos")
    public String vinculos(Model model, @RequestParam(name = "id") String id) {

        Optional<Usuario> user = ur.findById(Integer.parseInt(id));
        if(!user.isPresent()) {
            return "redirect:/inicio";
        };

        model.addAttribute("usuario", user.get());
        return "site/vinculos";
    }
	
	@GetMapping("dados")
    public String dados(Model model, @RequestParam(name = "id") String id) {

        Optional<Usuario> user = ur.findById(Integer.parseInt(id));
        if(!user.isPresent()) {
            return "redirect:/inicio";
        };

        model.addAttribute("usuario", user.get());
        return "site/dados";
    }
}

