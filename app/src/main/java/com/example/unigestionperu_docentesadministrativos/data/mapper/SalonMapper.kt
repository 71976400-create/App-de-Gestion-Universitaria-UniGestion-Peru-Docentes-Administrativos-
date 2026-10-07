package com.example.unigestionperu_docentesadministrativos.data.mapper

import com.example.unigestionperu_docentesadministrativos.data.local.entities.SalonEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.SalonDto
import com.example.unigestionperu_docentesadministrativos.model.Salon

fun SalonEntity.toModel(): Salon {
    return Salon(
        id = id,
        codigo = codigo,
        edificio = edificio,
        capacidad = capacidad,
        ocupados = ocupados,
        tipo = tipo,
        docenteAsignado = docenteAsignado,
        horario = horario
    )
}

fun Salon.toEntity(): SalonEntity {
    return SalonEntity(
        id = id,
        codigo = codigo,
        edificio = edificio,
        capacidad = capacidad,
        ocupados = ocupados,
        tipo = tipo,
        docenteAsignado = docenteAsignado,
        horario = horario
    )
}

fun SalonDto.toEntity(): SalonEntity {
    return SalonEntity(
        id = id,
        codigo = codigo,
        edificio = edificio,
        capacidad = capacidad,
        ocupados = ocupados,
        tipo = tipo,
        docenteAsignado = docenteAsignado,
        horario = horario
    )
}

fun SalonEntity.toDto(): SalonDto {
    return SalonDto(
        id = id,
        codigo = codigo,
        edificio = edificio,
        capacidad = capacidad,
        ocupados = ocupados,
        tipo = tipo,
        docenteAsignado = docenteAsignado,
        horario = horario
    )
}
