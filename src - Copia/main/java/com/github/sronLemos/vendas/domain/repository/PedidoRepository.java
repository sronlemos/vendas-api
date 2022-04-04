package com.github.sronLemos.vendas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.sronLemos.vendas.domain.entity.Cliente;
import com.github.sronLemos.vendas.domain.entity.Pedido;

import java.util.List;
import java.util.Optional;


public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByCliente(Cliente cliente);

    @Query("select p from Pedido p left join fetch p.itens where p.id = :id")
    Optional<Pedido> getByIdFetchItens(Integer id);

}
