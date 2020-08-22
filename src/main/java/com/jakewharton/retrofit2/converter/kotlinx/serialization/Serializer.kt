package com.jakewharton.retrofit2.converter.kotlinx.serialization

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialFormat
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializer
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type

@OptIn(ExperimentalSerializationApi::class)
internal sealed class Serializer {
  abstract fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T
  abstract fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody
  protected abstract val format: SerialFormat

  fun loader(type: Type): KSerializer<Any> = format.serializersModule.serializer(type)


  @OptIn(ExperimentalSerializationApi::class) // Experimental is only for subtypes.
  class FromString(override val format: StringFormat) : Serializer() {
    override fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T {
      val string = body.string()
      return format.decodeFromString(loader, string)
    }

    override fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody {
      val string = format.encodeToString(saver, value)
      return RequestBody.create(contentType, string)
    }
  }

  @OptIn(ExperimentalSerializationApi::class) // Experimental is only for subtypes.
  class FromBytes(override val format: BinaryFormat): Serializer() {
    override fun <T> fromResponseBody(loader: DeserializationStrategy<T>, body: ResponseBody): T {
      val bytes = body.bytes()
      return format.decodeFromByteArray(loader, bytes)
    }

    override fun <T> toRequestBody(contentType: MediaType, saver: SerializationStrategy<T>, value: T): RequestBody {
      val bytes = format.encodeToByteArray(saver, value)
      return RequestBody.create(contentType, bytes)
    }
  }
}
