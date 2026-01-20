package com.honeyai.repository;

import com.honeyai.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Find all non-deleted clients ordered by name.
     */
    List<Client> findByDeletedAtIsNullOrderByNameAsc();

    /**
     * Find a non-deleted client by ID.
     */
    Optional<Client> findByIdAndDeletedAtIsNull(Long id);

    /**
     * Search non-deleted clients by name or telephone (partial match).
     */
    @Query("SELECT c FROM Client c WHERE c.deletedAt IS NULL " +
           "AND (LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR c.phone LIKE CONCAT('%', :search, '%'))")
    List<Client> searchClients(@Param("search") String search);
}
