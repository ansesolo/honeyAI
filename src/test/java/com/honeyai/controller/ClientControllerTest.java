package com.honeyai.controller;

import com.honeyai.exception.ClientNotFoundException;
import com.honeyai.model.Client;
import com.honeyai.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private Client existingClient;

    @BeforeEach
    void setUp() {
        existingClient = Client.builder()
                .id(1L)
                .name("Jean Dupont")
                .phone("0612345678")
                .email("jean@example.com")
                .build();
    }

    @Test
    void createForm_shouldReturnFormViewWithEmptyClient() throws Exception {
        mockMvc.perform(get("/clients/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/form"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("activeMenu", "clients"));
    }

    @Test
    void editForm_shouldReturnFormViewWithExistingClient() throws Exception {
        when(clientService.findByIdOrThrow(1L)).thenReturn(existingClient);

        mockMvc.perform(get("/clients/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/form"))
                .andExpect(model().attributeExists("client"))
                .andExpect(model().attribute("activeMenu", "clients"));

        verify(clientService).findByIdOrThrow(1L);
    }

    @Test
    void editForm_shouldReturn404_whenClientNotFound() throws Exception {
        when(clientService.findByIdOrThrow(99L)).thenThrow(new ClientNotFoundException(99L));

        mockMvc.perform(get("/clients/99/edit"))
                .andExpect(status().isNotFound());
    }

    @Test
    void save_shouldRedirectToList_whenValidClient() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(existingClient);

        mockMvc.perform(post("/clients")
                        .param("name", "Jean Dupont")
                        .param("phone", "0612345678")
                        .param("email", "jean@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(clientService).save(any(Client.class));
    }

    @Test
    void save_shouldReturnForm_whenValidationFails() throws Exception {
        mockMvc.perform(post("/clients")
                        .param("name", "")
                        .param("phone", "0612345678"))
                .andExpect(status().isOk())
                .andExpect(view().name("clients/form"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("client", "name"));

        verify(clientService, never()).save(any(Client.class));
    }

    @Test
    void save_shouldUpdateExistingClient_whenIdProvided() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(existingClient);

        mockMvc.perform(post("/clients")
                        .param("id", "1")
                        .param("name", "Jean Dupont Updated")
                        .param("phone", "0612345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));

        verify(clientService).save(any(Client.class));
    }

    @Test
    void save_shouldAcceptFlexiblePhoneFormat() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(existingClient);

        mockMvc.perform(post("/clients")
                        .param("name", "Test Client")
                        .param("phone", "+33 6 12 34 56 78"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));
    }

    @Test
    void save_shouldAcceptInternationalPhoneFormat() throws Exception {
        when(clientService.save(any(Client.class))).thenReturn(existingClient);

        mockMvc.perform(post("/clients")
                        .param("name", "Test Client")
                        .param("phone", "0033612345678"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));
    }

    @Test
    void delete_shouldRedirectToList_withSuccessMessage() throws Exception {
        when(clientService.findByIdOrThrow(1L)).thenReturn(existingClient);

        mockMvc.perform(post("/clients/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(clientService).softDelete(1L);
    }

    @Test
    void delete_shouldReturn404_whenClientNotFound() throws Exception {
        when(clientService.findByIdOrThrow(99L)).thenThrow(new ClientNotFoundException(99L));

        mockMvc.perform(post("/clients/99/delete"))
                .andExpect(status().isNotFound());

        verify(clientService, never()).softDelete(anyLong());
    }

    @Test
    void detail_shouldReturn404_whenClientNotFound() throws Exception {
        when(clientService.findByIdOrThrow(99L)).thenThrow(new ClientNotFoundException(99L));

        mockMvc.perform(get("/clients/99"))
                .andExpect(status().isNotFound());
    }
}
