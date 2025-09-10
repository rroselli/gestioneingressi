package com.oleificiorenna.gestioneingressi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oleificiorenna.gestioneingressi.entities.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByCodiceFiscalePIVA(String codiceFiscalePIVA);

}