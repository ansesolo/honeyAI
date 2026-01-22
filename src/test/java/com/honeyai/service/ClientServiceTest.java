package com.honeyai.service;

import com.honeyai.exception.ClientNotFoundException;
import com.honeyai.model.Client;
import com.honeyai.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client activeClient;

    @BeforeEach
    void setUp() {
        activeClient = Client.builder()
                .id(1L)
                .name("Jean Dupont")
                .phone("0612345678")
                .email("jean@example.com")
                .build();
    }

    @Test
    void findAllActive_shouldReturnOnlyNonDeletedClients() {
        // Given
        List<Client> activeClients = Collections.singletonList(activeClient);
        when(clientRepository.findByDeletedAtIsNullOrderByNameAsc()).thenReturn(activeClients);

        // When
        List<Client> result = clientService.findAllActive();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getName()).isEqualTo("Jean Dupont");
        verify(clientRepository).findByDeletedAtIsNullOrderByNameAsc();
    }

    @Test
    void findAllActive_shouldReturnEmptyList_whenNoActiveClients() {
        // Given
        when(clientRepository.findByDeletedAtIsNullOrderByNameAsc()).thenReturn(Collections.emptyList());

        // When
        List<Client> result = clientService.findAllActive();

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findById_shouldReturnClient_whenExistsAndNotDeleted() {
        // Given
        when(clientRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(activeClient));

        // When
        Optional<Client> result = clientService.findById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Jean Dupont");
        verify(clientRepository).findByIdAndDeletedAtIsNull(1L);
    }

    @Test
    void findById_shouldReturnEmpty_whenClientNotFound() {
        // Given
        when(clientRepository.findByIdAndDeletedAtIsNull(99L)).thenReturn(Optional.empty());

        // When
        Optional<Client> result = clientService.findById(99L);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByIdOrThrow_shouldReturnClient_whenExists() {
        // Given
        when(clientRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(activeClient));

        // When
        Client result = clientService.findByIdOrThrow(1L);

        // Then
        assertThat(result.getName()).isEqualTo("Jean Dupont");
    }

    @Test
    void findByIdOrThrow_shouldThrowException_whenClientNotFound() {
        // Given
        when(clientRepository.findByIdAndDeletedAtIsNull(99L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> clientService.findByIdOrThrow(99L))
                .isInstanceOf(ClientNotFoundException.class)
                .hasMessageContaining("n'existe pas");
    }

    @Test
    void save_shouldSetUpdatedAtAndPersist() {
        // Given
        Client newClient = Client.builder()
                .name("Nouveau Client")
                .build();
        when(clientRepository.save(any(Client.class))).thenAnswer(invocation -> {
            Client saved = invocation.getArgument(0);
            saved.setId(10L);
            return saved;
        });

        // When
        Client result = clientService.save(newClient);

        // Then
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(clientRepository).save(newClient);
    }

    @Test
    void save_shouldUpdateExistingClient() {
        // Given
        activeClient.setUpdatedAt(null);
        when(clientRepository.save(any(Client.class))).thenReturn(activeClient);

        // When
        Client result = clientService.save(activeClient);

        // Then
        assertThat(result.getUpdatedAt()).isNotNull();
        verify(clientRepository).save(activeClient);
    }

    @Test
    void softDelete_shouldSetDeletedAtAndNotCallDelete() {
        // Given
        when(clientRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(activeClient));
        when(clientRepository.save(any(Client.class))).thenReturn(activeClient);

        // When
        clientService.softDelete(1L);

        // Then
        ArgumentCaptor<Client> captor = ArgumentCaptor.forClass(Client.class);
        verify(clientRepository).save(captor.capture());
        assertThat(captor.getValue().getDeletedAt()).isNotNull();

        // Verify delete() was never called
        verify(clientRepository, never()).delete(any(Client.class));
        verify(clientRepository, never()).deleteById(anyLong());
    }

    @Test
    void softDelete_shouldThrowException_whenClientNotFound() {
        // Given
        when(clientRepository.findByIdAndDeletedAtIsNull(99L)).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> clientService.softDelete(99L))
                .isInstanceOf(ClientNotFoundException.class);

        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    void searchClients_shouldDelegateToRepository() {
        // Given
        List<Client> searchResults = Collections.singletonList(activeClient);
        when(clientRepository.searchClients("Jean")).thenReturn(searchResults);

        // When
        List<Client> result = clientService.searchClients("Jean");

        // Then
        assertThat(result).hasSize(1);
        verify(clientRepository).searchClients("Jean");
    }

    @Test
    void searchClients_shouldReturnAllActive_whenSearchIsNull() {
        // Given
        List<Client> allActive = Collections.singletonList(activeClient);
        when(clientRepository.findByDeletedAtIsNullOrderByNameAsc()).thenReturn(allActive);

        // When
        List<Client> result = clientService.searchClients(null);

        // Then
        assertThat(result).hasSize(1);
        verify(clientRepository).findByDeletedAtIsNullOrderByNameAsc();
        verify(clientRepository, never()).searchClients(any());
    }

    @Test
    void searchClients_shouldReturnAllActive_whenSearchIsEmpty() {
        // Given
        List<Client> allActive = Collections.singletonList(activeClient);
        when(clientRepository.findByDeletedAtIsNullOrderByNameAsc()).thenReturn(allActive);

        // When
        List<Client> result = clientService.searchClients("");

        // Then
        assertThat(result).hasSize(1);
        verify(clientRepository).findByDeletedAtIsNullOrderByNameAsc();
    }

    @Test
    void searchClients_shouldReturnAllActive_whenSearchIsWhitespace() {
        // Given
        List<Client> allActive = Collections.singletonList(activeClient);
        when(clientRepository.findByDeletedAtIsNullOrderByNameAsc()).thenReturn(allActive);

        // When
        List<Client> result = clientService.searchClients("   ");

        // Then
        assertThat(result).hasSize(1);
        verify(clientRepository).findByDeletedAtIsNullOrderByNameAsc();
    }

    @Test
    void searchClients_shouldTrimSearchTerm() {
        // Given
        when(clientRepository.searchClients("Jean")).thenReturn(Collections.singletonList(activeClient));

        // When
        clientService.searchClients("  Jean  ");

        // Then
        verify(clientRepository).searchClients("Jean");
    }
}
