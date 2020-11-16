package ru.cepprice.githubprojects.data.local.model

import com.google.gson.annotations.SerializedName

data class SendRepo(
    @SerializedName("name") val name: String,
    @SerializedName("private") val isPrivate: Boolean = false,
    @SerializedName("auto_init") val addReadme: Boolean = false,
    @SerializedName("gitignore_template") val gitignore: String = "",
    @SerializedName("license_template") val license: String = ""
)