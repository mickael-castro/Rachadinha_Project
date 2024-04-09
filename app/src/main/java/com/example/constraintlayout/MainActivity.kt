package com.example.constraintlayout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.controls.actions.FloatAction
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.NullPointerException
import java.util.*

class MainActivity : AppCompatActivity() , TextWatcher, TextToSpeech.OnInitListener {
    private lateinit var tts: TextToSpeech
    private lateinit var edtConta: EditText
    private lateinit var edtGrupo: EditText
    private lateinit var txt_Conta: TextView
    private var ttsSucess: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        edtConta = findViewById<EditText>(R.id.edtConta)
        edtGrupo = findViewById<EditText>(R.id.pessoas_txt)
        txt_Conta = findViewById<EditText>(R.id.txt_conta)
        edtConta.addTextChangedListener(this)
        edtGrupo.addTextChangedListener(this)
        // Initialize TTS engine
        tts = TextToSpeech(this, this)

        val share: FloatingActionButton = findViewById(R.id.btn_Share)

        share.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "O valor da conta rachada é: $ ${txt_Conta.text}")

            // Iniciar a atividade de compartilhamento
            startActivity(Intent.createChooser(intent, "Compartilhar via"))
        }

    }


    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
       Log.d("PDM24","Antes de mudar")

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        Log.d("PDM24","Mudando")
    }

    override fun afterTextChanged(s: Editable?) {
        Log.d ("PDM24", "Depois de mudar")

        val valor: Double

        if(s.toString().length>0) {
             valor = s.toString().toDouble()
            Log.d("PDM24", "v: " + valor)
        //    edtConta.setText("9")
        }

        fazerConta(edtConta,edtGrupo)
    }

    fun fazerConta (valor: EditText, grupo: EditText) {
        val valorConta: Double = valor.text.toString().toDoubleOrNull() ?: 0.0
        val valorGrupo: Double = grupo.text.toString().toDoubleOrNull() ?: 0.0

        if (valorConta == 0.0 || valorGrupo == 0.0) {
            txt_Conta.text = "0"
        } else {
            val rachadinha: Double = valorConta / valorGrupo
            txt_Conta.text = rachadinha.toString()
        }


    }

    fun clickFalar(v: View){
        tts.setLanguage(Locale.ENGLISH)
        if (tts.isSpeaking) {
            tts.stop()
        }
        if(ttsSucess) {
            Log.d ("PDM23", tts.language.toString())
            tts.speak("${txt_Conta.text}", TextToSpeech.QUEUE_FLUSH, null, null)
        }




    }
    override fun onDestroy() {
            // Release TTS engine resources
            tts.stop()
            tts.shutdown()
            super.onDestroy()
        }

    override fun onInit(status: Int) {
            if (status == TextToSpeech.SUCCESS) {
                // TTS engine is initialized successfully
                tts.language = Locale.getDefault()
                ttsSucess=true
                Log.d("PDM23","Sucesso na Inicialização")
            } else {
                // TTS engine failed to initialize
                Log.e("PDM23", "Failed to initialize TTS engine.")
                ttsSucess=false
            }
        }


}

