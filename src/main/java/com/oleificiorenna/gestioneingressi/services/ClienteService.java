package com.oleificiorenna.gestioneingressi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.oleificiorenna.gestioneingressi.repositories.ClienteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.oleificiorenna.gestioneingressi.entities.Cliente;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public List<Cliente> findAll() {
		return clienteRepository.findAll();
	}

	public Cliente save(Cliente cliente) throws Exception {
		Optional<Cliente> existingCliente = clienteRepository.findByCodiceFiscalePIVA(cliente.getCodiceFiscalePIVA());
		if (cliente.getId()== null && existingCliente!= null && existingCliente.isPresent()) {
			throw new Exception("Esiste già un cliente con lo stesso codice fiscale/partita IVA");
		}
		return clienteRepository.save(cliente);
	}

	public void delete(Integer id) throws Exception {
		Optional<Cliente> existingCliente = clienteRepository.findById(id);
		if (existingCliente == null || !existingCliente.isPresent()) {
			throw new Exception("Non esiste un cliente con l'id specificato");
		}
		clienteRepository.deleteById(id);
	}

	public Cliente getById(Integer id) throws Exception {
		Optional<Cliente> existingCliente = clienteRepository.findById(id);
		if (existingCliente == null || !existingCliente.isPresent()) {
			throw new Exception("Non esiste un cliente con l'id specificato");
		}
		return existingCliente.get();
	}
}
