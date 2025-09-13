package com.oleificiorenna.gestioneingressi.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import com.oleificiorenna.gestioneingressi.services.ExportExcelService;

import jakarta.servlet.http.HttpServletResponse;




@RestController
@RequestMapping("/api/clienti")
@Slf4j
public class ClientiController {
	
	@Autowired
	ClienteService clienteService;
	
	@Autowired
	ExportExcelService exportExcelService;
	
	@GetMapping("/version")
	public String getVersione() {
		return "3";
	}

	@PostMapping("/getAll")
	public List<ClienteDto> getAll(@RequestBody GetAllIngressiDto body){
		return clienteService.findAll(body.getDateFrom(), body.getDateTo());
	}
	
	@PostMapping("/export")
    public void exportClienti(@RequestBody GetAllIngressiDto body, HttpServletResponse response) throws IOException {
		List<ClienteDto> clienti = clienteService.findAll(body.getDateFrom(), body.getDateTo());
		List<ClienteDto> clientiFiltered = new ArrayList<ClienteDto>();
		for(ClienteDto c: clienti) {
			if(body.getIdCliente()== null || body.getIdCliente().equals(c.getId())) {
				clientiFiltered.add(c);
			}
		}
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=clienti.xlsx");

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    String[] headers = { "Codice fiscale/Partita IVA", "Nome", "Cognome", "Nome Azienda", "Monte Ore"};
	    exportExcelService.exportToExcel(clientiFiltered, headers, out);

	    response.getOutputStream().write(out.toByteArray());
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
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
