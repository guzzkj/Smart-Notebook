package com.example.smartnotebook.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smartnotebook.R
import com.example.smartnotebook.SessionManager
import com.example.smartnotebook.databinding.ActivityMenuInstitucionalBinding

// TELA 12: Menu Institucional — acesso ao perfil, links e opção de sair
class MenuInstitucionalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuInstitucionalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuInstitucionalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarItensMenu()
        configurarBottomNav()
    }

    private fun configurarItensMenu() {
        // Card do usuário → abre tela de edição de perfil
        binding.cardUsuario.setOnClickListener {
            startActivity(Intent(this, EditarPerfilActivity::class.java))
        }

        // Item: Sobre o Aplicativo → abre a SobreActivity via Intent explícita
        binding.itemSobre.setOnClickListener {
            startActivity(Intent(this, SobreActivity::class.java))
        }

        // Item: Guia de Produtividade
        binding.itemGuia.setOnClickListener {
            Toast.makeText(this, "Guia em breve", Toast.LENGTH_SHORT).show()
        }

        // Item: Termos de Uso
        binding.itemTermos.setOnClickListener {
            Toast.makeText(this, "Termos de Uso em breve", Toast.LENGTH_SHORT).show()
        }

        // Botão Sair: limpa a sessão local e retorna ao Login
        binding.itemSair.setOnClickListener {
            SessionManager.logout(this)
            val intent = Intent(this, LoginActivity::class.java)
            // FLAG_ACTIVITY_CLEAR_TASK garante que o usuário não volta ao app com o botão Voltar
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    // Configura as 3 abas do BottomNavigationView — aba Menu já está selecionada
    private fun configurarBottomNav() {
        binding.bottomNav.selectedItemId = R.id.nav_menu

        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_inicio -> {
                    val intent = Intent(this, MinhasMateriasActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    startActivity(intent)
                    true
                }
                R.id.nav_calendario -> {
                    startActivity(Intent(this, CalendarioActivity::class.java))
                    true
                }
                R.id.nav_menu -> true // já está no Menu
                else -> false
            }
        }
    }
}
