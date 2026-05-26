package com.example.appcrud

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiCrud {

    @GET("/")
    suspend fun listarProdutos(): List<Produto>

    @GET("/{codigo}")
    suspend fun retornaProduto(@Path("codigo") codigo: Int): Produto

    @POST("/")
    suspend fun adicionaProduto(
        @Query("nome") nome: String,
        @Query("preco") preco: Float,
        @Query("codigo") codigo: Int
    ): Produto

    @DELETE("/{codigo}")
    suspend fun deletaProduto(@Path("codigo") codigo: Int)

    @PUT("/{codigo}")
    suspend fun atualizaProduto(
        @Path("codigo") codigo: Int,
        @Query("nomeNovo") nomeNovo: String,
        @Query("precoNovo") precoNovo: Float
    )
}