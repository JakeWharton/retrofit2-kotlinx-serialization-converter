package com.jakewharton.retrofit2.converter.kotlinx.serialization

import kotlinx.serialization.KSerialLoader
import kotlinx.serialization.KSerialSaver
import kotlinx.serialization.json.JSON
import kotlinx.serialization.protobuf.ProtoBuf
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody

internal sealed class Serializer {
  abstract fun <T : Any> fromResponseBody(loader: KSerialLoader<T>, body: ResponseBody): T
  abstract fun <T : Any> toRequestBody(contentType: MediaType, saver: KSerialSaver<T>, value: T): RequestBody

  class FromJson: Serializer() {
    override fun <T : Any> fromResponseBody(loader: KSerialLoader<T>, body: ResponseBody): T {
      val string = body.string()
      return JSON.parse(loader, string)
    }

    override fun <T : Any> toRequestBody(contentType: MediaType, saver: KSerialSaver<T>, value: T): RequestBody {
      val string = JSON.stringify(saver, value)
      return RequestBody.create(contentType, string)
    }
  }

  class FromProtoBuf : Serializer() {
    override fun <T : Any> fromResponseBody(loader: KSerialLoader<T>, body: ResponseBody): T {
      val bytes = body.bytes()
      return ProtoBuf.load(loader, bytes)
    }

    override fun <T : Any> toRequestBody(contentType: MediaType, saver: KSerialSaver<T>, value: T): RequestBody {
      val bytes = ProtoBuf.dump(saver, value)
      return RequestBody.create(contentType, bytes)
    }
  }
}
