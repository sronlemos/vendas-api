package com.github.sronLemos.vendas.service;

import java.util.Optional;

import org.springframework.data.repository.query.Param;

import com.github.sronLemos.vendas.domain.entity.Pedido;
import com.github.sronLemos.vendas.domain.enums.StatusPedido;
import com.github.sronLemos.vendas.rest.dto.PedidoDTO;

public interface PedidoService {

    Pedido save(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(@Param("id") Integer id);

    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
