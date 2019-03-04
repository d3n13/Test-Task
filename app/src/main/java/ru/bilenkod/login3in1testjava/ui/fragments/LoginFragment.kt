package ru.bilenkod.login3in1testjava.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.view.*
import ru.bilenkod.login3in1testjava.R
import ru.bilenkod.login3in1testjava.login.apis.ApiHolder.Companion.FACEBOOK_API
import ru.bilenkod.login3in1testjava.login.apis.ApiHolder.Companion.GOOGLE_API
import ru.bilenkod.login3in1testjava.login.apis.ApiHolder.Companion.VK_API

class LoginFragment : Fragment() {

    private var listener: LoginFragmentListener? = null

    interface LoginFragmentListener {
        fun initLoginVia(apiKey: Int)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_login, container, false)

        v.loginViaGoogle.setOnClickListener {
            listener?.initLoginVia(GOOGLE_API)
        }
        v.loginViaVk.setOnClickListener {
            listener?.initLoginVia(VK_API)
        }
        v.loginViaFacebook.setOnClickListener {
            listener?.initLoginVia(FACEBOOK_API)
        }

        return v
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginFragmentListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement LoginFragmentListener")
        }
    }
}