package com.github.sronLemos.vendas.rest.dto;

import javax.validation.constraints.NotNull;

import com.github.sronLemos.vendas.validation.NotEmptyList;

import java.math.BigDecimal;
import java.util.List;

public class PedidoDTO {

    @NotNull(message = "Informe o código do cliente")
    private Integer cliente;

    @NotNull(message = "Informe o total do pedido")
    private BigDecimal total;

    @NotEmptyList(message = "Pedido não pode ser realizado sem itens")
    private List<ItemPedidoDTO> itens;

    public Integer getCliente() {
        return cliente;
    }

    public void setCliente(Integer cliente) {
        this.cliente = cliente;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<ItemPedidoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoDTO> itens) {
        this.itens = itens;
    }
}
