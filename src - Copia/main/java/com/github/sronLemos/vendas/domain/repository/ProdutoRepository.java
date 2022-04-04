package com.github.sronLemos.vendas.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sronLemos.vendas.domain.entity.Cliente;
import com.github.sronLemos.vendas.domain.entity.Produto;

import java.util.List;


public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Cliente> findByDescricaoLike(String descricao);

}
