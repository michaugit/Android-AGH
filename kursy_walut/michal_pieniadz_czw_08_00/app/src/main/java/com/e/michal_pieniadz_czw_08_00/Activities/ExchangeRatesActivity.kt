package com.e.michal_pieniadz_czw_08_00.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError

import com.android.volley.toolbox.JsonArrayRequest
import com.e.michal_pieniadz_czw_08_00.Adapter.CurrencyAdapter
import com.e.michal_pieniadz_czw_08_00.R
import com.e.michal_pieniadz_czw_08_00.TemporaryData
import java.lang.Exception

class ExchangeRatesActivity : AppCompatActivity() {
    internal lateinit var recycler: RecyclerView
    internal lateinit var adapter: CurrencyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange)
        recycler = findViewById(R.id.recycler)
        adapter = CurrencyAdapter(
            TemporaryData.getData(),
            this
        )
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        requestTableA()
    }

    private fun requestTableA(){
        val url = "https://api.nbp.pl/api/exchangerates/tables/A/last/2?format=json"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    println("Success!")
                    TemporaryData.loadData(response, true)
                    adapter.dataSet = TemporaryData.getData()
                    adapter.notifyDataSetChanged()
                    requestTableB()
                },
                Response.ErrorListener { error->
                    handleNetworkError(error, "A")
                }
        )
        TemporaryData.queue.add(jsonObjectRequest)
    }

    private fun requestTableB(){
        val url = "https://api.nbp.pl/api/exchangerates/tables/B/last/2?format=json"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    println("Success!")
                    TemporaryData.loadData(response, false)
                    adapter.dataSet.addAll(TemporaryData.getData())
                    adapter.notifyDataSetChanged()
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
                "A" -> {adapter.dataSet.clear()
                        requestTableA()
                }
                "B" -> requestTableB()
            }
        }
    }


}