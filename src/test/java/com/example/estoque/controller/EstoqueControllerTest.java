package com.example.estoque.controller;

import com.example.estoque.domain.ItemPedido;
import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.service.ProdutoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EstoqueControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private EstoqueController estoqueController;

    private Produto produto1;
    private Produto produto2;
    private Pedido pedidoValido;
    private Pedido pedidoInvalido;

    @BeforeEach
    void setUp() {
        produto1 = new Produto("ProdutoA", "Descrição do Produto A", 100.00, 10);
        produto2 = new Produto("ProdutoB", "Descrição do Produto B", 50.0, 5);

        ItemPedido itemValido = new ItemPedido( );
        pedidoValido = new Pedido();
        pedidoValido.setItens(Collections.singletonList(itemValido));

        ItemPedido itemInvalido = new ItemPedido( );
        pedidoInvalido = new Pedido();
        pedidoInvalido.setItens(Collections.singletonList(itemInvalido));
    }

    @Test
    void cadastraProduto_DeveRetornarOkComMensagemDeSucesso() {
        doNothing().when(produtoService).cadastrarProduto(produto1);

        ResponseEntity<String> response = estoqueController.cadastraProduto(produto1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cadastrado com Sucesso", response.getBody());
        verify(produtoService, times(1)).cadastrarProduto(produto1);
    }

    @Test
    void listarProdutos_DeveRetornarOkComListaDeProdutos() {
        List<Produto> produtos = Arrays.asList(produto1, produto2);
        when(produtoService.encontrarTodos()).thenReturn(produtos);

        ResponseEntity<List<Produto>> response = estoqueController.listarProdutos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtos, response.getBody());
        verify(produtoService, times(1)).encontrarTodos();
    }

    @Test
    void listarProdutos_QuandoNaoHaProdutos_DeveRetornarOkComListaVazia() {

        when(produtoService.encontrarTodos()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Produto>> response = estoqueController.listarProdutos();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Collections.emptyList(), response.getBody());
        verify(produtoService, times(1)).encontrarTodos();
    }

    @Test
    void buscaProduto_QuandoProdutoExiste_DeveRetornarOkComProduto() {

        String nomeProduto = "ProdutoA";
        when(produtoService.encontrarPorNome(nomeProduto)).thenReturn(produto1);

        ResponseEntity<Produto> response = estoqueController.buscaProduto(nomeProduto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produto1, response.getBody());
        verify(produtoService, times(1)).encontrarPorNome(nomeProduto);
    }

    @Test
    void buscaProduto_QuandoProdutoNaoExiste_DeveRetornarOkComNullBody() {

        String nomeProdutoNaoExistente = "ProdutoC";
        when(produtoService.encontrarPorNome(nomeProdutoNaoExistente)).thenReturn(null);

        ResponseEntity<Produto> response = estoqueController.buscaProduto(nomeProdutoNaoExistente);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody()); // Controller returns OK with null body if service returns null
        verify(produtoService, times(1)).encontrarPorNome(nomeProdutoNaoExistente);
    }

    @Test
    void atualizarEstoque_QuandoEstoqueSuficiente_DeveRetornarOkComMensagemDeSucesso() throws ForaDeEstoqueException {

        doNothing().when(produtoService).atualizarEstoque(pedidoValido);

        ResponseEntity<String> response = estoqueController.atualizarEstoque(pedidoValido);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Estoque Atualizado", response.getBody());
        verify(produtoService, times(1)).atualizarEstoque(pedidoValido);
    }

    @Test
    void atualizarEstoque_QuandoForaDeEstoque_DeveRetornarBadRequestComMensagemDeErro() throws ForaDeEstoqueException {

        String mensagemErro = "Quantidade solicitada indisponível para o produto: ProdutoA";
        doThrow(new ForaDeEstoqueException(mensagemErro)).when(produtoService).atualizarEstoque(pedidoInvalido);

        ResponseEntity<String> response = estoqueController.atualizarEstoque(pedidoInvalido);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(mensagemErro, response.getBody());
        verify(produtoService, times(1)).atualizarEstoque(pedidoInvalido);
    }
}