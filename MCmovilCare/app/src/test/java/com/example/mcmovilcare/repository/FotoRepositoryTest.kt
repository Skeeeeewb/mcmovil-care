package com.example.mcmovilcare.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.file.Files

class FotoRepositoryTest {

    private lateinit var temporal: File

    private fun contexto(): Context {
        temporal = Files.createTempDirectory("mcmovil-fotos").toFile()
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        every { context.filesDir } returns temporal
        return context
    }

    @After
    fun limpiar() {
        unmockkStatic(BitmapFactory::class, Uri::class)
        if (::temporal.isInitialized) temporal.deleteRecursively()
    }

    @Test
    fun `guarda comprime y elimina una foto`() {
        val bitmap = mockk<Bitmap>(relaxed = true)
        every { bitmap.compress(any(), any<Int>(), any()) } returns true
        val repositorio = FotoRepository(contexto())

        val ruta = repositorio.guardar(bitmap)

        assertNotNull(ruta)
        assertTrue(File(ruta!!).exists())
        assertTrue(ruta.contains("evidencias"))

        repositorio.eliminar(ruta)
        assertFalse(File(ruta).exists())
    }

    @Test
    fun `carga una foto desde disco`() {
        mockkStatic(BitmapFactory::class)
        val bitmap = mockk<Bitmap>(relaxed = true)
        every { BitmapFactory.decodeFile(any<String>()) } returns bitmap
        val repositorio = FotoRepository(contexto())

        assertEquals(bitmap, repositorio.cargar("/tmp/foto.jpg"))
        assertNull(repositorio.cargar(null))
    }

    @Test
    fun `copia una foto desde el uri de galeria`() {
        mockkStatic(Uri::class)
        val uri = mockk<Uri>(relaxed = true)
        every { Uri.parse("content://galeria/foto") } returns uri
        val context = contexto()
        every { context.contentResolver.openInputStream(uri) } returns
            ByteArrayInputStream("contenido de prueba".toByteArray())
        val repositorio = FotoRepository(context)

        val ruta = repositorio.guardar("content://galeria/foto")

        assertNotNull(ruta)
        assertEquals("contenido de prueba", File(ruta!!).readText())
    }

    @Test
    fun `devuelve nulo si el uri no se puede abrir`() {
        mockkStatic(Uri::class)
        val uri = mockk<Uri>(relaxed = true)
        every { Uri.parse("content://invalido") } returns uri
        val context = contexto()
        every { context.contentResolver.openInputStream(uri) } returns null

        assertNull(FotoRepository(context).guardar("content://invalido"))
    }
}
