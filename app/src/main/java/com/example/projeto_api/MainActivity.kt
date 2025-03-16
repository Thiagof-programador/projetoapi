package com.example.projeto_api

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.projeto_api.Model.Postagem
import com.example.projeto_api.api.PostagemAPI
import com.example.projeto_api.api.RetrofitHelper
import com.example.projeto_api.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import com.example.projeto_api.Model.Comentario
import com.example.projeto_api.Model.Endereco
import com.example.projeto_api.api.EnderecoApi

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val retrofit by lazy {
        RetrofitHelper.retrofit
    }
    private val apiViaCep by lazy {
        RetrofitHelper.apiViaCep
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnGet.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                recuperarEndereco()
                //recupararPostagens()
                //recupararPostagensUnica()
                //recupararComentariosParaPostagem()
                //recupararComentariosParaPostagemQuery()
                //SalvarPostagem()
                //SalvarPostagemFormulario()
                //atualizarPostagemPut()
                //atualizarPostagemPatch()
                //removerPostagem()
            }
        }
        binding.BtnGetCom.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                recupararComentariosParaPostagem()
            }
        }

        binding.btnpost.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                SalvarPostagem()
            }
        }

        binding.btnPostForm.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                SalvarPostagemFormulario()
            }
        }

        binding.btnPut.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                atualizarPostagemPut()
            }
        }

        binding.btnPatch.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                atualizarPostagemPatch()
            }
        }

        binding.BtnDelete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                removerPostagem()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TextCep)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private suspend fun recuperarEndereco(){

        var retorno: Response<Endereco>? = null
        val cepDigitadoUsuario = binding.editCep.text.toString()

        try {
            val enderecoAPI = apiViaCep.create(EnderecoApi::class.java)
            retorno = enderecoAPI.recuperarEndereco(cepDigitadoUsuario)
        }catch (e:Exception){
            e.printStackTrace()
            //Log.i("info_endereco","erro ao recuperar $e")
            binding.textView.text = "erro ao recuperar $e"
        }
        if(retorno != null){
            if(retorno.isSuccessful){

                val endereco = retorno.body()
                val rua = endereco?.logradouro
                val cidade = endereco?.localidade
                val uf = endereco?.uf
                val cepapi = endereco?.cep
                //Log.i("info_endereco","Endereco: $rua , Cidade: $cidade $uf ,CEP: $cepapi")
                binding.textView.text = "Endereco: $rua , Cidade: $cidade, Uf: $uf, CEP: $cepapi"
            }else{
                //Log.i("info_endereco","Cep não encontrado: $cepDigitadoUsuario")
                binding.textView.text = "Cep não encontrado: $cepDigitadoUsuario"
            }
            binding.editCep.setText("")
        }



    }
    private suspend fun recupararComentariosParaPostagem() {
        var retorno: Response<List<Comentario>>? = null

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.recuperarComentariosParaPostagem(1)
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val listaComentario = retorno.body()
                var resultado=""
                listaComentario?.forEach { comentario ->
                    val idComentario= comentario.id
                    val email= comentario.email
                    val comentarioResultado = "$idComentario - $email \n"
                    resultado+=comentarioResultado
                }
                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }

        }
    }
    private suspend fun SalvarPostagem() {
        var retorno: Response<Postagem>? = null

        val postagem = Postagem(
            "corpo da postagem",
            1,
            "titulo da postagem",
            1090
        )

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.SalvarPostagem( postagem )
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val postagem = retorno.body()

                val id = postagem?.id
                val titulo = postagem?.title
                val idUsuario = postagem?.userId
                var resultado="SUCESSO CODE: ${retorno.code()} id: $id - T: $titulo - U:$idUsuario"


                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }else{
                withContext(Dispatchers.Main){
                    binding.textView.text = "ERRO: CODE: ${retorno.code()}"
                }
            }

        }
    }
    private suspend fun SalvarPostagemFormulario() {
        var retorno: Response<Postagem>? = null

        val postagem = Postagem(
            "corpo da postagem",
            1,
            "titulo da postagem",
            1090
        )

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.SalvarPostagemFormulario(
                1090,
                -1,
                "titulo da postagem",
                "corpo da postagem"
            )
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val postagem = retorno.body()

                val id = postagem?.id
                val titulo = postagem?.title
                val idUsuario = postagem?.userId
                var resultado="SUCESSO CODE: ${retorno.code()} id: $id - T: $titulo - U:$idUsuario"


                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }else{
                withContext(Dispatchers.Main){
                    binding.textView.text = "ERRO: CODE: ${retorno.code()}"
                }
            }

        }
    }
    private suspend fun atualizarPostagemPut() {
        var retorno: Response<Postagem>? = null

        val postagem = Postagem(
            "corpo da postagem",
            1,
            "titulo da postagem",
            1090
        )

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.atualizarPostagemPut(
                1,
                Postagem("descricao",
                    -1,
                    null,
                    1090)
            )
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val postagem = retorno.body()

                val id = postagem?.id
                val titulo = postagem?.title
                val idUsuario = postagem?.userId
                val corpo = postagem?.body
                var resultado="SUCESSO CODE: ${retorno.code()} id: $id - T: $titulo - C:$corpo - U:$idUsuario "


                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }else{
                withContext(Dispatchers.Main){
                    binding.textView.text = "ERRO: CODE: ${retorno.code()}"
                }
            }

        }
    }
    private suspend fun atualizarPostagemPatch() {
        var retorno: Response<Postagem>? = null

        val postagem = Postagem(
            "corpo da postagem",
            1,
            "titulo da postagem",
            1090
        )

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.atualizarPostagemPatch(
                1,
                Postagem("corpo da postagem",
                    -1,
                    null,
                    1090)
            )
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val postagem = retorno.body()

                val id = postagem?.id
                val titulo = postagem?.title
                val idUsuario = postagem?.userId
                val corpo = postagem?.body
                var resultado="SUCESSO CODE: ${retorno.code()} id: $id - T: $titulo - C:$corpo - U:$idUsuario "


                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }else{
                withContext(Dispatchers.Main){
                    binding.textView.text = "ERRO: CODE: ${retorno.code()}"
                }
            }

        }
    }
    private suspend fun removerPostagem() {
        var retorno: Response<Unit>? = null

        val postagem = Postagem(
            "corpo da postagem",
            1,
            "titulo da postagem",
            1090
        )

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno= postagemApi.removerPostagem(1)
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){

                var resultado="SUCESSO AO REMOVER POSTAGEM CODE: ${retorno.code()} "


                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }else{
                withContext(Dispatchers.Main){
                    binding.textView.text = "ERRO AO REMOVER POSTAGEM: CODE: ${retorno.code()}"
                }
            }

        }
    }



    private suspend fun recupararComentariosParaPostagemQuery() {
        var retorno: Response<List<Comentario>>? = null

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.recuperarComentariosParaPostagemQuery(1)
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val listaComentario = retorno.body()
                var resultado=""
                listaComentario?.forEach { comentario ->
                    val idComentario= comentario.id
                    val email= comentario.email
                    val comentarioResultado = "$idComentario - $email \n"
                    resultado+=comentarioResultado
                }
                withContext(Dispatchers.Main){
                    binding.textView.text = resultado
                }

            }

        }
    }
    private suspend fun recupararPostagensUnica() {
        var retorno: Response<Postagem>? = null

        try {
            val postagemAPI = retrofit.create(PostagemAPI::class.java)
            retorno = postagemAPI.recupararPostagensUnica(1)
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar 1")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val postagem = retorno.body()

                Log.i("info_jsonplace","Id: ${postagem?.id} - ${postagem?.title} ")
                // só pode ser execultado na main pois esta usando CoroutineScope(Dispatchers.IO)
                withContext(Dispatchers.Main){
                    binding.textView.text = "Id: ${postagem?.id} - ${postagem?.title} "
                }

            }else{
                Log.i("info_jsonplace","Erro ao recuperar 2")

            }

        }
    }
    private suspend fun recupararPostagens() {
        var retorno: Response<List<Postagem>>? = null

        try {
            val postagemApi = retrofit.create(PostagemAPI::class.java)
            retorno = postagemApi.recupararPostagens()
        }catch (e:Exception){
            e.printStackTrace()
            Log.i("info_jsonplace","Erro ao recuperar")

        }
        if(retorno != null){
            if(retorno.isSuccessful){
                val listaPostagens = retorno.body()
                listaPostagens?.forEach { postagem ->
                    val id = postagem.id
                    val title = postagem.title
                    Log.i("info_jsonplace","Id: $id , Title: $title")
                }

            }else{
                Log.i("info_jsonplace","Erro ao recuperar")

            }

        }
    }






}