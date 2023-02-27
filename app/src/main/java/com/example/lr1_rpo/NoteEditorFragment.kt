package com.example.lr1_rpo

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import com.example.lr1_rpo.controller.FileScanner
import com.example.lr1_rpo.data.Note
import com.google.android.material.internal.ViewUtils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_note_editor.*



class NoteEditorFragment : Fragment() {
    private var id: Int? = null
    var notes: MutableList<Note> = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(id!=null) {
            dt_notes_title.setText(notes[id!!].title)
            ed_note_text.setText(notes[id!!].text)
        }

        fabBtnAddNote.setOnClickListener {
            saveNote()
            requireActivity().supportFragmentManager.popBackStack()
        }


        btn_back_arrow.setOnClickListener{
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun saveNote(){
        var note: Note
        if(id!=null) {
            notes[id!!].title=dt_notes_title.text.toString()
            notes[id!!].text=ed_note_text.text.toString()
        }else{
            note = Note(dt_notes_title.text.toString(),ed_note_text.text.toString())
            notes.add(note)
        }
        FileScanner.writeNotesToFile("notes.txt",notes, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_editor, container, false)
    }

    fun setNote(notes_:MutableList<Note>,noteId:Int){
        notes=notes_
        id=noteId
    }

    fun setNote(notes_:MutableList<Note>){
        notes=notes_
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            NoteEditorFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}