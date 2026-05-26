package com.example.appcrud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ProdutoViewModel
    private lateinit var adapter: ProdutoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. Inicializar a ViewModel de forma correta para o sistema de Views
        viewModel = ViewModelProvider(this)[ProdutoViewModel::class.java]

        // 2. Mapear os componentes do layout XML
        val editCodigo = findViewById<EditText>(R.id.editCodigo)
        val editPreco = findViewById<EditText>(R.id.editPreco)
        val editNome = findViewById<EditText>(R.id.editNome)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewProdutos)

        // 3. Configurar o RecyclerView e o Adapter
        adapter = ProdutoAdapter(onDeleteClick = { codigo ->
            // Ação do botão deletar da lista: chama a ViewModel
            viewModel.excluirProduto(codigo)
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 4. Lógica do Botão Salvar (POST)
        btnSalvar.setOnClickListener {
            val cod = editCodigo.text.toString().toIntOrNull()
            val preco = editPreco.text.toString().toFloatOrNull()
            val nome = editNome.text.toString()

            if (cod != null && preco != null && nome.isNotBlank()) {
                viewModel.adicionarNovoProduto(nome, preco, cod)

                // Limpa os campos após enviar
                editCodigo.text.clear()
                editPreco.text.clear()
                editNome.text.clear()
            } else {
                Toast.makeText(this, "Preencha todos os campos corretamente!", Toast.LENGTH_SHORT).show()
            }
        }

        // 5. OBSERVAR OS ESTADOS DA VIEWMODEL (Aqui acontece a magia reativa)
        // Usamos o lifecycleScope porque estamos a escutar um StateFlow assíncrono
        lifecycleScope.launch {
            viewModel.produtos.collect { listaDeProdutos ->
                // Sempre que a lista no MySQL mudar, o StateFlow emite a nova lista aqui
                // e o adapter atualiza a tela automaticamente
                adapter.atualizarLista(listaDeProdutos)
            }
        }

        lifecycleScope.launch {
            viewModel.erro.collect { mensagemDeErro ->
                if (mensagemDeErro != null) {
                    Toast.makeText(this@MainActivity, mensagemDeErro, Toast.LENGTH_LONG).show()
                }
            }
        }

        // Carrega os produtos do MySQL assim que a aplicação abre
        viewModel.carregarProdutos()
    }
}