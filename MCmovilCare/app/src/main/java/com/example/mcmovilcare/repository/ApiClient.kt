package com.example.mcmovilcare.repository

import com.example.mcmovilcare.model.AtencionDto
import com.example.mcmovilcare.model.DecisionDto
import com.example.mcmovilcare.model.DecisionRespuestaDto
import com.example.mcmovilcare.model.EquipoDto
import com.example.mcmovilcare.model.ProductoDto
import com.example.mcmovilcare.model.SolicitudDto
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:8080/api/"
private const val DUMMY_JSON_URL = "https://dummyjson.com/"

interface McmovilApi {

    @GET("equipos")
    suspend fun listarEquipos(): List<EquipoDto>

    @GET("equipos/{id}")
    suspend fun obtenerEquipo(@Path("id") id: Int): EquipoDto

    @GET("atenciones")
    suspend fun listarAtenciones(): List<AtencionDto>

    @GET("atenciones/{id}")
    suspend fun obtenerAtencion(@Path("id") id: Int): AtencionDto

    @GET("solicitudes")
    suspend fun listarSolicitudes(): List<SolicitudDto>

    @GET("solicitudes/equipo/{equipoId}")
    suspend fun listarSolicitudesPorEquipo(@Path("equipoId") equipoId: Int): List<SolicitudDto>

    @POST("solicitudes")
    suspend fun crearSolicitud(@Body solicitud: SolicitudDto): SolicitudDto

    @GET("atenciones/{id}/decision")
    suspend fun obtenerDecision(@Path("id") atencionId: Int): DecisionRespuestaDto

    @PUT("atenciones/{id}/decision")
    suspend fun guardarDecision(
        @Path("id") atencionId: Int,
        @Body decision: DecisionDto
    ): DecisionRespuestaDto
}

interface DummyJsonApi {

    @GET("products/{id}")
    suspend fun obtenerProducto(@Path("id") id: Int): ProductoDto
}

object ApiClient {

    private val cliente = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private fun retrofits(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(cliente)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val mcmovilApi: McmovilApi by lazy { retrofits(BASE_URL).create(McmovilApi::class.java) }

    val dummyJsonApi: DummyJsonApi by lazy { retrofits(DUMMY_JSON_URL).create(DummyJsonApi::class.java) }
}
