package com.example.estoque.service;

import com.example.estoque.domain.Produto;
import com.example.estoque.entity.ProdutoEntity;
import com.example.estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith ( MockitoExtension.class )

class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    private Produto produtoDomain;
    private ProdutoEntity produtoEntity;

    @BeforeEach
    void setUp() {
        produtoDomain = new Produto ( "Produto Teste" , "Descricao Teste" , 10.0 , 100 );
        produtoEntity = new ProdutoEntity ( produtoDomain );
    }

    @Test
    void cadastrarProdutoExistente ( ) {
        when ( produtoRepository.findByNome ( produtoDomain.getNome ( ) ) ).thenReturn ( produtoEntity );

        produtoService.cadastrarProduto ( produtoDomain );

        ArgumentCaptor<ProdutoEntity> produtoEntityCaptor = ArgumentCaptor.forClass ( ProdutoEntity.class );
        verify ( produtoRepository , times ( 1 ) ).save ( produtoEntityCaptor.capture ( ) );

        ProdutoEntity produtoEntitySalvo = produtoEntityCaptor.getValue ( );
        assertEquals ( produtoDomain.getNome ( ) , produtoEntitySalvo.getNome ( ) );
        assertEquals ( produtoDomain.getDescricao ( ) , produtoEntitySalvo.getDescricao ( ) );
        assertEquals ( produtoDomain.getPreco ( ) , produtoEntitySalvo.getPreco ( ) );
        assertEquals ( produtoDomain.getQtd ( ) , produtoEntitySalvo.getQtd ( ) );
    }

    @Test
    void cadastrarProdutoNovoProduto ( ) {
        when ( produtoRepository.findByNome ( produtoDomain.getNome ( ) ) ).thenReturn ( null );

        produtoService.cadastrarProduto ( produtoDomain );

        ArgumentCaptor<ProdutoEntity> produtoEntityCaptor = ArgumentCaptor.forClass ( ProdutoEntity.class );
        verify ( produtoRepository , times ( 1 ) ).save ( produtoEntityCaptor.capture ( ) );

        ProdutoEntity produtoEntitySalvo = produtoEntityCaptor.getValue ( );
        assertEquals ( produtoDomain.getNome ( ) , produtoEntitySalvo.getNome ( ) );
        assertEquals ( produtoDomain.getDescricao ( ) , produtoEntitySalvo.getDescricao ( ) );
        assertEquals ( produtoDomain.getPreco ( ) , produtoEntitySalvo.getPreco ( ) );
        assertEquals ( produtoDomain.getQtd ( ) , produtoEntitySalvo.getQtd ( ) );
    }

    @Test
    void encontrarTodos ( ) {
        when ( produtoRepository.findAll ( ) ).thenReturn ( Arrays.asList ( produtoEntity ) );

        List<Produto> produtos = produtoService.encontrarTodos ( );

        assertEquals ( 1 , produtos.size ( ) );
        assertEquals ( produtoDomain.getNome ( ) , produtos.get ( 0 ).getNome ( ) );
        assertEquals ( produtoDomain.getDescricao ( ) , produtos.get ( 0 ).getDescricao ( ) );
        assertEquals ( produtoDomain.getPreco ( ) , produtos.get ( 0 ).getPreco ( ) );
        assertEquals ( produtoDomain.getQtd ( ) , produtos.get ( 0 ).getQtd ( ) );
    }

    @Test
    void encontrarPorNome_produtoEncontrado () {
        when(produtoRepository.findByNome("Produto Teste")).thenReturn(produtoEntity);

        Produto produtoEncontrado = produtoService.encontrarPorNome("Produto Teste");

        assertNotNull(produtoEncontrado);
        assertEquals(produtoDomain.getNome(), produtoEncontrado.getNome());
        assertEquals(produtoDomain.getDescricao(), produtoEncontrado.getDescricao());
        assertEquals(produtoDomain.getPreco(), produtoEncontrado.getPreco());
        assertEquals(produtoDomain.getQtd(), produtoEncontrado.getQtd());

        verify(produtoRepository, times(1)).findByNome("Produto Teste");
    }
}