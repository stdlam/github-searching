package com.android.nldlam.sample.searchgithubuser.data

import com.android.nldlam.sample.searchgithubuser.data.model.User
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class UserDeserializer : JsonDeserializer<User?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): User? {
        json?.asJsonObject?.let { jsonObject ->
            val company = jsonObject.get("company").asString
            val name = jsonObject.get("name").asString
            val location = jsonObject.get("location").asString
            val bio = jsonObject.get("bio").asString
            val publicRepos = jsonObject.get("public_repos").asInt
            val followers = jsonObject.get("followers").asInt
            val following = jsonObject.get("following").asInt
            return User(name, company, location, bio, publicRepos, followers, following)
        }
        return null
    }
}