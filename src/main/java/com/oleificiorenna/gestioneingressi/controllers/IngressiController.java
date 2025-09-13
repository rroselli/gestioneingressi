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

import com.oleificiorenna.gestioneingressi.dtos.ClienteDto;
import com.oleificiorenna.gestioneingressi.dtos.GetAllIngressiDto;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;
import com.oleificiorenna.gestioneingressi.repositories.IngressoRepository;
import com.oleificiorenna.gestioneingressi.services.ExportExcelService;
import com.oleificiorenna.gestioneingressi.services.IngressoService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/ingressi")
public class IngressiController {

	@Autowired
	IngressoService ingressoService;
	
	@Autowired
	ExportExcelService exportExcelService;
	
	@GetMapping("/getAll")
	public List<Ingresso> getAll(){
		return ingressoService.getAll();
	}
	
	@PostMapping("/save")
	public Ingresso save(@RequestBody Ingresso ingresso) {
		return ingressoService.save(ingresso);
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Integer id) throws Exception {
		ingressoService.delete(id);
	}
	
	@PostMapping("/export")
    public void exportIngressi(@RequestBody GetAllIngressiDto body, HttpServletResponse response) throws IOException {
		List<Ingresso> ingressi = ingressoService.getIngressiByRange(body.getDateFrom(), body.getDateTo());
		List<Ingresso> ingressiFiltered = new ArrayList<Ingresso>();
		for(Ingresso i: ingressi) {
			if(body.getIdCliente()== null || body.getIdCliente().equals(i.getCliente().getId())) {
				ingressiFiltered.add(i);
			}
		}
	    response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	    response.setHeader("Content-Disposition", "attachment; filename=ingressi.xlsx");

	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    String[] headers = {"Codice fiscale/Partita IVA", "Nome", "Cognome", "Nome Azienda",  "Data e ora ingresso", "Data e ora uscita"};
	    exportExcelService.exportToExcel(ingressiFiltered, headers, out);

	    response.getOutputStream().write(out.toByteArray());
	    response.getOutputStream().flush();
	    response.getOutputStream().close();
    }
	
}
