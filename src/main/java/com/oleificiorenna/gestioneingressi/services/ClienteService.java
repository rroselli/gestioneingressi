package com.oleificiorenna.gestioneingressi.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oleificiorenna.gestioneingressi.repositories.ClienteRepository;
import com.oleificiorenna.gestioneingressi.repositories.IngressoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.oleificiorenna.gestioneingressi.dtos.ClienteDto;
import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;
import com.oleificiorenna.gestioneingressi.exceptions.BadRequestException;
import com.oleificiorenna.gestioneingressi.exceptions.ResourceNotFoundException;

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
		for (Cliente c : clienti) {
			ClienteDto cDto = new ClienteDto();
			cDto.setId(c.getId());
			cDto.setCodiceFiscalePIVA(c.getCodiceFiscalePIVA());
			cDto.setCognome(c.getCognome());
			cDto.setNomeAzienda(c.getNomeAzienda());
			cDto.setNome(c.getNome());
			long totaleMinuti = 0;
			if (startDate != null || endDate != null) {
				List<Ingresso> ingressiIntervallo = ingressoRepository.findIngressiInIntervallo(startDate, endDate);
				for (Ingresso ingresso : ingressiIntervallo) {
					if (ingresso.getCliente().getId().equals(c.getId()) && ingresso.getDataOraUscita() != null) {
						LocalDateTime effStart = ingresso.getDataOraIngresso();
						LocalDateTime effEnd = ingresso.getDataOraUscita();

						if (startDate != null && effStart.isBefore(startDate)) {
							effStart = startDate;
						}
						if (endDate != null && effEnd.isAfter(endDate)) {
							effEnd = endDate;
						}

						if (!effEnd.isBefore(effStart)) {
							totaleMinuti += Duration.between(effStart, effEnd).toMinutes();
						}
					}
				}
			} else {
				List<Ingresso> ingressi = ingressoRepository.findByCliente(c);
				for (Ingresso ingresso : ingressi) {
					if (ingresso.getCliente().getId().equals(c.getId()) && ingresso.getDataOraUscita() != null) {
						totaleMinuti += Duration
								.between(ingresso.getDataOraIngresso(), ingresso.getDataOraUscita()).toMinutes();
					}
				}
			}
			cDto.setMonteOre(totaleMinuti/60.0);
			res.add(cDto);
		}
		return res;
	}
	
//	public <T> void exportToExcel(List<T> dataList, String[] headers, String filePath) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Export");
//
//        // Crea header
//        Row headerRow = sheet.createRow(0);
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//            CellStyle style = workbook.createCellStyle();
//            Font font = workbook.createFont();
//            font.setBold(true);
//            style.setFont(font);
//            cell.setCellStyle(style);
//        }
//
//        // Popola i dati
//        int rowNum = 1;
//        for (T item : dataList) {
//            Row row = sheet.createRow(rowNum++);
//            int colNum = 0;
//            
//            for (java.lang.reflect.Field field : item.getClass().getDeclaredFields()) {
//                field.setAccessible(true);
//                Object value;
//                try {
//                    value = field.get(item);
//                    Cell cell = row.createCell(colNum++);
//                    if (value instanceof Number) {
//                        cell.setCellValue(((Number) value).doubleValue());
//                    } else if (value instanceof java.time.LocalDateTime) {
//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//                        cell.setCellValue(((java.time.LocalDateTime) value).format(formatter));
//                    } else if (value != null) {
//                        cell.setCellValue(value.toString());
//                    } else {
//                        cell.setCellValue("");
//                    }
//                } catch (IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        // Autosize colonne
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
//
//        // Salva su file
//        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
//            workbook.write(fileOut);
//        }
//
//        workbook.close();
//    }

	public Cliente save(Cliente cliente) throws Exception {
		Optional<Cliente> existingCliente = clienteRepository.findByCodiceFiscalePIVA(cliente.getCodiceFiscalePIVA());
		if (cliente.getId() == null && existingCliente != null && existingCliente.isPresent()) {
			throw new BadRequestException("Esiste già un cliente con lo stesso codice fiscale/partita IVA");
		}
		return clienteRepository.save(cliente);
	}

	public void delete(Integer id) throws Exception {
		Optional<Cliente> existingCliente = clienteRepository.findById(id);
		if (existingCliente == null || !existingCliente.isPresent()) {
			throw new ResourceNotFoundException("Non esiste un cliente con l'id specificato");
		}
		clienteRepository.deleteById(id);
	}

	public Cliente getById(Integer id) throws Exception {
		Optional<Cliente> existingCliente = clienteRepository.findById(id);
		if (existingCliente == null || !existingCliente.isPresent()) {
			throw new ResourceNotFoundException("Non esiste un cliente con l'id specificato");
		}
		return existingCliente.get();
	}
}
