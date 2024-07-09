package com.example.appmarket

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmarket.Opciones_login.Login_email
import com.example.appmarket.databinding.ActivityOpcionesLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class OpcionesLogin : AppCompatActivity() {

    private lateinit var binding : ActivityOpcionesLoginBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleSingnInClient: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpcionesLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
        comprobarSesion()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSingnInClient = GoogleSignIn.getClient(this,gso)

        binding.IngresarEmail.setOnClickListener{
            startActivity(Intent(this@OpcionesLogin, Login_email::class.java))
        }
        binding.IngresarGoogle.setOnClickListener{
            googleLogin()
        }

        }

    private fun googleLogin() {
        val googleSignIntent = mGoogleSingnInClient.signInIntent
        googleSignInArl.launch(googleSignIntent)


    }

    private val googleSignInArl = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){resultado ->

        if (resultado.resultCode == RESULT_OK){
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {

                val cuenta = task.getResult(ApiException::class.java)
                autenticacionGoogle(cuenta.idToken)
            }catch (e:Exception){

                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun autenticacionGoogle(idToken: String?) {

        val credential = GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)

            .addOnSuccessListener {resultadoAuth->
                if (resultadoAuth.additionalUserInfo!!.isNewUser){

                    llenaInfoBD()

                }else{

                    startActivity(Intent(this, MainActivity::class.java))
                        finishAffinity()
                }


            }

            .addOnFailureListener {e->
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun llenaInfoBD() {
        progressDialog.setMessage("Guardando información")

        val tiempo = Constantes.obtenerTiempoDis()
        val emailUsuario = firebaseAuth.currentUser!!.email
        val uidUsuario = firebaseAuth.uid
        val nombreUsuario = firebaseAuth.currentUser?.displayName

        val hashMap = HashMap<String, Any>()
        hashMap["nombre"] = "${nombreUsuario}"
        hashMap["codigoTelefono"] = ""
        hashMap["telefono"] = ""
        hashMap["urlImagenPerfil"] = ""
        hashMap["proveedor"] = "Google"
        hashMap["escribiendo"] = ""
        hashMap["tiempo"] = tiempo
        hashMap["email"] = "${emailUsuario}"
        hashMap["uid"] = "${uidUsuario}"
        hashMap["fecha_nac"] = ""

        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child(uidUsuario!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finishAffinity()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se registrón debido a ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun comprobarSesion(){

        if (firebaseAuth.currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finishAffinity()
        }

    }
    }
