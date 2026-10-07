package com.example.unigestionperu_docentesadministrativos.data.mapper

import com.example.unigestionperu_docentesadministrativos.data.local.entities.UsuarioEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.UsuarioDto

fun UsuarioDto.toEntity(): UsuarioEntity {
    return UsuarioEntity(
        id = id,
        nombre = nombre,
        email = email,
        password = password ?: "",
        rol = rol
    )
}

fun UsuarioEntity.toDto(): UsuarioDto {
    return UsuarioDto(
        id = id,
        nombre = nombre,
        email = email,
        rol = rol
    )
}
