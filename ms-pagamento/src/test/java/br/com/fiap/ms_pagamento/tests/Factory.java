package br.com.fiap.ms_pagamento.tests;

import br.com.fiap.ms_pagamento.dto.PagamentoDTO;
import br.com.fiap.ms_pagamento.model.Pagamento;
import br.com.fiap.ms_pagamento.model.Status;

import java.math.BigDecimal;

public class Factory {
    public static Pagamento createPagamento(){
        Pagamento pag = new Pagamento(1L, BigDecimal.valueOf(32.25),
                "Bill", "8294758501823748","07/28",
                "585", Status.CRIADO,
                1L, 2L);
        return pag;
    }

    public static PagamentoDTO createPagamentoDTO(){
        Pagamento pagamento = createPagamento();
        return new PagamentoDTO(pagamento);
    }
    public static PagamentoDTO createNewPagamentoDTO(){
        Pagamento pagamento = createPagamento();
        pagamento.setId(null);
        return new PagamentoDTO(pagamento);
    }
}
