package com.oleificiorenna.gestioneingressi.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllIngressiDto {
	
	private Integer idCliente;
	
	private LocalDateTime dateFrom;
	
	private LocalDateTime dateTo;

}
