package com.example.lr1_rpo

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.lr1_rpo.adapter.NotesAdapter
import com.example.lr1_rpo.controller.FileScanner
import kotlinx.android.synthetic.main.fragment_home.*
import com.example.lr1_rpo.data.Note
import kotlinx.android.synthetic.main.fragment_note_editor.*
import kotlinx.android.synthetic.main.note_item.*
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment : Fragment() {
    var notesAdapter: NotesAdapter = NotesAdapter()
    var notes= mutableListOf<Note>()
    var searchedNotes = ArrayList<Note>()
    // var selectedNotes = mutableListOf<Note>()
    var isSelectMode = false

    companion object {
        @JvmStatic
        fun newInstance() =
            HomeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private val onClicked = object: NotesAdapter.OnItemClickListener{
        override fun onClicked(noteId: Int) {
            val id:Int
            if(isSelectMode==true){
                if(searchedNotes.size!=0){
                    //find id of choosed note in searched note list
                    id =  notes.indexOf(searchedNotes[noteId])
                }else{
                    id=noteId
                }
                if(notes[id].isSelected==false) {
                    notes[id].isSelected = true
                }else {
                    notes[id].isSelected = false
                }

                val findedElement = notes.find {
                    it.isSelected == true

                }
                if (findedElement == null) {
                    isSelectMode=false
                    fabBtnCreateNote.setVisibility(View.VISIBLE)
                    fabDeleteNotes.setVisibility(View.GONE)
                }


                notesAdapter.notifyDataSetChanged()
            }else {
                val fragement = NoteEditorFragment.newInstance()
                if(searchedNotes.size!=0){
                   id =  notes.indexOf(searchedNotes[noteId])
                }else{
                    id=noteId
                }
                fragement.setNote(notes, id)
                replaceFragment(fragement, true)
            }
        }

        override fun onLongClicked(noteId: Int) {
            val id:Int
            isSelectMode=true
            if(searchedNotes.size!=0){
                id =  notes.indexOf(searchedNotes[noteId])
            }else{
                id=noteId
            }
            notes[id].isSelected=true
            notesAdapter.notifyDataSetChanged()
            fabBtnCreateNote.setVisibility(View.GONE)
            fabDeleteNotes.setVisibility(View.VISIBLE)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //set columns
        recycler_view.setHasFixedSize(true)
        //два столбца
        recycler_view.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        //read notes from file
        if(notes.size==0) {
            notes = FileScanner.readNotesFromFile("notes.txt", requireContext())
        }
        notesAdapter.setData(notes)
        notesAdapter.setOnClickListener(onClicked)
        recycler_view.adapter = notesAdapter

        fabBtnCreateNote.setOnClickListener {
            val fragement = NoteEditorFragment.newInstance()
            fragement.setNote(notes)
            replaceFragment(fragement,true)
        }

        fabDeleteNotes.setOnClickListener{
            notes.removeIf { x->x.isSelected==true }
            searchedNotes.removeIf{ x->x.isSelected==true }
            notesAdapter.notifyDataSetChanged()
            isSelectMode=false
            fabBtnCreateNote.setVisibility(View.VISIBLE)
            fabDeleteNotes.setVisibility(View.GONE)
            FileScanner.writeNotesToFile("notes.txt",notes, requireContext())
        }

        search_view.setOnQueryTextListener( object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                searchedNotes.clear()
               // var tempArr = ArrayList<Note>()
                for (arr in notes) {
                    if (arr.title!!.lowercase(Locale.getDefault()).contains(p0.toString())) {
                        searchedNotes.add(arr)
                    }
                }

                notesAdapter.setData(searchedNotes)
                notesAdapter.notifyDataSetChanged()
                return true
            }
        })
    }

    fun replaceFragment(fragment:Fragment, istransition:Boolean){

        val act = requireActivity()
        val fragmentTransition= act.supportFragmentManager.beginTransaction()

        if (istransition){
            fragmentTransition.setCustomAnimations(android.R.anim.slide_out_right,android.R.anim.slide_in_left)
        }
        fragmentTransition.replace(R.id.frame_layout,fragment).addToBackStack(fragment.javaClass.simpleName).commit()
    }
}