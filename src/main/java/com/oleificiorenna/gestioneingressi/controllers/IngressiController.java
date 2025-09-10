package com.oleificiorenna.gestioneingressi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oleificiorenna.gestioneingressi.entities.Ingresso;
import com.oleificiorenna.gestioneingressi.repositories.IngressoRepository;
import com.oleificiorenna.gestioneingressi.services.IngressoService;

@RestController
@RequestMapping("/api/ingressi")
public class IngressiController {

	@Autowired
	IngressoService ingressoService;
	
	@GetMapping("/getAll")
	public List<Ingresso> getAll(){
		return ingressoService.getAll();
	}
	
	@PostMapping("/save")
	public Ingresso save(@RequestBody Ingresso ingresso) {
		return ingressoService.save(ingresso);
	}
	
}
