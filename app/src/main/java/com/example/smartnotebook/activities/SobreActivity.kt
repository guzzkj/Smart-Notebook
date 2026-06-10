package com.example.smartnotebook.activities

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smartnotebook.R
import com.example.smartnotebook.databinding.ActivitySobreBinding

// Tela Sobre — informações do aplicativo, versão e funcionalidades
class SobreActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySobreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySobreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotaoVoltar()
        colorirNomeNoTexto()
    }

    private fun configurarBotaoVoltar() {
        binding.btnVoltar.setOnClickListener { finish() }
    }

    // Colore "Smart Notebook" de roxo e negrito no primeiro parágrafo
    private fun colorirNomeNoTexto() {
        val textoOriginal = "O Smart Notebook é uma plataforma dedicada a transformar a rotina de estudos de milhares de alunos."
        val spannable = SpannableString(textoOriginal)
        val corRoxa = ContextCompat.getColor(this, R.color.roxo_primario)

        val inicio = textoOriginal.indexOf("Smart Notebook")
        val fim    = inicio + "Smart Notebook".length

        spannable.setSpan(ForegroundColorSpan(corRoxa), inicio, fim, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(StyleSpan(Typeface.BOLD),      inicio, fim, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        binding.tvDescricao1.text = spannable
    }
}
