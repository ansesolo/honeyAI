package com.honeyai.repository;

import com.honeyai.model.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    private Client client1;
    private Client client2;
    private Client client3;

    @BeforeEach
    void setUp() {
        clientRepository.deleteAll();

        client1 = Client.builder()
                .name("Dupont Jean")
                .phone("0612345678")
                .email("jean.dupont@email.com")
                .address("123 Rue de Paris")
                .notes("Client fidèle")
                .build();

        client2 = Client.builder()
                .name("Martin Marie")
                .phone("0698765432")
                .email("marie.martin@email.com")
                .address("456 Avenue de Lyon")
                .build();

        client3 = Client.builder()
                .name("Bernard Pierre")
                .phone("0611223344")
                .email("pierre.bernard@email.com")
                .build();
    }

    @Test
    void save_shouldPersistClientWithGeneratedId() {
        // When
        Client saved = clientRepository.save(client1);

        // Then
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Dupont Jean");
        assertThat(saved.getPhone()).isEqualTo("0612345678");
    }

    @Test
    void findById_shouldReturnClient_whenExists() {
        // Given
        Client saved = clientRepository.save(client1);

        // When
        Optional<Client> found = clientRepository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Dupont Jean");
    }

    @Test
    void findByDeletedAtIsNullOrderByNameAsc_shouldReturnOnlyNonDeletedClients() {
        // Given
        clientRepository.save(client1); // Dupont
        clientRepository.save(client2); // Martin
        Client deletedClient = clientRepository.save(client3); // Bernard
        deletedClient.setDeletedAt(LocalDateTime.now());
        clientRepository.save(deletedClient);

        // When
        List<Client> activeClients = clientRepository.findByDeletedAtIsNullOrderByNameAsc();

        // Then
        assertThat(activeClients).hasSize(2);
        assertThat(activeClients.getFirst().getName()).isEqualTo("Dupont Jean");
        assertThat(activeClients.get(1).getName()).isEqualTo("Martin Marie");
    }

    @Test
    void findByIdAndDeletedAtIsNull_shouldReturnEmpty_whenClientIsDeleted() {
        // Given
        Client saved = clientRepository.save(client1);
        saved.setDeletedAt(LocalDateTime.now());
        clientRepository.save(saved);

        // When
        Optional<Client> found = clientRepository.findByIdAndDeletedAtIsNull(saved.getId());

        // Then
        assertThat(found).isEmpty();
    }

    @Test
    void findByIdAndDeletedAtIsNull_shouldReturnClient_whenNotDeleted() {
        // Given
        Client saved = clientRepository.save(client1);

        // When
        Optional<Client> found = clientRepository.findByIdAndDeletedAtIsNull(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Dupont Jean");
    }

    @Test
    void softDelete_shouldSetDeletedAtAndKeepRecordInDatabase() {
        // Given
        Client saved = clientRepository.save(client1);
        Long clientId = saved.getId();

        // When - Soft delete
        saved.setDeletedAt(LocalDateTime.now());
        clientRepository.save(saved);

        // Then - Record still exists in DB
        Optional<Client> stillInDb = clientRepository.findById(clientId);
        assertThat(stillInDb).isPresent();
        assertThat(stillInDb.get().isDeleted()).isTrue();

        // But not returned by active query
        Optional<Client> notActive = clientRepository.findByIdAndDeletedAtIsNull(clientId);
        assertThat(notActive).isEmpty();
    }

    @Test
    void searchClients_shouldReturnMatchingClientsByName() {
        // Given
        clientRepository.save(client1); // Dupont
        clientRepository.save(client2); // Martin
        clientRepository.save(client3); // Bernard

        // When
        List<Client> results = clientRepository.searchClients("dupont");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getName()).isEqualTo("Dupont Jean");
    }

    @Test
    void searchClients_shouldReturnMatchingClientsByPhone() {
        // Given
        clientRepository.save(client1);
        clientRepository.save(client2);
        clientRepository.save(client3);

        // When
        List<Client> results = clientRepository.searchClients("0698");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getName()).isEqualTo("Martin Marie");
    }

    @Test
    void searchClients_shouldNotReturnDeletedClients() {
        // Given
        Client saved = clientRepository.save(client1);
        saved.setDeletedAt(LocalDateTime.now());
        clientRepository.save(saved);

        // When
        List<Client> results = clientRepository.searchClients("Dupont");

        // Then
        assertThat(results).isEmpty();
    }

    @Test
    void searchClients_shouldBeCaseInsensitiveForName() {
        // Given
        clientRepository.save(client1);

        // When
        List<Client> results = clientRepository.searchClients("DUPONT");

        // Then
        assertThat(results).hasSize(1);
    }

    @Test
    void isDeleted_shouldReturnFalse_whenDeletedAtIsNull() {
        // Given
        Client client = new Client();
        client.setDeletedAt(null);

        // Then
        assertThat(client.isDeleted()).isFalse();
    }

    @Test
    void isDeleted_shouldReturnTrue_whenDeletedAtIsSet() {
        // Given
        Client client = new Client();
        client.setDeletedAt(LocalDateTime.now());

        // Then
        assertThat(client.isDeleted()).isTrue();
    }

    @Test
    void saveMultipleClients_shouldSucceed() {
        // When - AC7: At least 3 test clients inserted successfully
        Client saved1 = clientRepository.save(client1);
        Client saved2 = clientRepository.save(client2);
        Client saved3 = clientRepository.save(client3);

        // Then
        assertThat(saved1.getId()).isNotNull();
        assertThat(saved2.getId()).isNotNull();
        assertThat(saved3.getId()).isNotNull();

        List<Client> all = clientRepository.findAll();
        assertThat(all).hasSize(3);
    }
}
