package com.github.sronLemos.vendas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sronLemos.vendas.domain.entity.ItemPedido;


public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

}
