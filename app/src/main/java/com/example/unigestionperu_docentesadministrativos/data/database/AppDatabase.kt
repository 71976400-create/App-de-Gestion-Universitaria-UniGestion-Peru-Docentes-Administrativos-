package com.example.unigestionperu_docentesadministrativos.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [UsuarioEntity::class, CursoEntity::class, MatriculaEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun cursoDao(): CursoDao
    abstract fun matriculaDao(): MatriculaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "unigestion_docentes_db"
                )
                .addCallback(DatabaseCallback())
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        DatabaseSeeder.seed(database)
                    }
                }
            }
        }
    }
}

object DatabaseSeeder {
    suspend fun seed(db: AppDatabase): Boolean {
        val usuarioDao = db.usuarioDao()
        val cursoDao = db.cursoDao()
        val matriculaDao = db.matriculaDao()

        // 1. Seed Docentes y Administrativos
        val docente1Id = usuarioDao.insertUsuario(
            UsuarioEntity(nombre = "Dr. Carlos Mendoza", email = "carlos.mendoza@unigestion.edu.pe", password = "123", rol = "Docente")
        )
        val docente2Id = usuarioDao.insertUsuario(
            UsuarioEntity(nombre = "Dra. Ana Torres", email = "ana.torres@unigestion.edu.pe", password = "123", rol = "Docente")
        )
        usuarioDao.insertUsuario(
            UsuarioEntity(nombre = "Admin Roberto Silva", email = "admin@unigestion.edu.pe", password = "admin", rol = "Administrativo")
        )

        // 2. Seed Estudiantes
        val est1Id = usuarioDao.insertUsuario(
            UsuarioEntity(nombre = "Juan Pérez", email = "juan.perez@unigestion.edu.pe", password = "123", rol = "Estudiante")
        )
        val est2Id = usuarioDao.insertUsuario(
            UsuarioEntity(nombre = "María García", email = "maria.garcia@unigestion.edu.pe", password = "123", rol = "Estudiante")
        )
        val est3Id = usuarioDao.insertUsuario(
            UsuarioEntity(nombre = "Pedro Lopez", email = "pedro.lopez@unigestion.edu.pe", password = "123", rol = "Estudiante")
        )

        // 3. Seed Cursos
        val curso1Id = cursoDao.insertCurso(
            CursoEntity(codigo = "CS101", nombre = "Desarrollo Móvil Android", creditos = 4, docenteId = docente1Id, ciclo = "2026-I")
        )
        val curso2Id = cursoDao.insertCurso(
            CursoEntity(codigo = "CS102", nombre = "Base de Datos Avanzada", creditos = 3, docenteId = docente1Id, ciclo = "2026-I")
        )
        val curso3Id = cursoDao.insertCurso(
            CursoEntity(codigo = "CS103", nombre = "Ingeniería de Software", creditos = 4, docenteId = docente2Id, ciclo = "2026-I")
        )

        // 4. Seed Matriculas y Notas
        matriculaDao.insertMatricula(
            MatriculaEntity(estudianteId = est1Id, cursoId = curso1Id, nota1 = 15.0, nota2 = 16.0, examenFinal = 14.0, promedio = 15.0)
        )
        matriculaDao.insertMatricula(
            MatriculaEntity(estudianteId = est2Id, cursoId = curso1Id, nota1 = 18.0, nota2 = 17.0, examenFinal = 19.0, promedio = 18.0)
        )
        matriculaDao.insertMatricula(
            MatriculaEntity(estudianteId = est3Id, cursoId = curso2Id, nota1 = 12.0, nota2 = 13.0, examenFinal = 11.0, promedio = 12.0)
        )

        return true
    }
}
