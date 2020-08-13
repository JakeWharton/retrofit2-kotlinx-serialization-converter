@file:JvmName("KotlinSerializationConverterFactory")

package com.jakewharton.retrofit2.converter.kotlinx.serialization

import com.jakewharton.retrofit2.converter.kotlinx.serialization.Serializer.FromBytes
import com.jakewharton.retrofit2.converter.kotlinx.serialization.Serializer.FromString
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.StringFormat
import kotlinx.serialization.serializerByTypeToken
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

internal class Factory(
    private val contentType: MediaType,
    private val serializer: Serializer
): Converter.Factory() {
  override fun responseBodyConverter(type: Type, annotations: Array<out Annotation>,
      retrofit: Retrofit): Converter<ResponseBody, *>? {
    val loader = try {
      serializerByTypeToken(type)
    } catch (e: Exception) {
      return null
    }
    return DeserializationStrategyConverter(loader, serializer)
  }

  override fun requestBodyConverter(type: Type, parameterAnnotations: Array<out Annotation>,
      methodAnnotations: Array<out Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
    val saver = try {
      serializerByTypeToken(type)
    } catch (e: Exception) {
      return null
    }
    return SerializationStrategyConverter(contentType, saver, serializer)
  }
}

/**
 * Return a [Converter.Factory] which uses Kotlin serialization for string-based payloads.
 *
 * Because Kotlin serialization is so flexible in the types it supports, this converter assumes
 * that it can handle all types. If you are mixing this with something else, you must add this
 * instance last to allow the other converters a chance to see their types.
 */
@JvmName("create")
fun StringFormat.asConverterFactory(contentType: MediaType): Converter.Factory {
  return Factory(contentType, FromString(this))
}

/**
 * Return a [Converter.Factory] which uses Kotlin serialization for byte-based payloads.
 *
 * Because Kotlin serialization is so flexible in the types it supports, this converter assumes
 * that it can handle all types. If you are mixing this with something else, you must add this
 * instance last to allow the other converters a chance to see their types.
 */
@JvmName("create")
fun BinaryFormat.asConverterFactory(contentType: MediaType): Converter.Factory {
  return Factory(contentType, FromBytes(this))
}
