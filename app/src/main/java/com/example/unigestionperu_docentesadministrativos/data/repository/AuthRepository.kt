package com.example.unigestionperu_docentesadministrativos.data.repository

import com.example.unigestionperu_docentesadministrativos.data.local.dao.UsuarioDao
import com.example.unigestionperu_docentesadministrativos.data.local.entities.UsuarioEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.api.ApiService
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.LoginRequestDto
import com.example.unigestionperu_docentesadministrativos.data.mapper.toEntity

class AuthRepository(
    private val usuarioDao: UsuarioDao,
    private val apiService: ApiService
) {
    suspend fun login(email: String, password: String): UsuarioEntity? {
        // 1. Intento con API Remota (si hay servidor disponible)
        try {
            val response = apiService.login(LoginRequestDto(email, password))
            if (response.isSuccessful && response.body()?.usuario != null) {
                val usuarioDto = response.body()!!.usuario!!
                val usuarioEntity = usuarioDto.toEntity()
                usuarioDao.insertUsuario(usuarioEntity)
                return usuarioEntity
            }
        } catch (e: Exception) {
            // Servidor offline o inaccesible, se utiliza la copia local en Room
        }

        // 2. Acceso offline local utilizando Room
        return usuarioDao.login(email, password)
    }

    suspend fun getUsuarioByEmail(email: String): UsuarioEntity? {
        return usuarioDao.getUsuarioByEmail(email)
    }

    suspend fun countUsuarios(): Int {
        return usuarioDao.countUsuarios()
    }
}
