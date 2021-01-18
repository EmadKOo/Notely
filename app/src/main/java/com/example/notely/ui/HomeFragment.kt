package com.example.notely.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notely.R
import com.example.notely.databinding.FragmentHomeBinding
import com.example.notely.db.Database
import com.example.notely.db.Note
import com.example.notely.helper.Fonts

class HomeFragment : Fragment() {
    private  val TAG = "HomeFragment"
    private var binding: FragmentHomeBinding? = null
    private final val CHANNEL_ID = 1012
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mBinding =  FragmentHomeBinding.inflate(inflater, container, false)
        binding = mBinding
        binding?.font = Fonts(requireContext())
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnAddNote?.setOnClickListener {
           addNote()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
        val mList:MutableList<Note> = Database(requireActivity()).getNoteDao().getAllNotes();
        initRecyclerView(mList)
        handleAppearance(mList)
    }

    fun initRecyclerView(mList: MutableList<Note>){
        val adapter = NotesAdapter(mList);
        binding?.notesRecyclerView?.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        binding?.notesRecyclerView?.layoutManager = layoutManager
        binding?.notesRecyclerView?.adapter = adapter
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.notesRecyclerView?.adapter as NotesAdapter
                Database(requireActivity()).getNoteDao().deleteNote(adapter.notes.get(viewHolder.adapterPosition))
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding?.notesRecyclerView)
    }

    fun handleAppearance(mList: MutableList<Note>){
        if(mList.size==0){
            (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
            binding?.rootLayout?.setBackgroundResource(R.color.white)
            binding?.emptyImage?.visibility = View.VISIBLE
            binding?.noItemTV?.visibility = View.VISIBLE
            binding?.emptyImage?.setOnClickListener{addNote()}
            binding?.noItemTV?.setOnClickListener{addNote()}
        }else{
            (activity as AppCompatActivity?)!!.supportActionBar!!.show()
            binding?.rootLayout?.setBackgroundResource(R.drawable.background)
            binding?.emptyImage?.visibility = View.GONE
            binding?.noItemTV?.visibility = View.GONE
        }
    }

    fun addNote(){
        val action = HomeFragmentDirections.actionAddNote()
        findNavController().navigate(action)
    }


}