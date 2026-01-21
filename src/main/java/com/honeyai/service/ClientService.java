package com.honeyai.service;

import com.honeyai.exception.ClientNotFoundException;
import com.honeyai.model.Client;
import com.honeyai.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Find all non-deleted clients ordered by name.
     */
    @Transactional(readOnly = true)
    public List<Client> findAllActive() {
        return clientRepository.findByDeletedAtIsNullOrderByNameAsc();
    }

    /**
     * Find a non-deleted client by ID.
     * @param id client ID
     * @return Optional containing the client if found and not deleted
     */
    @Transactional(readOnly = true)
    public Optional<Client> findById(Long id) {
        return clientRepository.findByIdAndDeletedAtIsNull(id);
    }

    /**
     * Find a client by ID or throw exception.
     * @param id client ID
     * @return the client
     * @throws ClientNotFoundException if client not found or soft-deleted
     */
    @Transactional(readOnly = true)
    public Client findByIdOrThrow(Long id) {
        return clientRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    /**
     * Save a client. Sets updatedAt timestamp.
     * @param client the client to save
     * @return the saved client
     */
    public Client save(Client client) {
        client.setUpdatedAt(LocalDateTime.now());
        log.info("Saving client: {}", client.getName());
        return clientRepository.save(client);
    }

    /**
     * Soft delete a client by setting deletedAt timestamp.
     * Does not physically delete the record.
     * @param id client ID
     * @throws ClientNotFoundException if client not found or already deleted
     */
    public void softDelete(Long id) {
        Client client = findByIdOrThrow(id);
        client.setDeletedAt(LocalDateTime.now());
        clientRepository.save(client);
        log.info("Soft deleted client: {} (id={})", client.getName(), id);
    }

    /**
     * Search clients by name or phone number.
     * Handles null or empty search strings gracefully by returning all active clients.
     * @param search search term
     * @return list of matching clients
     */
    @Transactional(readOnly = true)
    public List<Client> searchClients(String search) {
        if (search == null || search.trim().isEmpty()) {
            return findAllActive();
        }
        return clientRepository.searchClients(search.trim());
    }
}
