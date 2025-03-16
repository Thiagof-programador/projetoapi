package com.example.projeto_api.Model

data class Postagem(
    //@SerializedName("body")
    val body: String,
    val id: Int,
    val title: String?,
    val userId: Int
)
