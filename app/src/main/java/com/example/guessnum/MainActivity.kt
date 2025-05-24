package com.example.guessnum

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var edtNumero: EditText
    private lateinit var btnIntentar: Button
    private lateinit var txtTimer: TextView

    private var numeroSecreto = 0
    private var intentosRestantes = 3

    private var countDownTimer: CountDownTimer? = null
    private val TIEMPO_TOTAL = 60000L // 1 minuto en milisegundos
    private var tiempoRestante = TIEMPO_TOTAL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtNumero = findViewById(R.id.edtNumero)
        btnIntentar = findViewById(R.id.btnIntentar)
        txtTimer = findViewById(R.id.txtTimer)

        iniciarJuego()

        btnIntentar.setOnClickListener {
            val textoBoton = btnIntentar.text.toString()

            if (textoBoton.equals("Intentar", ignoreCase = true)) {
                intentarAdivinar()
            } else {
                iniciarJuego()
            }
        }
    }

    private fun intentarAdivinar() {
        if (intentosRestantes == 0) {
            Toast.makeText(this, "No tienes más intentos", Toast.LENGTH_SHORT).show()
            return
        }

        val texto = edtNumero.text.toString().trim()

        if (texto.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa un número válido", Toast.LENGTH_SHORT).show()
            return
        }

        val intento = texto.toIntOrNull()
        if (intento == null) {
            Toast.makeText(this, "Número inválido", Toast.LENGTH_SHORT).show()
            return
        }

        if (intento < 0 || intento > 100) {
            Toast.makeText(this, "El número debe estar entre 0 y 100", Toast.LENGTH_SHORT).show()
            return
        }

        intentosRestantes--

        if (intento == numeroSecreto) {
            Toast.makeText(this, "¡Felicidades! Adivinaste el número.", Toast.LENGTH_LONG).show()
            terminarJuego()
        } else {
            if (intentosRestantes == 0) {
                Toast.makeText(this, "Se acabaron tus intentos. El número era: $numeroSecreto", Toast.LENGTH_LONG).show()
                terminarJuego()
            } else {
                val pista = if (intento < numeroSecreto) "Mayor" else "Menor"
                Toast.makeText(this, "Incorrecto. El número es $pista. Intentos restantes: $intentosRestantes", Toast.LENGTH_SHORT).show()
            }
        }

        edtNumero.setText("")
    }

    private fun iniciarJuego() {
        numeroSecreto = Random.nextInt(0, 101)
        intentosRestantes = 3
        edtNumero.isEnabled = true
        btnIntentar.isEnabled = true
        btnIntentar.text = "Intentar"
        edtNumero.setText("")

        tiempoRestante = TIEMPO_TOTAL
        iniciarCountDownTimer()
    }

    private fun terminarJuego() {
        edtNumero.isEnabled = false
        btnIntentar.text = "Reintentar"

        countDownTimer?.cancel()
    }

    private fun iniciarCountDownTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(tiempoRestante, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tiempoRestante = millisUntilFinished
                val segundos = (millisUntilFinished / 1000).toInt()
                val minutos = segundos / 60
                val segundosRestantes = segundos % 60
                txtTimer.text = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundosRestantes)
            }

            override fun onFinish() {
                txtTimer.text = "00:00"
                Toast.makeText(this@MainActivity, "Se acabó el tiempo. El número era: $numeroSecreto", Toast.LENGTH_LONG).show()
                terminarJuego()
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}
