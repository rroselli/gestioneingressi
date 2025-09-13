package com.oleificiorenna.gestioneingressi.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oleificiorenna.gestioneingressi.entities.Cliente;
import com.oleificiorenna.gestioneingressi.entities.Ingresso;

@Repository
public interface IngressoRepository extends JpaRepository<Ingresso, Integer> {

	// Recupera tutti gli ingressi di un cliente
	List<Ingresso> findByCliente(Cliente cliente);

	// Recupera tutti gli ingressi in ordine di ingresso
	List<Ingresso> findAllByOrderByDataOraIngressoDesc();

	@Query(value = "SELECT * FROM ingresso i " + "WHERE i.data_ora_ingresso <= :fine "
			+ "AND i.data_ora_uscita >= :inizio", nativeQuery = true)
	List<Ingresso> findIngressiInIntervallo(@Param("inizio") LocalDateTime inizio, @Param("fine") LocalDateTime fine);
}
