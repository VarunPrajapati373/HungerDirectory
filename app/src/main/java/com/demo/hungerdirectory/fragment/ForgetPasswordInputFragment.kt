package com.demo.hungerdirectory.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.demo.hungerdirectory.R
import com.demo.hungerdirectory.utils.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgetPasswordInputFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
lateinit var editTextMobileNumber: EditText
lateinit var editTextEmail:EditText
lateinit var buttonNext: Button
lateinit var forgot_password_input_fragment_Progressdialog: RelativeLayout

class ForgetPasswordInputFragment(val contextParam: Context) : Fragment() {
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

        val view=inflater.inflate(R.layout.fragment_forget_password_input, container, false)

        editTextMobileNumber=view.findViewById(R.id.editTextMobileNumber)
        editTextEmail=view.findViewById(R.id.editTextEmail)
        buttonNext=view.findViewById(R.id.buttonNext)
        forgot_password_input_fragment_Progressdialog=view.findViewById(R.id.forgot_password_input_fragment_Progressdialog)

        buttonNext.setOnClickListener(View.OnClickListener {

            println("inside click listener next")

            if (editTextMobileNumber.text.isBlank())
            {
                editTextMobileNumber.setError("Mobile Number Missing")
            }else{
                if(editTextEmail.text.isBlank()){
                    editTextEmail.setError("Email Missing")
                }else{

                    if (ConnectionManager().checkConnectivity(activity as Context)) {

                        try {

                            val loginUser = JSONObject()

                            loginUser.put("mobile_number", editTextMobileNumber.text)
                            loginUser.put("email", editTextEmail.text)

                            println(loginUser.getString("mobile_number"))
                            println(loginUser.getString("email"))



                            val queue = Volley.newRequestQueue(activity as Context)

                            val url = " http://13.235.250.119/v2/forgot_password/fetch_result"

                            forgot_password_input_fragment_Progressdialog.visibility=View.VISIBLE

                            val jsonObjectRequest = object : JsonObjectRequest(
                                Request.Method.POST,
                                url,
                                loginUser,
                                Response.Listener {

                                    val responseJsonObjectData = it.getJSONObject("data")

                                    val success = responseJsonObjectData.getBoolean("success")

                                    if (success) {

                                        val first_try=responseJsonObjectData.getBoolean("first_try")

                                        if(first_try==true){
                                            Toast.makeText(
                                                contextParam,
                                                "OTP sent",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            callChangePasswordFragment()//after we get a response we call the Log the user in
                                        }else{
                                            Toast.makeText(
                                                contextParam,
                                                "OTP sent already",
                                                Toast.LENGTH_SHORT
                                            ).show()



                                            callChangePasswordFragment()
                                        }

                                    } else {
                                        val responseMessageServer =
                                            responseJsonObjectData.getString("errorMessage")
                                        Toast.makeText(
                                            contextParam,
                                            responseMessageServer.toString(),
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    }
                                    forgot_password_input_fragment_Progressdialog.visibility=View.INVISIBLE
                                },
                                Response.ErrorListener {
                                    println(it)
                                    Toast.makeText(
                                        contextParam,
                                        "mSome Error occurred!!!",
                                        Toast.LENGTH_SHORT
                                    ).show()


                                    forgot_password_input_fragment_Progressdialog.visibility=View.INVISIBLE

                                }) {
                                override fun getHeaders(): MutableMap<String, String> {
                                    val headers = HashMap<String, String>()

                                    headers["Content-type"] = "application/json"
                                    headers["token"] = "aeb3d0581d9395"

                                    return headers
                                }
                            }
                            jsonObjectRequest.setRetryPolicy( DefaultRetryPolicy(15000,
                                1,
                                1f
                            )
                            )

                            queue.add(jsonObjectRequest)

                        } catch (e: JSONException) {
                            Toast.makeText(
                                contextParam,
                                "Some unexpected error occured!!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }else
                    {
                        val alterDialog=androidx.appcompat.app.AlertDialog.Builder(activity as Context)

                        alterDialog.setTitle("No Internet")
                        alterDialog.setMessage("Internet Connection can't be establish!")
                        alterDialog.setPositiveButton("Open Settings"){text,listener->
                            val settingsIntent= Intent(Settings.ACTION_SETTINGS)//open wifi settings
                            startActivity(settingsIntent)

                        }

                        alterDialog.setNegativeButton("Exit"){ text,listener->
                            ActivityCompat.finishAffinity(activity as Activity)//closes all the instances of the app and the app closes completely
                        }
                        alterDialog.create()
                        alterDialog.show()
                    }
                }
            }
        })

        return view
    }

   /* companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ForgetPasswordInputFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgetPasswordInputFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

    fun callChangePasswordFragment(){
        val transaction = fragmentManager?.beginTransaction()

        transaction?.replace(
            R.id.frameLayout,
            ForgetPasswordFragment(contextParam, editTextMobileNumber.text.toString())
        )//replace the old layout with the new frag  layout

        transaction?.commit()//apply changes
    }


    override fun onResume() {

        if (!ConnectionManager().checkConnectivity(activity as Context)) {

            val alterDialog=androidx.appcompat.app.AlertDialog.Builder(activity as Context)
            alterDialog.setTitle("No Internet")
            alterDialog.setMessage("Internet Connection can't be establish!")
            alterDialog.setPositiveButton("Open Settings"){text,listener->
                val settingsIntent= Intent(Settings.ACTION_SETTINGS)//open wifi settings
                startActivity(settingsIntent)
            }

            alterDialog.setNegativeButton("Exit"){ text,listener->
                ActivityCompat.finishAffinity(activity as Activity)//closes all the instances of the app and the app closes completely
            }
            alterDialog.setCancelable(false)

            alterDialog.create()
            alterDialog.show()

        }

        super.onResume()
    }
}