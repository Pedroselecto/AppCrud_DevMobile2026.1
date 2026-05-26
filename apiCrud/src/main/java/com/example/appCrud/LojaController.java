package com.example.appCrud;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Gerenciamento de loja", description = "Endpoints para gerenciamento de produtos da loja")

public class LojaController {

    private GerenciadorLoja gerenciadorLoja;

    public LojaController(GerenciadorLoja gerenciadorLoja) {this.gerenciadorLoja = gerenciadorLoja;}

    @GetMapping
    @Operation(summary = "Retorna uma lista de produtos", description = "Mostra uma lista com todos os produtos")
    public List<Produto> listarProdutos(){
        return gerenciadorLoja.listarProdutos();
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Retornar produto", description = "Retorna um produto pelo código")
    public ResponseEntity<Produto> retornaProduto(@PathVariable int codigo) {
        Produto produto = gerenciadorLoja.retornarProduto(codigo);

        if (produto != null){
            return ResponseEntity.ok(produto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(summary = "Adicionar produto", description = "Adiciona um produto com seu nome e preço")
    public Produto adicionaProduto(
        @RequestParam String nome,
        @RequestParam float preco,
        @RequestParam int codigo) {
        return gerenciadorLoja.inserirProduto(nome, preco, codigo);

    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Deletar produto", description = "Deleta um produto pelo seu código")
    public void deletaProduto(@PathVariable int codigo) {
       gerenciadorLoja.deletarProduto(codigo);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Atualizar produto", description = "Atualiza um produto pelo código")
    public void atualizaProduto(
            @PathVariable int codigo,
            @RequestParam String nomeNovo,
            @RequestParam float precoNovo){
        gerenciadorLoja.updateProduto(codigo, nomeNovo, precoNovo);
    }

}
