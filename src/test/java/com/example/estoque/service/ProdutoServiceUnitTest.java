package com.example.estoque.service;

import com.example.estoque.domain.Pedido;
import com.example.estoque.domain.Produto;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.exception.ForaDeEstoqueException;
import com.example.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceUnitTest {

    @Mock
    private ProdutoRepository repository;

    @InjectMocks
    private ProdutoService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks ( this );
    }

    @Test
    void testCadastrarProduto_NovoProduto() {
        Produto produto = new Produto("");
        when(repository.findByNome(produto.getNome())).thenReturn(null);

        service.cadastrarProduto(produto);

        verify(repository, times(1)).save(any(ProdutoEntity.class));
    }

}
