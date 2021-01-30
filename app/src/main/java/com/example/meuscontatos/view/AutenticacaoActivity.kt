package com.example.meuscontatos.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.meuscontatos.R
import com.example.meuscontatos.controller.AutenticadorFirebase
import com.example.meuscontatos.databinding.ActivityAutenticacaoBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

class AutenticacaoActivity : AppCompatActivity() {

    private lateinit var activityAutenticacaoBinding: ActivityAutenticacaoBinding

    //Autenticacao pelo google
    private lateinit var googleSignInOptions: GoogleSignInOptions
    private val GOOGLE_SIGN_IN_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityAutenticacaoBinding = ActivityAutenticacaoBinding.inflate(layoutInflater)
        setContentView(activityAutenticacaoBinding.root)

        //Instanciando o GSO - Google Sign In Options
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Instanciado o GSC- Google Sign In Client
        AutenticadorFirebase.googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        activityAutenticacaoBinding.loginGoogleBt.setOnClickListener{
            //Verificar se já existe algupem conectado
            val googleSignInAccount: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
            if(googleSignInAccount == null) {
                //Não existe uma conta Google Logada
                startActivityForResult(AutenticadorFirebase.googleSignInClient?.signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE)
            }
            else{
                //Já existe uma conta google logada
                posLoginSucesso()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){
            //Pegando dados de retorno da intent  de u objeto
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                //Pegando uma conta google a partir do objeto task
                val contaGoogle = task.getResult(ApiException::class.java)
                if (contaGoogle != null){
                    //Extraindo as credenciais da conta google (id token)
                    val credencial = GoogleAuthProvider.getCredential(contaGoogle.idToken, null)

                    AutenticadorFirebase.firebaseAuth.signInWithCredential(credencial).addOnCompleteListener{ task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "Usuario ${contaGoogle.email} autenticado com sucesso", Toast.LENGTH_SHORT).show()
                            posLoginSucesso()
                        } else {
                            Toast.makeText(this, "Falha na autenticação Google 3", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "Falha na autenticação Google 2", Toast.LENGTH_SHORT).show()
                }

            } catch (e: ApiException){
                Toast.makeText(this, "Falha na autenticação Google 1", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onClick(view: View) {

        when(view.id){
            R.id.cadastrarBt -> {
                startActivity(Intent(this,CadastrarActivity::class.java))
            }
            R.id.entrarBt -> {
                val email = activityAutenticacaoBinding.emailEt.text.toString()
                val senha = activityAutenticacaoBinding.senhaEt.text.toString()
                if (email.isBlank() || senha.isBlank()){
                    Toast.makeText(this, "Necessário informar usuário e senha!", Toast.LENGTH_SHORT).show()
                    return
                }
                AutenticadorFirebase.firebaseAuth.signInWithEmailAndPassword(email, senha).addOnSuccessListener {
                    Toast.makeText(this, "Usuario $email autenticado com sucesso", Toast.LENGTH_SHORT).show()
                    posLoginSucesso()
                } .addOnFailureListener{
                    Toast.makeText(this, "Usuario não cadastrado", Toast.LENGTH_SHORT).show()
                    activityAutenticacaoBinding.emailEt.setText("")
                    activityAutenticacaoBinding.senhaEt.setText("")
                    //finish()
                }
            }
            R.id.recuperarSenhaBt -> {
                startActivity(Intent(this,RecuperarSenhaActivity::class.java))
            }
        }

    }

    private fun posLoginSucesso(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}