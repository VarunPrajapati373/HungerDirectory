package com.demo.hungerdirectory.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.demo.hungerdirectory.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var textViewName:TextView
lateinit var textViewEmail:TextView
lateinit var textViewMobileNumber:TextView
lateinit var textViewAddress: TextView

class ProfileFragment(val contextParam: Context) : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_profile, container, false)

        textViewName=view.findViewById(R.id.textViewName)
        textViewEmail=view.findViewById(R.id.textViewEmail)
        textViewMobileNumber=view.findViewById(R.id.textViewMobileNumber)
        textViewAddress=view.findViewById(R.id.textViewAddress)

        val sharedPreferencess=contextParam.getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE)

        textViewName.text=sharedPreferencess.getString("name","")
        textViewEmail.text=sharedPreferencess.getString("email","")
        textViewMobileNumber.text="+91-"+sharedPreferencess.getString("mobile_number","")
        textViewAddress.text=sharedPreferencess.getString("address","")

        return view
    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

}