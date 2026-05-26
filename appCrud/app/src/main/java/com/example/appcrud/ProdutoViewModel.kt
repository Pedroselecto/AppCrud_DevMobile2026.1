package com.example.appcrud

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProdutoViewModel : ViewModel() {

    // Estado para a lista de todos os produtos (GET geral)
    private val _produtos = MutableStateFlow<List<Produto>>(emptyList())
    val produtos: StateFlow<List<Produto>> = _produtos

    // === O NOVO ESTADO QUE FALTAVA ===
    // Guarda o resultado da busca de um único produto por código (pode ser null se não encontrar)
    private val _produtoBuscado = MutableStateFlow<Produto?>(null)
    val produtoBuscado: StateFlow<Produto?> = _produtoBuscado

    // Estados de controle da interface
    private val _carregando = MutableStateFlow(false)
    val carregando: StateFlow<Boolean> = _carregando

    private val _erro = MutableStateFlow<String?>(null)
    val erro: StateFlow<String?> = _erro


    // 1. GET - Carregar/Listar Todos os Produtos
    fun carregarProdutos() {
        viewModelScope.launch {
            _carregando.value = true
            _erro.value = null
            try {
                _produtos.value = RetrofitClient.api.listarProdutos()
            } catch (e: Exception) {
                e.printStackTrace()
                _erro.value = "Falha ao carregar produtos do servidor."
            } finally {
                _carregando.value = false
            }
        }
    }

    // ===A NOVA FUNÇÃO GET POR CÓDIGO ===
    // 2. GET/{codigo} - Buscar um único produto pelo código no MySQL
    fun buscarProdutoPorCodigo(codigo: Int) {
        viewModelScope.launch {
            _carregando.value = true
            _erro.value = null
            try {
                // Faz a chamada ao endpoint @GetMapping("/{codigo}") do Spring
                val produto = RetrofitClient.api.retornaProduto(codigo)
                _produtoBuscado.value = produto
            } catch (e: Exception) {
                e.printStackTrace()
                // Se o Spring retornar 404 (Not Found) ou falhar a rede, cai aqui
                _erro.value = "Produto com o código $codigo não foi encontrado."
                _produtoBuscado.value = null
            } finally {
                _carregando.value = false
            }
        }
    }

    // 3. POST - Adicionar Novo Produto
    fun adicionarNovoProduto(nome: String, preco: Float, codigo: Int) {
        viewModelScope.launch {
            _erro.value = null
            try {
                val produtoCriado = RetrofitClient.api.adicionaProduto(nome, preco, codigo)
                _produtos.value = _produtos.value + produtoCriado
            } catch (e: Exception) {
                e.printStackTrace()
                _erro.value = "Erro ao adicionar o produto no servidor."
            }
        }
    }

    // 4. DELETE - Eliminar Produto pelo Código
    fun excluirProduto(codigo: Int) {
        viewModelScope.launch {
            _erro.value = null
            try {
                RetrofitClient.api.deletaProduto(codigo)
                _produtos.value = _produtos.value.filter { it.codigo != codigo }

                // Se o produto deletado for o mesmo que estava na busca individual, limpa ele também
                if (_produtoBuscado.value?.codigo == codigo) {
                    _produtoBuscado.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _erro.value = "Não foi possível eliminar o produto."
            }
        }
    }

    // 5. PUT - Atualizar Dados de um Produto Existente
    fun atualizarProduto(codigo: Int, novoNome: String, novoPreco: Float) {
        viewModelScope.launch {
            _erro.value = null
            try {
                RetrofitClient.api.atualizaProduto(codigo, novoNome, novoPreco)

                val produtoAtualizado = Produto(nome = novoNome, preco = novoPreco, codigo = codigo)

                // Atualiza na lista geral
                _produtos.value = _produtos.value.map {
                    if (it.codigo == codigo) produtoAtualizado else it
                }

                // Se ele estiver aberto na busca individual, atualiza lá também
                if (_produtoBuscado.value?.codigo == codigo) {
                    _produtoBuscado.value = produtoAtualizado
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _erro.value = "Falha ao atualizar os dados do produto."
            }
        }
    }

    // Função utilitária para limpar a busca individual na tela quando necessário
    fun limparProdutoBuscado() {
        _produtoBuscado.value = null
    }
}