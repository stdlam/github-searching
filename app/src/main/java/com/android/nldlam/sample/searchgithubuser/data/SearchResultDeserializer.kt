package com.android.nldlam.sample.searchgithubuser.data

import com.android.nldlam.sample.searchgithubuser.data.model.SearchResult
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class SearchResultDeserializer : JsonDeserializer<MutableList<SearchResult>> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MutableList<SearchResult> {
        val results = mutableListOf<SearchResult>()
        json?.asJsonObject?.let { jsonObject ->
            val list = jsonObject.get("items").asJsonArray
            list.forEach { jsonElement ->
                val eleObject = jsonElement.asJsonObject
                val login = eleObject.get("login").asString
                val avatarUrl = eleObject.get("avatar_url").asString
                results.add(SearchResult(login, avatarUrl))
            }
        }
        return results
    }
}