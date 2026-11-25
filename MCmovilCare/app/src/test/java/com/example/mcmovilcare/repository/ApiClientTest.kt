package com.example.mcmovilcare.repository

import org.junit.Assert.assertNotNull
import org.junit.Test

class ApiClientTest {

    @Test
    fun `expone servicios retrofit configurados`() {
        assertNotNull(ApiClient.mcmovilApi)
        assertNotNull(ApiClient.dummyJsonApi)
    }
}
