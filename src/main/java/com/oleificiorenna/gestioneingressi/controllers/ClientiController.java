package com.oleificiorenna.gestioneingressi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.oleificiorenna.gestioneingressi.dtos.ClienteDto;
import com.oleificiorenna.gestioneingressi.dtos.GetAllIngressiDto;
import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.services.ClienteService;


@RestController
@RequestMapping("/api/clienti")
@Slf4j
public class ClientiController {
	
	@Autowired
	ClienteService clienteService;

	@PostMapping("/getAll")
	public List<ClienteDto> getAll(@RequestBody GetAllIngressiDto body){
		return clienteService.findAll(body.getDateFrom(), body.getDateTo());
	}
	
	@GetMapping("/get/{id}")
	public Cliente getById(Integer id) throws Exception {
		return clienteService.getById(id);
	}
	
	@PostMapping("/save")
	public Cliente save(@RequestBody Cliente cliente) throws Exception {
		return clienteService.save(cliente);
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Integer id) throws Exception {
		clienteService.delete(id);
	}

}
