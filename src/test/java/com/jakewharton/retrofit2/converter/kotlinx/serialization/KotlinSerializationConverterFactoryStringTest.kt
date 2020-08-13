package com.jakewharton.retrofit2.converter.kotlinx.serialization

import kotlinx.serialization.Serializable
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.lang.IllegalArgumentException

class KotlinSerializationConverterFactoryStringTest {
  @get:Rule val server = MockWebServer()

  private lateinit var service: Service

  interface Service {
    @GET("/") fun deserialize(): Call<User>
    @GET("/") fun deserializeOther(): Call<NotSerializable>
    @POST("/") fun serialize(@Body user: User): Call<Void?>
    @POST("/") fun serializeOther(@Body not: NotSerializable): Call<Void?>
  }

  @Serializable
  data class User(val name: String)

  data class NotSerializable(val name: String)

  @UnstableDefault
  @Before fun setUp() {
    val contentType = MediaType.get("application/json; charset=utf-8")
    val retrofit = Retrofit.Builder()
        .baseUrl(server.url("/"))
        .addConverterFactory(Json.asConverterFactory(contentType))
        .build()
    service = retrofit.create(Service::class.java)
  }

  @Test fun deserialize() {
    server.enqueue(MockResponse().setBody("""{"name":"Bob"}"""))
    val user = service.deserialize().execute().body()!!
    assertEquals(User("Bob"), user)
  }

  @Test fun serialize() {
    server.enqueue(MockResponse())
    service.serialize(User("Bob")).execute()
    val request = server.takeRequest()
    assertEquals("""{"name":"Bob"}""", request.body.readUtf8())
    assertEquals("application/json; charset=utf-8", request.headers["Content-Type"])
  }

  @Test fun deserializeOther() {
    server.enqueue(MockResponse().setBody("""{"name":"Bob"}"""))
    assertThrows(IllegalArgumentException::class.java) {
      val user = service.deserializeOther().execute().body()
    }
  }

  @Test fun serializeOther() {
    server.enqueue(MockResponse())
    assertThrows(IllegalArgumentException::class.java) {
      service.serializeOther(NotSerializable("Bob"))
    }
  }
}
