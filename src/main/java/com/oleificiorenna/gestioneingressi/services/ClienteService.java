package com.oleificiorenna.gestioneingressi.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.oleificiorenna.gestioneingressi.repositories.ClienteRepository;
import com.oleificiorenna.gestioneingressi.repositories.IngressoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.oleificiorenna.gestioneingressi.dtos.ClienteDto;
import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private IngressoRepository ingressoRepository;

	public List<ClienteDto> findAll(LocalDateTime startDate, LocalDateTime endDate) {
		List<Cliente> clienti = clienteRepository.findAll();
		List<ClienteDto> res = new ArrayList<ClienteDto>();
		for(Cliente c: clienti) {
			ClienteDto cDto = new ClienteDto();
			cDto.setId(c.getId());
			cDto.setCodiceFiscalePIVA(c.getCodiceFiscalePIVA());
			cDto.setCognome(c.getCognome());
			cDto.setNomeAzienda(c.getNomeAzienda());
			cDto.setNome(c.getNome());
			cDto.setMonteOre(0L);
			if(startDate!=null || endDate !=null) {
				List<Ingresso> ingressiIntervallo = ingressoRepository.findIngressiInIntervallo(startDate, endDate);
				for(Ingresso ingresso:ingressiIntervallo) {
					if(ingresso.getCliente().getId().equals(c.getId())) {
						cDto.setMonteOre(cDto.getMonteOre() + Duration.between(ingresso.getDataOraIngresso(), ingresso.getDataOraUscita()).toHours());
					}
				}
			} else {
				List<Ingresso> ingressi = ingressoRepository.findByCliente(c);
				for(Ingresso ingresso:ingressi) {
					if(ingresso.getCliente().getId().equals(c.getId())) {
						cDto.setMonteOre(cDto.getMonteOre() + Duration.between(ingresso.getDataOraIngresso(), ingresso.getDataOraUscita()).toHours());
					}
				}
			}
			res.add(cDto);
		}
		return res;
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
