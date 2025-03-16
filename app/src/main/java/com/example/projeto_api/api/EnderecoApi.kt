package com.example.projeto_api.api

import com.example.projeto_api.Model.Endereco
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface EnderecoApi {
    //https://viacep.com.br/ws/01001000/json/
    // Base URL :  https://viacep.com.br/
    // ROTA OU ENDPOINT
    //GET,POST,PUT,PATCH E DELETE
    @GET("ws/{cep}/json/")

    suspend fun recuperarEndereco(
        @Path("cep") cep: String
    ) : Response<Endereco>
}