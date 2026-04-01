package com.example.smartnotebook

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.smartnotebook.databinding.ActivitySobreBinding

class SobreActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySobreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySobreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configurarBotaoVoltar()
        colorirNomeNoTexto()
    }

    // ----------------------------------------------------------------
    // Botão voltar — fecha a Activity e retorna à tela anterior
    // ----------------------------------------------------------------
    private fun configurarBotaoVoltar() {
        binding.btnVoltar.setOnClickListener {
            finish()
        }
    }

    // ----------------------------------------------------------------
    // Colore "Smart Notebook" de roxo no primeiro parágrafo
    // ----------------------------------------------------------------
    private fun colorirNomeNoTexto() {
        val textoOriginal = "O Smart Notebook é uma plataforma dedicada a transformar a rotina de estudos de milhares de alunos."
        val spannable     = SpannableString(textoOriginal)
        val corRoxa       = ContextCompat.getColor(this, R.color.roxo_primario) // #5C6BC0

        val inicio = textoOriginal.indexOf("Smart Notebook")
        val fim    = inicio + "Smart Notebook".length

        // Cor roxa
        spannable.setSpan(
            ForegroundColorSpan(corRoxa),
            inicio, fim,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        // Negrito
        spannable.setSpan(
            StyleSpan(Typeface.BOLD),
            inicio, fim,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.tvDescricao1.text = spannable
    }
}