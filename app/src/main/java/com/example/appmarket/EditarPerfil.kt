package com.example.appmarket

import android.app.ProgressDialog
import android.os.Bundle
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.appmarket.databinding.ActivityEditarPerfilBinding
import com.example.appmarket.databinding.FragmentCuentaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditarPerfil : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarInfo()
        binding.FABCambiarImg.setOnClickListener {
            selec_imagen_de()
        }


    }

    private fun cargarInfo() {
        val ref = FirebaseDatabase.getInstance().getReference("Usuarios")
        ref.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    val nombres = "${snapshot.child("nombre").value}"
                    val imagen = "${snapshot.child("urlImagenPerfil").value}"
                    val f_nac = "${snapshot.child("fecha_nac").value}"
                    val telefono = "${snapshot.child("telefono").value}"
                    val codTelefono = "${snapshot.child("codigoTelefono").value}"

                    //Setear la información

                    binding.EtNombres.setText(nombres)
                    binding.EtFNac.setText(f_nac)
                    binding.EtTelefono.setText(telefono)

                    //Seteamos imagen
                    try {
                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder(R.drawable.img_perfil)
                            .into(binding.imgPerfil)


                    }catch (e:Exception){
                        Toast.makeText(
                            this@EditarPerfil,
                            "${e.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }

                    //codigo de país
                    try {
                        val codigo = codTelefono.replace("+" , "").toInt()
                        binding.selectorCod.setCountryForPhoneCode(codigo)


                    }catch (e:Exception){
                        Toast.makeText(
                            this@EditarPerfil,
                            "${e.message}",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun selec_imagen_de(){
        val popupMenu = PopupMenu (this, binding. FABCambiarImg)

        popupMenu.menu.add(Menu.NONE, 1, 1, "Cámara")
        popupMenu.menu.add(Menu.NONE, 2, 2, "Galería")

        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {item->
            val itemId =item.itemId
            if (itemId==1){
                //Cámara
            }else if (itemId==2){
                //Galería
            }

            return@setOnMenuItemClickListener true
        }

    }
}