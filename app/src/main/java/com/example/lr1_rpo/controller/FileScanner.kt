package com.example.lr1_rpo.controller

import android.content.Context
import com.example.lr1_rpo.data.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import java.io.File

class FileScanner {
    companion object {
        @JvmStatic
        fun writeNotesToFile(filename: String,notes: MutableList<Note>, context: Context){
            val pathToDir = context.getFilesDir()
            var file = File("$pathToDir/$filename")
            val isNewFileCreated :Boolean = file.createNewFile()
            val jsonList = Json.encodeToString(notes)
            file.writeText(jsonList)
        }

        @JvmStatic
        fun readNotesFromFile(filename: String, context: Context):MutableList<Note>{
            val pathToDir = context.getFilesDir()
            var file = File("$pathToDir/$filename")
            if(file.exists()) {
                val bufferedReader = file.bufferedReader()
                val outputString = bufferedReader.use { it.readText() }
                return Json.decodeFromString<MutableList<Note>>(outputString)
            }
            return mutableListOf()
        }
    }

}