package com.example.lr1_rpo.data

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    var title: String,
    var text: String){
    @Transient var isSelected:Boolean = false
}
