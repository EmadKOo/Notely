package com.example.notely.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.notely.R
import com.example.notely.databinding.FragmentUpdateNoteBinding
import com.example.notely.db.Database
import com.example.notely.db.Note
import com.example.notely.helper.Fonts
import com.example.notely.services.NotifierWorker
import com.example.notely.utils.Helper
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_update_note.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class UpdateNoteFragment : Fragment() {

    private var binding: FragmentUpdateNoteBinding? = null
    private lateinit var note :Note

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mBinding =  FragmentUpdateNoteBinding.inflate(inflater, container, false)
        binding = mBinding
        binding?.font = Fonts(requireContext())
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note = arguments?.let { UpdateNoteFragmentArgs.fromBundle(it).note }!!
        binding?.note = note
        Log.d("TAG","onOptionsItemSelected: " + note.isRemainder)
        updateNote()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true);
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
        if (note.isRemainder)
            menu.findItem(R.id.notify).setIcon(R.drawable.bell)
        else
            menu.findItem(R.id.notify).setIcon(R.drawable.silent)
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.notify) {
            //change the icon first
            if (note.isRemainder){// bell icon => change to silent
                item.setIcon(R.drawable.silent)
                note.isRemainder=false
                // user selects time then start worker class

            }else{
                item.setIcon(R.drawable.bell)
                note.isRemainder= true
                note.remainderTime =0
                MyDatePicker()
            }
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    fun updateNote(){
        binding?.btnSaveNote?.setOnClickListener{
            val updatedNote = Note(binding?.titleEditText?.text.toString(),
                binding?.descriptionEditText?.text.toString(),note.isRemainder,
                note.remainderTime,Helper().getTimeNow())
            updatedNote.id= note.id
            Log.d("TAG", "updateNote: " + updatedNote.remainderTime)
            Database(requireActivity()).getNoteDao().update(updatedNote)
            val actionUpdate = UpdateNoteFragmentDirections.actionUpdateNoteFragmentToHomeFragment()
            findNavController().navigate(actionUpdate)
        }
    }

    fun initWorker(days: Long) {
        val data = workDataOf("title" to note.title, "content" to note.note)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val notifierRequest = OneTimeWorkRequestBuilder<NotifierWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .setInitialDelay(days, TimeUnit.SECONDS)
            //.setInitialDelay(days, TimeUnit.DAYS)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(notifierRequest)
    }

    fun MyDatePicker() {
        val c = Calendar.getInstance()
        val cal = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd/MM/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                Log.d("TAG", "calculateDifference: Time " + sdf.format(Date().time))
                Log.d("TAG", "calculateDifference: " + sdf.format(cal.time))
                note.remainderTime = getDaysDifference(sdf.format(Date().time), sdf.format(cal.time)).toLong()
                initWorker(1)
               // initWorker(getDaysDifference(sdf.format(Date().time), sdf.format(cal.time)).toLong())

            },
            year,
            month,
            day
        )
        dpd.show()
    }

    fun getDaysDifference(currentDate: String, finalDate: String):Int{
        val date1: Date
        val date2: Date
        val dates = SimpleDateFormat("dd/MM/yyyy")
        date1 = dates.parse(currentDate)
        date2 = dates.parse(finalDate)
        val dayDifference = ((Math.abs(date1.time - date2.time)) / (24 * 60 * 60 * 1000)).toString()
        Log.d("TAG", "getDaysDifference: " + dayDifference)
        Log.d("TAG", "getDaysDifference ABS : " + (Math.abs(date1.time - date2.time)))
        return dayDifference.toInt()
    }
}