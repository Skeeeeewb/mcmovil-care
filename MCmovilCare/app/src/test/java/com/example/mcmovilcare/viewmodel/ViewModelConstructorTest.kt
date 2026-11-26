package com.example.mcmovilcare.viewmodel

import android.app.Application
import org.junit.Assert.assertNotNull
import org.junit.Test

class ViewModelConstructorTest {

    @Test
    fun `DetalleAtencionViewModel expone constructor para AndroidViewModelFactory`() {
        assertNotNull(
            DetalleAtencionViewModel::class.java.getConstructor(Application::class.java)
        )
    }

    @Test
    fun `SolicitudViewModel expone constructor para AndroidViewModelFactory`() {
        assertNotNull(
            SolicitudViewModel::class.java.getConstructor(Application::class.java)
        )
    }
}
