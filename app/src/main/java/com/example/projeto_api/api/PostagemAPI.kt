package com.example.projeto_api.api


import com.example.projeto_api.Model.Comentario
import com.example.projeto_api.Model.Postagem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PostagemAPI {
    //https://jsonplaceholder.typicode.com/posts

    @GET("posts/")
    suspend fun recupararPostagens(): Response<List<Postagem>>

    @GET("posts/{id}/comments")
    suspend fun recuperarComentariosParaPostagem(
        @Path("id") id: Int
    ): Response<List<Comentario>>

    @POST("posts/")
    suspend fun SalvarPostagem(
        @Body postagem: Postagem
    ): Response<Postagem>

    @FormUrlEncoded
    @POST("posts/")
    suspend fun SalvarPostagemFormulario(
        @Field("userId") userId: Int,
        @Field("id") id: Int,
        @Field("title") title: String,
        @Field("body") body: String,
    ): Response<Postagem>

    @PUT("posts/{id}")// atualização completa
    suspend fun atualizarPostagemPut(
        @Path("id") id: Int,
        @Body postagem: Postagem
    ): Response<Postagem>




    @GET("posts/{id}")
    suspend fun recupararPostagensUnica(
        @Path("id") id: Int
    ): Response<Postagem>


    @GET("comments")// comments?postId=1&idComentario=2&
    suspend fun recuperarComentariosParaPostagemQuery(
        @Query("postId") id: Int
    ): Response<List<Comentario>>




    @PATCH("posts/{id}")// atualização completa
    suspend fun atualizarPostagemPatch(
        @Path("id") id: Int,
        @Body postagem: Postagem
    ): Response<Postagem>

    @DELETE("posts/{id}")// remover postagem
    suspend fun removerPostagem(
        @Path("id") id: Int,
    ): Response<Unit>


}