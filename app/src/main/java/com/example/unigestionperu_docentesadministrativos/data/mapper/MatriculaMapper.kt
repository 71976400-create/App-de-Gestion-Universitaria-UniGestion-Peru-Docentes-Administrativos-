package com.example.unigestionperu_docentesadministrativos.data.mapper

import com.example.unigestionperu_docentesadministrativos.data.local.entities.MatriculaEntity
import com.example.unigestionperu_docentesadministrativos.data.remote.dto.MatriculaDto
import com.example.unigestionperu_docentesadministrativos.model.Matricula

fun MatriculaEntity.toModel(): Matricula {
    return Matricula(
        id = id,
        estudianteId = estudianteId,
        cursoId = cursoId,
        nota1 = nota1,
        nota2 = nota2,
        examenFinal = examenFinal,
        promedio = promedio
    )
}

fun Matricula.toEntity(): MatriculaEntity {
    return MatriculaEntity(
        id = id,
        estudianteId = estudianteId,
        cursoId = cursoId,
        nota1 = nota1,
        nota2 = nota2,
        examenFinal = examenFinal,
        promedio = promedio
    )
}

fun MatriculaDto.toEntity(): MatriculaEntity {
    return MatriculaEntity(
        id = id,
        estudianteId = estudianteId,
        cursoId = cursoId,
        nota1 = nota1,
        nota2 = nota2,
        examenFinal = examenFinal,
        promedio = promedio
    )
}

fun MatriculaEntity.toDto(): MatriculaDto {
    return MatriculaDto(
        id = id,
        estudianteId = estudianteId,
        cursoId = cursoId,
        nota1 = nota1,
        nota2 = nota2,
        examenFinal = examenFinal,
        promedio = promedio
    )
}
