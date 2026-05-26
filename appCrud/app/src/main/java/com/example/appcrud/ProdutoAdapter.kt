package com.example.appcrud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProdutoAdapter(
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    private var listaProdutos = emptyList<Produto>()

    // Função para atualizar os dados do Adapter sempre que a ViewModel mudar
    fun atualizarLista(novaLista: List<Produto>) {
        this.listaProdutos = novaLista
        notifyDataSetChanged() // Avisa o RecyclerView para se redesenhar
    }

    // 1. Cria o visual do item na tela (infla o XML)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    // 2. Cola os dados do Produto nos componentes de texto do XML
    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = listaProdutos[position]
        holder.bind(produto, onDeleteClick)
    }

    override fun getItemCount(): Int = listaProdutos.size

    // Classe interna que segura as referências dos componentes visuais de cada linha
    class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textCodigo = itemView.findViewById<TextView>(R.id.textCodigoItem)
        private val textNome = itemView.findViewById<TextView>(R.id.textNomeItem)
        private val textPreco = itemView.findViewById<TextView>(R.id.textPrecoItem)
        private val btnDeletar = itemView.findViewById<Button>(R.id.btnDeletarItem)

        fun bind(produto: Produto, onDeleteClick: (Int) -> Unit) {
            textCodigo.text = "Cód: ${produto.codigo}"
            textNome.text = produto.nome
            textPreco.text = "R$ ${produto.preco}"

            // Quando clicar em deletar, passa o código do produto para a ViewModel
            btnDeletar.setOnClickListener {
                onDeleteClick(produto.codigo)
            }
        }
    }
}