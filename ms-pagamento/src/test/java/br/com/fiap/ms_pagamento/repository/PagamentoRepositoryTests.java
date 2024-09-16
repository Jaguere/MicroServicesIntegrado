package br.com.fiap.ms_pagamento.repository;

import br.com.fiap.ms_pagamento.model.Pagamento;
import br.com.fiap.ms_pagamento.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PagamentoRepositoryTests {

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalPagamento;

    @BeforeEach
    void setup() throws Exception {
        existingId = 1L;
        nonExistingId = 100L;
        countTotalPagamento = 3L;
    }

    @Autowired
    private PagamentoRepository repository;

    @Test
    @DisplayName("Deletar normalmente quando existe o ID")
    public void deleteWhenExists() {
        repository.deleteById(existingId);

        Optional<Pagamento> result = repository.findById(existingId);

        Assertions.assertFalse(result.isPresent());

    }
//    @Test
//    public void deleteThrowExceptionWhenDoesNotExist(){
//        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
//           repository.deleteById(nonExistingId);
//        });
//    }
    @Test
    @DisplayName("Quando id for nulo deve salvar com incremento automatico")
    public void saveWhenIdNull(){
        Pagamento pagamento = Factory.createPagamento();
        pagamento.setId(null);
        pagamento = repository.save(pagamento);
        Assertions.assertNotNull(pagamento.getId());
        Assertions.assertEquals(countTotalPagamento + 1, pagamento.getId());
    }
    @Test
    @DisplayName("Achar entidade quando ID valido")
    public void findByIdWhenValid(){
        Optional<Pagamento> result = repository.findById(existingId);
        Assertions.assertTrue(result.isPresent());
    }
    @Test
    @DisplayName("Não achar entidade quando ID inválido")
    public void findByIdWhenInvalid(){
        Optional<Pagamento> result = repository.findById(nonExistingId);
        Assertions.assertFalse(result.isPresent());
    }
}
