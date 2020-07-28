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
 * Use the [ForgetPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

lateinit var editTextOTP:EditText
lateinit var editTextNewPassword:EditText
lateinit var editTextConfirmPasswordForgot: EditText
lateinit var forgot_password_fragment_Progressdialog: RelativeLayout

class ForgetPasswordFragment(val contextParam: Context, val mobile_number:String) : Fragment() {
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
        val view=inflater.inflate(R.layout.fragment_forget_password, container, false)

        var buttonSubmit: Button

        editTextOTP=view.findViewById(R.id.editTextOTP)
        editTextNewPassword=view.findViewById(R.id.editTextNewPassword)
        editTextConfirmPasswordForgot=view.findViewById(R.id.editTextConfirmPasswordForgot)
        buttonSubmit=view.findViewById(R.id.buttonSubmit)
        forgot_password_fragment_Progressdialog=view.findViewById(R.id.forgot_password_fragment_Progressdialog)



        buttonSubmit.setOnClickListener(View.OnClickListener {
            if(editTextOTP.text.isBlank()){
                editTextOTP.setError("OTP missing")
            }else{
                if(editTextNewPassword.text.isBlank())
                {
                    editTextNewPassword.setError("Password Missing")
                }else{
                    if(editTextConfirmPasswordForgot.text.isBlank()){
                        editTextConfirmPasswordForgot.setError("Confirm Password Missing")
                    }else{
                        if((editTextNewPassword.text.toString().toInt()==editTextConfirmPasswordForgot.text.toString().toInt()))
                        {
                            if (ConnectionManager().checkConnectivity(activity as Context)) {

                                forgot_password_fragment_Progressdialog.visibility=View.VISIBLE
                                try {

                                    val loginUser = JSONObject()

                                    loginUser.put("mobile_number", mobile_number)
                                    loginUser.put("password", editTextNewPassword.text.toString())
                                    loginUser.put("otp", editTextOTP.text.toString())

                                    val queue = Volley.newRequestQueue(activity as Context)

                                    val url = " http://13.235.250.119/v2/reset_password/fetch_result"

                                    val jsonObjectRequest = object : JsonObjectRequest(
                                        Request.Method.POST,
                                        url,
                                        loginUser,
                                        Response.Listener {

                                            val responseJsonObjectData = it.getJSONObject("data")

                                            val success = responseJsonObjectData.getBoolean("success")

                                            if (success) {

                                                val serverMessage=responseJsonObjectData.getString("successMessage")

                                                Toast.makeText(
                                                    contextParam,
                                                    serverMessage,
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                passwordChanged()

                                            } else {
                                                val responseMessageServer =
                                                    responseJsonObjectData.getString("errorMessage")
                                                Toast.makeText(
                                                    contextParam,
                                                    responseMessageServer.toString(),
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                            }
                                            forgot_password_fragment_Progressdialog.visibility=View.INVISIBLE
                                        },
                                        Response.ErrorListener {

                                            forgot_password_fragment_Progressdialog.visibility=View.INVISIBLE

                                            Toast.makeText(
                                                contextParam,
                                                "mSome Error occurred!!!",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                        }) {
                                        override fun getHeaders(): MutableMap<String, String> {
                                            val headers = HashMap<String, String>()

                                            headers["Content-type"] = "application/json"
                                            headers["token"] = "aeb3d0581d9395"

                                            return headers
                                        }
                                    }


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

                        }else{

                            editTextConfirmPasswordForgot.setError("Passwords don't match")

                        }
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
         * @return A new instance of fragment ForgetPasswordFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForgetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }*/

    fun passwordChanged(){
        val transaction = fragmentManager?.beginTransaction()

        transaction?.replace(
            R.id.frameLayout,
            LoginFragment(contextParam)
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