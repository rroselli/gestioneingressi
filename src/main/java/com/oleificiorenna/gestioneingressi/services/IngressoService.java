package com.oleificiorenna.gestioneingressi.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oleificiorenna.gestioneingressi.repositories.ClienteRepository;
import com.oleificiorenna.gestioneingressi.repositories.IngressoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;

@Service
@Slf4j
@RequiredArgsConstructor
public class IngressoService {

	@Autowired
	IngressoRepository ingressoRepository;

	@Autowired
	ClienteRepository clienteRepository;

	public List<Ingresso> getAll() {
		return ingressoRepository.findAll();
	}

	public Ingresso save(Ingresso ingresso) {
		return ingressoRepository.save(ingresso);
	}

	public void delete(Integer id) {
		ingressoRepository.deleteById(id);
	}

	public List<Ingresso> getIngressiByRange(LocalDateTime dateFrom, LocalDateTime dateTo) {
		if (dateFrom != null || dateFrom != null) {
			return ingressoRepository.findIngressiInIntervallo(dateFrom, dateTo);
		} else {
			return getAll();
		}
	}
}
