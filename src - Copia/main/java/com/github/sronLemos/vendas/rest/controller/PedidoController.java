package com.github.sronLemos.vendas.rest.controller;

import com.github.sronLemos.vendas.domain.entity.ItemPedido;
import com.github.sronLemos.vendas.domain.entity.Pedido;
import com.github.sronLemos.vendas.domain.enums.StatusPedido;
import com.github.sronLemos.vendas.rest.dto.AtualizacaoStatusPedidoDTO;
import com.github.sronLemos.vendas.rest.dto.InformacaoItemPedidoDTO;
import com.github.sronLemos.vendas.rest.dto.InformacoesPedidoDTO;
import com.github.sronLemos.vendas.rest.dto.PedidoDTO;
import com.github.sronLemos.vendas.service.PedidoService;

import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/{id}")
    public InformacoesPedidoDTO findById(@PathVariable("id") Integer id) {

        return pedidoService.obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Produto não encontrado"));

    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id,
                             @RequestBody AtualizacaoStatusPedidoDTO dto) {


        pedidoService.atualizarStatus(id, StatusPedido.valueOf(dto.getNovoStatus()));

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = pedidoService.save(dto);
        return pedido.getId();
    }

    private InformacoesPedidoDTO converter(Pedido pedido) {
        InformacoesPedidoDTO dto = new InformacoesPedidoDTO();
        dto.setCodigo(pedido.getId());
        dto.setDataPedido(pedido.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dto.setCpf(pedido.getCliente().getCpf());
        dto.setNomeCliente(pedido.getCliente().getNome());
        dto.setTotal(pedido.getTotal());
        dto.setStatus(pedido.getStatus().name());
        dto.setItens(converter(pedido.getItens()));
        return dto;
    }

    private List<InformacaoItemPedidoDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream().map(itemPedido -> {
            InformacaoItemPedidoDTO informacao = new InformacaoItemPedidoDTO();
            informacao.setDescricaoProduto(itemPedido.getProduto().getDescricao());
            informacao.setQuantidade(itemPedido.getQuantidade());
            informacao.setPrecoUnitario(itemPedido.getProduto().getPreco());
            return informacao;
        }).collect(Collectors.toList());
    }

}
