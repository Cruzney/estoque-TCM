package com.example.estoque.repository;

import com.example.estoque.entity.ProdutoEntity;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ProdutoRepositoryTest {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Test
    public void findByNome () {
        var produto = new ProdutoEntity (  );
        produto.setNome ( "LapTop" );
        produto.setDescricao ( "Teste_Produto" );
        produto.setPreco ( 100.0 );
        produto.setQtd ( 10 );
        produtoRepository.save ( produto );

        var found = produtoRepository.findByNome ( "LapTop" );

        Assertions.assertEquals("LapTop", found.getNome ());
    }

    @Test
    public void givenProdutoNotExists_whenIFindByNome_theReturNull() {
        var produto = produtoRepository.findByNome(RandomString.make(15));
        Assertions.assertNull(produto);
    }
}