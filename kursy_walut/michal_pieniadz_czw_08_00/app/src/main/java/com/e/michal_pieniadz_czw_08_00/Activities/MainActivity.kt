package com.e.michal_pieniadz_czw_08_00.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.e.michal_pieniadz_czw_08_00.R
import com.e.michal_pieniadz_czw_08_00.TemporaryData

class MainActivity : AppCompatActivity() {
    internal lateinit var exchangeButton: Button
    internal lateinit var goldButton: Button
    internal lateinit var converterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        TemporaryData.prepare(this)
        exchangeButton = findViewById(R.id.exchange_btn)
        goldButton = findViewById(R.id.gold_btn)
        converterButton = findViewById(R.id.converter_btn)
        setListeners()
    }

    private fun setListeners() {
        exchangeButton.setOnClickListener {
            val intent = Intent(this, ExchangeRatesActivity::class.java)
            startActivity(intent)
        }

        goldButton.setOnClickListener {
            val intent = Intent(this, GoldActivity::class.java)
            startActivity(intent)
        }

        converterButton.setOnClickListener {
            val intent = Intent(this, ConverterActivity::class.java)
            startActivity(intent)
        }
    }


}