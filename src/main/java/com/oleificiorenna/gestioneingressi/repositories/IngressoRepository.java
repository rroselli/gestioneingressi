package com.oleificiorenna.gestioneingressi.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Integer> {

    // Recupera tutti gli ingressi di un cliente
    List<Ingresso> findByCliente(Cliente cliente);

    // Recupera tutti gli ingressi in ordine di ingresso
    List<Ingresso> findAllByOrderByDataOraIngressoDesc();
    
    @Query("""
            SELECT i FROM Ingresso i
            WHERE (:inizio IS NULL OR i.dataOraUscita >= :inizio)
              AND (:fine IS NULL OR i.dataOraIngresso <= :fine)
        """)
    List<Ingresso> findIngressiInIntervallo(LocalDateTime inizio, LocalDateTime fine);
}
