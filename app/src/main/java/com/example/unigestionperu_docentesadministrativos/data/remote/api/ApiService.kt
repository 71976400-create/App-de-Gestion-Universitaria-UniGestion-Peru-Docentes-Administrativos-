package com.example.unigestionperu_docentesadministrativos.data.remote.api

import com.example.unigestionperu_docentesadministrativos.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequestDto): Response<LoginResponseDto>

    @GET("api/cursos")
    suspend fun getCursos(): Response<List<CursoDto>>

    @GET("api/cursos/{id}")
    suspend fun getCursoById(@Path("id") id: Long): Response<CursoDto>

    @POST("api/cursos")
    suspend fun createCurso(@Body curso: CursoDto): Response<CursoDto>

    @PUT("api/cursos/{id}")
    suspend fun updateCurso(@Path("id") id: Long, @Body curso: CursoDto): Response<CursoDto>

    @DELETE("api/cursos/{id}")
    suspend fun deleteCurso(@Path("id") id: Long): Response<Unit>

    @GET("api/matriculas")
    suspend fun getMatriculas(): Response<List<MatriculaDto>>

    @GET("api/matriculas/curso/{cursoId}")
    suspend fun getMatriculasByCurso(@Path("cursoId") cursoId: Long): Response<List<MatriculaDto>>

    @PUT("api/matriculas/{id}/notas")
    suspend fun updateNotasMatricula(@Path("id") id: Long, @Body matricula: MatriculaDto): Response<MatriculaDto>

    @GET("api/salones")
    suspend fun getSalones(): Response<List<SalonDto>>

    @POST("api/salones")
    suspend fun createSalon(@Body salon: SalonDto): Response<SalonDto>

    @PUT("api/salones/{id}")
    suspend fun updateSalon(@Path("id") id: Long, @Body salon: SalonDto): Response<SalonDto>

    @DELETE("api/salones/{id}")
    suspend fun deleteSalon(@Path("id") id: Long): Response<Unit>

    @POST("api/sync")
    suspend fun syncOperaciones(@Body request: SyncRequestDto): Response<SyncResponseDto>
}
