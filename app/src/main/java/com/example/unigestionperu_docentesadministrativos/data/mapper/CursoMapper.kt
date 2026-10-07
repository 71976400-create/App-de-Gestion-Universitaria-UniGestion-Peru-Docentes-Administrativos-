package com.example.unigestionperu_docentesadministrativos.data.mapper

import com.example.unigestionperu_docentesadministrativos.data.local.entities.CursoEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.CursoDto
import com.example.unigestionperu_docentesadministrativos.model.Curso

fun CursoEntity.toModel(): Curso {
    return Curso(
        id = id,
        codigo = codigo,
        nombre = nombre,
        docenteId = docenteId,
        creditos = creditos,
        ciclo = ciclo,
        facultad = facultad,
        modalidad = modalidad,
        cupoMaximo = cupoMaximo,
        matriculados = matriculados
    )
}

fun Curso.toEntity(): CursoEntity {
    return CursoEntity(
        id = id,
        codigo = codigo,
        nombre = nombre,
        docenteId = docenteId,
        creditos = creditos,
        ciclo = ciclo,
        facultad = facultad,
        modalidad = modalidad,
        cupoMaximo = cupoMaximo,
        matriculados = matriculados
    )
}

fun CursoDto.toEntity(): CursoEntity {
    return CursoEntity(
        id = id,
        codigo = codigo,
        nombre = nombre,
        docenteId = docenteId,
        creditos = creditos,
        ciclo = ciclo,
        facultad = facultad,
        modalidad = modalidad,
        cupoMaximo = cupoMaximo,
        matriculados = matriculados
    )
}

fun CursoEntity.toDto(): CursoDto {
    return CursoDto(
        id = id,
        codigo = codigo,
        nombre = nombre,
        docenteId = docenteId,
        creditos = creditos,
        ciclo = ciclo,
        facultad = facultad,
        modalidad = modalidad,
        cupoMaximo = cupoMaximo,
        matriculados = matriculados
    )
}
