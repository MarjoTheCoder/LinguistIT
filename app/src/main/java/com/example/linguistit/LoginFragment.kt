package com.example.linguistit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import kotlin.properties.ReadWriteProperty

class LoginFragment : Fragment() {

    private var viewModel by viewModels<SigninViewModel>() {

    }

}

