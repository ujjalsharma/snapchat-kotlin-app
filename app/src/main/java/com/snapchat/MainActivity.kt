package com.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    var emailEditText: EditText? = null
    var passEditText: EditText? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailText);
        passEditText = findViewById(R.id.passText);

        if (mAuth.currentUser != null) {
            logIn()
        }

    }

    fun goClicked(view: View){

        // check if we can login the user
        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passEditText?.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    logIn()
                } else {
                    //sinup the user
                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passEditText?.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Add to database
                            FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid.toString()).child("email").setValue(emailEditText?.text.toString())
                            logIn()
                        } else {
                            Toast.makeText(this, "Login Failed! Try Again!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }



        //sinup the user

    }

    fun logIn() {
        //move to next activity
        val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }

}