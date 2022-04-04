package com.github.sronLemos.vendas.service.impl;

import com.github.sronLemos.vendas.domain.entity.ItemPedido;
import com.github.sronLemos.vendas.domain.entity.Pedido;
import com.github.sronLemos.vendas.domain.enums.StatusPedido;
import com.github.sronLemos.vendas.domain.repository.ClienteRepository;
import com.github.sronLemos.vendas.domain.repository.ItemPedidoRepository;
import com.github.sronLemos.vendas.domain.repository.PedidoRepository;
import com.github.sronLemos.vendas.domain.repository.ProdutoRepository;
import com.github.sronLemos.vendas.exception.PedidoNaoEncontradoException;
import com.github.sronLemos.vendas.exception.RegraNegocioException;
import com.github.sronLemos.vendas.rest.dto.ItemPedidoDTO;
import com.github.sronLemos.vendas.rest.dto.PedidoDTO;
import com.github.sronLemos.vendas.service.PedidoService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    private PedidoRepository pedidoRepository;
    private ClienteRepository clienteRepository;
    private ProdutoRepository produtoRepository;
    private ItemPedidoRepository itemPedidoRepository;

    public PedidoServiceImpl(PedidoRepository pedidoRepository, ClienteRepository clienteRepository, ProdutoRepository produtoRepository, ItemPedidoRepository itemPedidoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoRepository = produtoRepository;
        this.itemPedidoRepository = itemPedidoRepository;
    }

    @Override
    @Transactional
    public Pedido save(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        pedido.setCliente(
                clienteRepository.findById(dto.getCliente())
                        .orElseThrow(() -> new RegraNegocioException("Código de cliente inválido."))
        );
        pedido.setDataPedido(LocalDate.now());
        pedido.setTotal(dto.getTotal());
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itemPedidos = converterItens(pedido, dto.getItens());
        pedidoRepository.save(pedido);
        itemPedidoRepository.saveAll(itemPedidos);
        pedido.setItens(itemPedidos);


        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return pedidoRepository.getByIdFetchItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        pedidoRepository.findById(id).map(p -> {
           p.setStatus(statusPedido);
            return pedidoRepository.save(p);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());

    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itensDto) {
        if (CollectionUtils.isEmpty(itensDto)) {
            throw new RegraNegocioException("Não é possível realizar um pedido sem itens.");
        }

        return itensDto.stream().map(itemPedidoDTO -> {
            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(itemPedidoDTO.getQuantidade());
            itemPedido.setPedido(pedido);
            itemPedido.setProduto(
                    produtoRepository.findById(itemPedidoDTO.getProduto())
                            .orElseThrow(() -> new RegraNegocioException("Código de produto inválido."))
            );
            return itemPedido;
        }).collect(Collectors.toList());
    }
}
