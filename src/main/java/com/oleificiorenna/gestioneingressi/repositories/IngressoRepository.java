package com.oleificiorenna.gestioneingressi.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Integer> {

    // Recupera tutti gli ingressi di un cliente
    List<Ingresso> findByCliente(Cliente cliente);

    // Recupera tutti gli ingressi in ordine di ingresso
    List<Ingresso> findAllByOrderByDataOraIngressoDesc();
}
