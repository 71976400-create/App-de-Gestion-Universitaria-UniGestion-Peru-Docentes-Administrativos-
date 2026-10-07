package com.example.unigestionperu_docentesadministrativos.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.unigestionperu_docentesadministrativos.data.local.dao.*
import com.example.unigestionperu_docentesadministrativos.data.local.entities.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UsuarioEntity::class,
        CursoEntity::class,
        MatriculaEntity::class,
        SalonEntity::class,
        OperacionPendienteEntity::class,
        SyncMetadataEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun cursoDao(): CursoDao
    abstract fun matriculaDao(): MatriculaDao
    abstract fun salonDao(): SalonDao
    abstract fun operacionPendienteDao(): OperacionPendienteDao
    abstract fun syncMetadataDao(): SyncMetadataDao

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
                .fallbackToDestructiveMigration()
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
        val salonDao = db.salonDao()

        if (usuarioDao.countUsuarios() > 0) return false

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

        // 5. Seed 30 Salones / Aulas
        val salonesDemo = listOf(
            SalonEntity(codigo = "Aula A-101", edificio = "Pabellón A", capacidad = 40, ocupados = 25, tipo = "Teoría", docenteAsignado = "Dr. Carlos Mendoza", horario = "Lun y Mié 08:00 - 10:15 AM"),
            SalonEntity(codigo = "Aula A-102", edificio = "Pabellón A", capacidad = 35, ocupados = 30, tipo = "Teoría", docenteAsignado = "Dra. Ana Torres", horario = "Mar y Jue 08:00 - 10:15 AM"),
            SalonEntity(codigo = "Aula A-103", edificio = "Pabellón A", capacidad = 40, ocupados = 20, tipo = "Teoría", docenteAsignado = "Dr. Roberto Silva", horario = "Lun y Mié 10:30 - 12:45 PM"),
            SalonEntity(codigo = "Aula A-104", edificio = "Pabellón A", capacidad = 45, ocupados = 40, tipo = "Teoría", docenteAsignado = "Mag. Patricia Ramos", horario = "Mar y Jue 10:30 - 12:45 PM"),
            SalonEntity(codigo = "Aula A-105", edificio = "Pabellón A", capacidad = 30, ocupados = 15, tipo = "Teoría", docenteAsignado = "Dr. Juan Carlos Vega", horario = "Viernes 08:00 - 12:15 PM"),

            SalonEntity(codigo = "Lab Cómputo B-201", edificio = "Pabellón B (Sistemas)", capacidad = 30, ocupados = 28, tipo = "Laboratorio", docenteAsignado = "Dr. Carlos Mendoza", horario = "Mar y Jue 10:30 - 12:45 PM"),
            SalonEntity(codigo = "Lab Cómputo B-202", edificio = "Pabellón B (Sistemas)", capacidad = 30, ocupados = 25, tipo = "Laboratorio", docenteAsignado = "Ing. Luis Alberto Paredes", horario = "Lun y Mié 14:00 - 16:15 PM"),
            SalonEntity(codigo = "Lab Móvil B-203", edificio = "Pabellón B (Sistemas)", capacidad = 25, ocupados = 22, tipo = "Laboratorio", docenteAsignado = "Dr. Carlos Mendoza", horario = "Mar y Jue 14:00 - 16:15 PM"),
            SalonEntity(codigo = "Lab Redes B-204", edificio = "Pabellón B (Sistemas)", capacidad = 25, ocupados = 20, tipo = "Laboratorio", docenteAsignado = "Ing. Jorge Benítez", horario = "Viernes 14:00 - 18:15 PM"),
            SalonEntity(codigo = "Lab IA B-205", edificio = "Pabellón B (Sistemas)", capacidad = 30, ocupados = 29, tipo = "Laboratorio", docenteAsignado = "Dra. Sofia Quispe", horario = "Lun y Mié 16:30 - 18:45 PM"),

            SalonEntity(codigo = "Aula C-301", edificio = "Pabellón C (Ciencias)", capacidad = 50, ocupados = 45, tipo = "Teoría", docenteAsignado = "Dr. Fernando Gutierrez", horario = "Lun y Mié 08:00 - 10:15 AM"),
            SalonEntity(codigo = "Aula C-302", edificio = "Pabellón C (Ciencias)", capacidad = 50, ocupados = 48, tipo = "Teoría", docenteAsignado = "Dra. Elena Morales", horario = "Mar y Jue 08:00 - 10:15 AM"),
            SalonEntity(codigo = "Aula C-303", edificio = "Pabellón C (Ciencias)", capacidad = 40, ocupados = 35, tipo = "Teoría", docenteAsignado = "Dr. Hugo Sanchez", horario = "Lun y Mié 10:30 - 12:45 PM"),
            SalonEntity(codigo = "Aula C-304", edificio = "Pabellón C (Ciencias)", capacidad = 40, ocupados = 38, tipo = "Teoría", docenteAsignado = "Dra. Carmen Rosa Delgado", horario = "Mar y Jue 10:30 - 12:45 PM"),
            SalonEntity(codigo = "Aula C-305", edificio = "Pabellón C (Ciencias)", capacidad = 35, ocupados = 30, tipo = "Teoría", docenteAsignado = "Dr. Mario Vargas", horario = "Sábados 08:00 - 12:15 PM"),
            SalonEntity(codigo = "Lab Física C-306", edificio = "Pabellón C (Ciencias)", capacidad = 30, ocupados = 26, tipo = "Laboratorio", docenteAsignado = "Lic. Ricardo Palma", horario = "Lun y Mié 14:00 - 16:15 PM"),
            SalonEntity(codigo = "Lab Química C-307", edificio = "Pabellón C (Ciencias)", capacidad = 25, ocupados = 24, tipo = "Laboratorio", docenteAsignado = "Dra. Ana Torres", horario = "Mar y Jue 14:00 - 16:15 PM"),

            SalonEntity(codigo = "Aula Magna 401", edificio = "Edificio Central", capacidad = 120, ocupados = 110, tipo = "Conferencias", docenteAsignado = "Dra. Ana Torres", horario = "Viernes 14:00 - 17:00 PM"),
            SalonEntity(codigo = "Auditorio Principal 402", edificio = "Edificio Central", capacidad = 200, ocupados = 180, tipo = "Auditorio", docenteAsignado = "Dr. Carlos Mendoza", horario = "Jueves 16:00 - 19:00 PM"),

            SalonEntity(codigo = "Aula D-501", edificio = "Pabellón D (Humanidades)", capacidad = 40, ocupados = 32, tipo = "Teoría", docenteAsignado = "Mag. Manuel Ugarte", horario = "Lun y Mié 08:00 - 10:15 AM"),
            SalonEntity(codigo = "Aula D-502", edificio = "Pabellón D (Humanidades)", capacidad = 40, ocupados = 36, tipo = "Teoría", docenteAsignado = "Dra. Beatriz Galindo", horario = "Mar y Jue 08:00 - 10:15 AM"),
            SalonEntity(codigo = "Aula D-503", edificio = "Pabellón D (Humanidades)", capacidad = 35, ocupados = 20, tipo = "Teoría", docenteAsignado = "Lic. Fernando Alonso", horario = "Lun y Mié 10:30 - 12:45 PM"),
            SalonEntity(codigo = "Aula D-504", edificio = "Pabellón D (Humanidades)", capacidad = 45, ocupados = 42, tipo = "Teoría", docenteAsignado = "Dr. Gonzalo Pizarro", horario = "Mar y Jue 10:30 - 12:45 PM"),

            SalonEntity(codigo = "Taller Diseño E-101", edificio = "Pabellón E (Arquitectura)", capacidad = 30, ocupados = 27, tipo = "Taller", docenteAsignado = "Arq. Claudia Castillo", horario = "Lun y Mié 14:00 - 17:15 PM"),
            SalonEntity(codigo = "Taller Maquetas E-102", edificio = "Pabellón E (Arquitectura)", capacidad = 25, ocupados = 22, tipo = "Taller", docenteAsignado = "Arq. Esteban Quito", horario = "Mar y Jue 14:00 - 17:15 PM"),

            SalonEntity(codigo = "Lab Robótica F-201", edificio = "Pabellón F (Ingeniería)", capacidad = 25, ocupados = 21, tipo = "Laboratorio", docenteAsignado = "Ing. Roberto Gomez", horario = "Viernes 08:00 - 12:15 PM"),
            SalonEntity(codigo = "Lab Electrónica F-202", edificio = "Pabellón F (Ingeniería)", capacidad = 25, ocupados = 23, tipo = "Laboratorio", docenteAsignado = "Ing. Daniel Ortega", horario = "Sábados 14:00 - 18:15 PM"),

            SalonEntity(codigo = "Aula Virtual V-01", edificio = "Campus Virtual UniGestión", capacidad = 200, ocupados = 150, tipo = "Virtual", docenteAsignado = "Dr. Carlos Mendoza", horario = "Lun y Mié 19:00 - 21:15 PM"),
            SalonEntity(codigo = "Aula Virtual V-02", edificio = "Campus Virtual UniGestión", capacidad = 200, ocupados = 175, tipo = "Virtual", docenteAsignado = "Dra. Ana Torres", horario = "Mar y Jue 19:00 - 21:15 PM"),
            SalonEntity(codigo = "Aula Virtual V-03", edificio = "Campus Virtual UniGestión", capacidad = 250, ocupados = 210, tipo = "Virtual", docenteAsignado = "Dr. Roberto Silva", horario = "Viernes 19:00 - 22:00 PM")
        )

        salonesDemo.forEach { salon ->
            salonDao.insertSalon(salon)
        }

        return true
    }
}
