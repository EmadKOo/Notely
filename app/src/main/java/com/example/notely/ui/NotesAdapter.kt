package com.example.notely.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.notely.databinding.NoteHolderBinding
import com.example.notely.db.Note
import com.example.notely.helper.Fonts

class NotesAdapter(val notes:MutableList<Note>): RecyclerView.Adapter<NotesAdapter.MyViewHolder>(){
    class MyViewHolder(val noteHolder: NoteHolderBinding): RecyclerView.ViewHolder(noteHolder.root) {

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

     val mNoteHolder: NoteHolderBinding =NoteHolderBinding.inflate(LayoutInflater.from(parent.context), parent,
         false)
        return MyViewHolder(mNoteHolder)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.noteHolder.currentNote = notes.get(position)
        holder.noteHolder.root.setOnClickListener{view ->
            val action = HomeFragmentDirections.actionHomeFragmentToUpdateNoteFragment(notes.get(position))
            view.findNavController().navigate(action)
        }
        holder.noteHolder.font = Fonts( holder.noteHolder.root.context)
    }

    fun removeAt(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }



}