package com.example.meuscontatos.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.meuscontatos.controller.AutenticadorFirebase
import com.example.meuscontatos.databinding.ActivityRecuperarSenhaBinding

class RecuperarSenhaActivity : AppCompatActivity() {

    private lateinit var activityRecuperarSenhaBinding: ActivityRecuperarSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityRecuperarSenhaBinding = ActivityRecuperarSenhaBinding.inflate(layoutInflater)
        setContentView(activityRecuperarSenhaBinding.root)
    }

    fun onClick(view: View) {
        if(view == activityRecuperarSenhaBinding.enviarEmailBt){
            val email = activityRecuperarSenhaBinding.emailRecuperacaoSenhaEt.text.toString()
            if(email.isNotBlank() && email.isNotEmpty()){
                AutenticadorFirebase.firebaseAuth.sendPasswordResetEmail(email)
                Toast.makeText(this,"E-mail de recuperacao enviado para o $email.", Toast.LENGTH_SHORT).show()
            }
            finish()
        }
    }
}