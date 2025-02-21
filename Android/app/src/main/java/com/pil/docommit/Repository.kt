package com.pil.docommit.model

data class Repository(
    val name: String,
    val owner: Owner
)

data class Owner(
    val login: String
)
