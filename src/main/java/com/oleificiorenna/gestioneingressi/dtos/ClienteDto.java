package com.oleificiorenna.gestioneingressi.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDto {
	
    private Integer id;

    private String codiceFiscalePIVA;

    private String cognome;

    private String nome;

    private String nomeAzienda;
    
    private double monteOre;
}
