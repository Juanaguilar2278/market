package com.example.appmarket

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmarket.databinding.ActivityRegistroEmailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import java.util.regex.Pattern

class Registro_email : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroEmailBinding

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroEmailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.BtnRegistrar.setOnClickListener {
            validarInfo()
        }

    }

    private var email = ""
    private var password = ""
    private var r_password = ""

    private fun validarInfo() {

        email = binding.EtEmail.text.toString().trim()
        password = binding.EtPassword.text.toString().trim()
        r_password = binding.EtRPassword.text.toString().trim()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            binding.EtEmail.error = "Email inválido"
            binding.EtEmail.requestFocus()

        } else if (email.isEmpty()) {

            binding.EtEmail.error = "Ingrese Email"
            binding.EtEmail.requestFocus()

        } else if (password.isEmpty()) {

            binding.EtPassword.error = "Ingrese Password"
            binding.EtPassword.requestFocus()

        } else if (r_password.isEmpty()) {

            binding.EtRPassword.error = "Repita el Password"
            binding.EtRPassword.requestFocus()
        }
        else if (password != r_password) {

            binding.EtRPassword.error = "No coinciden"
            binding.EtRPassword.requestFocus()
        }
        else{
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {
        progressDialog.setMessage("Creando cuenta")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email,password)

            .addOnSuccessListener {
            llenarInfoBD()
            }

            .addOnFailureListener {e->
                progressDialog.dismiss()
                Toast.makeText(
                    this,
                    "No se registró el usuario debido a ${e.message}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun llenarInfoBD() {


    }
}