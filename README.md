# Kotlin Serialization Converter

A Retrofit 2 `Converter.Factory` for [Kotlin serialization](https://github.com/Kotlin/kotlinx.serialization/).


## Usage

Add a converter factory when building your `Retrofit` instance using the `asConverterFactory`
extension function:

```kotlin
val contentType = "application/json".toMediaType()
val retrofit = Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addConverterFactory(Json.asConverterFactory(contentType))
    .build()
```

Response body types (e.g., `Call<User>`) and `@Body` types (e.g., `@Body user: User`) will now use
the supplied serializer.

Because Kotlin serialization is so flexible in the types it supports, these converters assume
that they can handle all types. If you are mixing this converter with another, you must add this
instance _last_ to allow the other converters a chance to see their types.


## Download

Gradle:
```kotlin
implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
```

Maven:
```xml
<dependency>
  <groupId>com.jakewharton.retrofit</groupId>
  <artifactId>retrofit2-kotlinx-serialization-converter</artifactId>
  <version>1.0.0</version>
</dependency>
```

Snapshots of the development version are available in
[Sonatype's `snapshots` repository](https://oss.sonatype.org/content/repositories/snapshots/).


# License

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
