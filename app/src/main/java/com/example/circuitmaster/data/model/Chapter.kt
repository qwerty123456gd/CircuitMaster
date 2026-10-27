package com.example.circuitmaster.data.model

data class Chapter(
    val id: String,
    val title: String,
    val subchapters: List<Subchapter>
)
//главы