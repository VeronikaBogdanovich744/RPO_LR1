package com.example.lr1_rpo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.app.NotificationCompat.getColor
import com.example.lr1_rpo.data.*
import androidx.recyclerview.widget.RecyclerView
import com.example.lr1_rpo.R
import kotlinx.android.synthetic.main.note_item.view.*

class NotesAdapter():
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {
    var listener: OnItemClickListener? = null
    var notesList : MutableList<Note> = arrayListOf()

    class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view){
    }

    fun setData(notes: MutableList<Note>){
        notesList = notes;
    }

    fun setOnClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    //создание представления элeмента в recycleview
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item,parent,false)
        )
    }
//связывание данных с представлением
    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.tvTitle.text = notesList[position].title

        if(notesList[position].isSelected==true){
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#d47d7d"))
        }else{
            holder.itemView.cardView.setCardBackgroundColor(Color.parseColor("#E8C4BB"))
        }

        holder.itemView.cardView.setOnClickListener {
            listener!!.onClicked(position)
        }

        holder.itemView.cardView.setOnLongClickListener{
            listener!!.onLongClicked(position)
                true
        }
    }

   /* fun removeItem(position: Int) {
        notesList.removeAt(position)
        notifyDataSetChanged()
       // notifyItemChanged(position)
    }*/

    override fun getItemCount(): Int {
        return notesList.size
    }

    interface OnItemClickListener{
        fun onClicked(noteId:Int)
        fun onLongClicked(noteId:Int)
    }
}

