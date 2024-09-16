package br.com.fiap.ms_pagamento.service;

import br.com.fiap.ms_pagamento.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class PagamentoServiceTests {
    @InjectMocks
    private PagamentoService service;

    @Mock
    private PagamentoRepository repository;

    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 10L;
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.doNothing().when(repository).deleteById(existingId);
    }
}
