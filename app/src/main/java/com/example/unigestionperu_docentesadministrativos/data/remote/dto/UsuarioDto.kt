package com.example.unigestionperu_docentesadministrativos.data.remote.dto

data class UsuarioDto(
    val id: Long = 0L,
    val nombre: String,
    val email: String,
    val password: String? = null,
    val rol: String
)

data class LoginRequestDto(
    val email: String,
    val password: String
)

data class LoginResponseDto(
    val token: String?,
    val usuario: UsuarioDto?
)
