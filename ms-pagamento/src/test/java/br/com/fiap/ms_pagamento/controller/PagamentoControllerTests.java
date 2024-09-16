package br.com.fiap.ms_pagamento.controller;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.service.PagamentoService;
import br.com.fiap.ms_pagamento.service.exception.ResourceNotFoundException;
import br.com.fiap.ms_pagamento.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.bind.annotation.PostMapping;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(PagamentoController.class)
public class PagamentoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagamentoService service;
    private PagamentoDTO pagamentoDTO;
    private Long existingId;
    private Long nonExistingId;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        pagamentoDTO = Factory.createPagamentoDTO();
        List<PagamentoDTO> list = List.of(pagamentoDTO);
        existingId = 1L;
        nonExistingId = 10L;

        when(service.findAll()).thenReturn(list);
        when(service.findById(existingId)).thenReturn(pagamentoDTO);
        when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
        when(service.insert(any())).thenReturn(pagamentoDTO);
        when(service.update(eq(existingId),any())).thenReturn(pagamentoDTO);
        when(service.update(eq(nonExistingId),any())).thenThrow(ResourceNotFoundException.class);
        doNothing().when(service).delete(existingId);
        doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
    }

    @Test
    public void findAllShouldReturnList() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos")
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnDTOWhenValid() throws Exception {
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.valor").exists());
        result.andExpect(jsonPath("$.status").exists());
    }
    @Test
    public void findByIdShouldReturnNotFoundWhenInvalid() throws Exception{
        ResultActions result = mockMvc.perform(get("/pagamentos/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }
    @Test
    public void insertShouldReturnDTO() throws Exception{
        PagamentoDTO newDTO = Factory.createNewPagamentoDTO();
        String jsonBody = objectMapper.writeValueAsString(newDTO);

        mockMvc.perform(post("/pagamentos")
                    .content(jsonBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void updateShouldReturnDTOWhenValid() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(pagamentoDTO);

        mockMvc.perform(put("/pagamentos/{id}", existingId)
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.valor").exists())
                .andExpect(jsonPath("$.status").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenInvalid() throws Exception{

        String jsonBody = objectMapper.writeValueAsString(pagamentoDTO);

        mockMvc.perform(put("/pagamentos{id}",nonExistingId)
                    .content(jsonBody)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void deleteShouldReturnNoContentWhenValid() throws Exception{
        mockMvc.perform(delete("/pagamentos/{id}",existingId)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNoContent());
    }
    @Test
    public void deleteShouldReturnNotFoundWhenInvalid() throws Exception {
        mockMvc.perform(delete("/pagamentos/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON)).andDo(print())
                .andExpect(status().isNotFound());
    }
}
