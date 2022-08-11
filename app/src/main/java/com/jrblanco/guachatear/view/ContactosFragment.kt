package com.jrblanco.guachatear.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.FragmentContactosBinding

class ContactosFragment : Fragment() {

    private lateinit var binding: FragmentContactosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactosBinding.inflate(layoutInflater)



        return binding.root
    }

}