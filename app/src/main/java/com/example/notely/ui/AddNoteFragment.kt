package com.example.notely.ui


import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.notely.R
import com.example.notely.databinding.FragmentAddNoteBinding
import com.example.notely.db.Database
import com.example.notely.db.Note
import com.example.notely.helper.Fonts
import com.example.notely.services.NotifierWorker
import com.tapadoo.alerter.Alerter
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

class AddNoteFragment : Fragment() {
    private var binding: FragmentAddNoteBinding? = null
    lateinit var currentNote: Note
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
        binding = mBinding
        binding?.font = Fonts(requireContext())
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btnSaveNote?.setOnClickListener {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
            val currentDate = sdf.format(Date())
            Log.d("TAG", "onViewCreated: " + currentDate)
            currentNote = Note(
                binding?.titleEditText?.text.toString(),
                binding?.descriptionEditText?.text.toString(),
                false,
                0,
                currentDate
            )
            if (currentNote.note == "" || currentNote.title == "") {
                Alerter.create(activity)
                    .setTitle("Attention")
                    .setText("Please mention title and some info to save your note")
                    .setBackgroundResource(R.color.colorGray)
                    .setDuration(2000)
                    .setDismissable(true)
                    .show()

            } else {
                Database(requireActivity()).getNoteDao().addNote(currentNote)
                val action =AddNoteFragmentDirections.actionSaveNote();
                findNavController().navigate(action)
                Alerter.create(activity)
                    .setTitle("Saved")
                    .setText("Note Saved")
                    .setBackgroundResource(R.color.colorAccen3t)
                    .setDuration(2000)
                    .setDismissable(true)
                    .show()

                // scheduleWork(0,2)
            }
        }

    }
}

