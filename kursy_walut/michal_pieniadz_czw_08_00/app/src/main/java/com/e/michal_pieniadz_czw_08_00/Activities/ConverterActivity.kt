package com.e.michal_pieniadz_czw_08_00.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.widget.addTextChangedListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.e.michal_pieniadz_czw_08_00.CurrencyDetails
import com.e.michal_pieniadz_czw_08_00.R
import com.e.michal_pieniadz_czw_08_00.TemporaryData

class ConverterActivity : AppCompatActivity() {
    internal lateinit var spinner: Spinner
    internal lateinit var plnET: EditText
    internal lateinit var otherET: EditText
    internal  lateinit var  dataSet: ArrayList<CurrencyDetails>
    internal var isPlnLastFocused: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_converter)
        plnET = findViewById(R.id.first_pln_et)
        otherET = findViewById(R.id.second_value_et)
        spinner = findViewById(R.id.spinner)
        requestTableA()
        setListeners()
    }

    private fun setListeners() {
        plnET.addTextChangedListener {if(currentFocus == plnET){
            isPlnLastFocused = true
            evaluate()}}

        otherET.addTextChangedListener {if(currentFocus == otherET){
            isPlnLastFocused = false
            evaluate()}}

        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                evaluate()
            }
        }
    }

    private fun evaluate(){
        val rate = getCurrencyRateSelctedItem()
        if(isPlnLastFocused){
            var plnText = plnET.text.toString()
            if(plnText.isNotEmpty()){
                if(plnText.startsWith('.')){
                    plnText = "0$plnText"
                }
                val otherValue = rate * plnText.toDouble()
                otherET.setText(otherValue.toString())
            }else{
                otherET.setText("0")
            }
        }else{
            var otherText = otherET.text.toString()
            if(otherText.isNotEmpty()){
                if(otherText.startsWith('.')){
                    otherText = "0$otherText"
                }
                val plnValue = rate * otherText.toDouble()
                plnET.setText(plnValue.toString())
            }else{
                plnET.setText("0")
            }
        }
    }

    private fun getCurrencyRateSelctedItem(): Double{
        return dataSet[spinner.selectedItemPosition].rate
    }

    private fun addItemsToSpiner() {
        var itemList = ArrayList<String>()
        for( currency in dataSet){
            itemList.add(currency.currencyCode + " (" +currency.rate +"): " )
        }
        val dataAdapter = ArrayAdapter<String>(this, R.layout.spinner_item, itemList)
        spinner.adapter = dataAdapter
    }

    private fun requestTableA(){
        val url = "https://api.nbp.pl/api/exchangerates/tables/A?format=json"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    println("Success!")
                    TemporaryData.loadData(response, true)
                    dataSet = TemporaryData.getData()
                    addItemsToSpiner()
                    requestTableB()
                },
                Response.ErrorListener { error->
                    handleNetworkError(error, "A")
                }
        )
        TemporaryData.queue.add(jsonObjectRequest)
    }

    private fun requestTableB(){
        val url = "https://api.nbp.pl/api/exchangerates/tables/B?format=json"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    println("Success!")
                    TemporaryData.loadData(response, false)
                    dataSet.addAll(TemporaryData.getData())
                    addItemsToSpiner()
                },
                Response.ErrorListener { error->
                    handleNetworkError(error, "B")
                }
        )
        TemporaryData.queue.add(jsonObjectRequest)
    }

    private fun handleNetworkError(error: VolleyError, typeTable: String){
        println(error.toString())
        if (error is TimeoutError) {
            when (typeTable) {
                "A" -> {dataSet.clear()
                    requestTableA()
                }
                "B" -> requestTableB()
            }
        }
    }
}