package com.example.notely.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notely.databinding.FragmentStartBinding
import com.example.notely.helper.Fonts

class StartFragment : Fragment() {
    private var binding: FragmentStartBinding? = null
    private lateinit var pref: SharedPreferences

    private val TAG = "StartFragment"
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: ")
        val mBinding = FragmentStartBinding.inflate(inflater, container, false)
        binding = mBinding
        binding?.font = Fonts(requireContext())
        binding?.btnStart?.setOnClickListener{ startShared()
            val action =StartFragmentDirections.actionStartFragmentToHomeFragment()
            findNavController().navigate(action)}

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        return mBinding.root
    }

    fun startShared(){
        pref = context?.getSharedPreferences("notely", Context.MODE_PRIVATE)!!
        val editor = pref.edit()
        editor.putBoolean("start", true)
        editor.apply()
    }
}