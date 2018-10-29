Kotlin Serialization Converter
==============================

A Retrofit 2 `Converter.Factory` for [Kotlin serialization][1].


Usage
-----

Add a converter factory when building your `Retrofit` instance using the
`serializationConverterFactory` factory method:
```kotlin
val contentType = MediaType.parse("application/json")!!
val retrofit = Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addConverterFactory(serializationConverterFactory(contentType, JSON))
    .build()
```

Response body types (e.g., `Call<User>`) and `@Body` types (e.g., `@Body user: User`) will now use
the supplied serializer.

Because Kotlin serialization is so flexible in the types it supports, these converters assume
that they can handle all types. If you are mixing this converter with another, you must add this
instance _last_ to allow the other converters a chance to see their types.


Download
--------

Download [the latest JAR][2] or grab via [Maven][3]:
```xml
<dependency>
  <groupId>com.jakewharton.retrofit</groupId>
  <artifactId>retrofit2-kotlinx-serialization-converter</artifactId>
  <version>0.2.0</version>
</dependency>
```
or [Gradle][3]:
```groovy
compile 'com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.2.0'
```

Snapshots of the development version are available in [Sonatype's `snapshots` repository][snap].


License
=======

    Copyright 2018 Jake Wharton

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.




 [1]: https://github.com/Kotlin/kotlinx.serialization/
 [2]: https://search.maven.org/remote_content?g=com.squareup.retrofit2&a=adapter-kotlin-coroutines-experimental&v=LATEST
 [3]: http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.squareup.retrofit2%22%20a%3A%22adapter-kotlin-coroutines-experimental%22
 [snap]: https://oss.sonatype.org/content/repositories/snapshots/
