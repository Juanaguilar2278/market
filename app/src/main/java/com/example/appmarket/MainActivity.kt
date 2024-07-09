package com.example.appmarket

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appmarket.Fragmentos.FragmentChats
import com.example.appmarket.Fragmentos.FragmentCuenta
import com.example.appmarket.Fragmentos.FragmentInicio
import com.example.appmarket.Fragmentos.FragmentMisAnuncios
import com.example.appmarket.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    //private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)



        firebaseAuth =  FirebaseAuth.getInstance()
        comprobarSesion()
        verFragmentInicio()


        binding.BottomNV.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.Item_Inicio->{
                    verFragmentInicio()
                    true
                }
                R.id.Item_Chats->{
                    verFragmentChats()
                    true
                }
                R.id.Item_Mis_Anuncios->{
                    verFragmentMisAnuncios()
                    true
                }
                R.id.Item_Cuenta->{
                    verCuenta()
                    true
                }
                else->{
                    false
                }
            }

        }
    }
    private fun comprobarSesion(){
        if (firebaseAuth.currentUser == null){
            startActivity(Intent(this, OpcionesLogin::class.java))
            finishAffinity()
        }
    }

    private fun verFragmentInicio(){
        binding.TituloRl.text = "Inicio"
        val fragment = FragmentInicio ()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentInicio")
        fragmentTransition.commit()

    }
    private fun verFragmentChats(){
        binding.TituloRl.text = "Chats"
        val fragment = FragmentChats ()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentChats")
        fragmentTransition.commit()

    }
    private fun verFragmentMisAnuncios(){
        binding.TituloRl.text = "Mis Anuncios"
        val fragment = FragmentMisAnuncios ()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentMisAnuncios")
        fragmentTransition.commit()

    }
    private fun verCuenta(){
        binding.TituloRl.text = "Cuenta"
        val fragment = FragmentCuenta ()
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.replace(binding.FragmentL1.id, fragment, "FragmentCuenta")
        fragmentTransition.commit()

    }
}