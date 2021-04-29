package com.e.michal_pieniadz_czw_08_00.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.e.michal_pieniadz_czw_08_00.CurrencyDetails
import com.e.michal_pieniadz_czw_08_00.R
import com.e.michal_pieniadz_czw_08_00.TemporaryData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class DetailsActivity : AppCompatActivity() {
    internal lateinit var currencyName: TextView
    internal lateinit var todayRate: TextView
    internal lateinit var yesterdayRate: TextView
    internal lateinit var sevenDaysChart: LineChart
    internal lateinit var thirtyDaysChart: LineChart
    internal lateinit var currencyCode: String
    internal var isTableA: Boolean = true
    internal lateinit var currency: CurrencyDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        initialize()
        getData()
    }

    private fun initialize() {
        currencyName = findViewById(R.id.currency_name)
        todayRate = findViewById(R.id.today_rate)
        yesterdayRate = findViewById(R.id.yesterday_rate)
        sevenDaysChart = findViewById(R.id.seven_days_chart)
        thirtyDaysChart = findViewById(R.id.thirty_days_chart)
        currencyCode = intent.getStringExtra("currencyCode") ?: "USD"
        isTableA = intent.getBooleanExtra("isTableA", true)
    }

    private fun getData(){
        var url = "https://api.nbp.pl/api/exchangerates/rates/%s/%s/last/30?format=json"
        if(isTableA){
            url = url.format("A", currencyCode)
        }else{
            url = url.format("B", currencyCode)
        }
        Log.i("url", url)
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                println("Success!")
                currency = TemporaryData.loadDataForOneCurrency(response, isTableA)!!
                showData()
            },
            Response.ErrorListener { error->
                handleNetworkError(error)
            }
        )
        TemporaryData.queue.add(jsonObjectRequest)
    }

    private fun showData() {
        currencyName.text = currency.currencyCode
        todayRate.text = getString(R.string.today_rate_tv, currency.getTodayRate().toString())
        yesterdayRate.text = getString(R.string.yesterday_rate_tv, currency.getYesterdayRate().toString())
        drawChart(sevenDaysChart, 7)
        drawChart(thirtyDaysChart, 30)
    }

    private fun drawChart(chart: LineChart, days: Int){
        var entries = ArrayList<Entry>()
        for((index, element) in currency.historicRates!!.takeLast(days).withIndex()){
            entries.add(Entry(index.toFloat(), element.second.toFloat()))
        }

        val entriesDataSet = LineDataSet(entries, "Kurs $days dni:")

        entriesDataSet.setDrawValues(false)
        entriesDataSet.lineWidth = 6f
        entriesDataSet.color = R.color.purple_700
        entriesDataSet.circleRadius = 8f
        entriesDataSet.setCircleColor(R.color.purple_500)

        val lineData = LineData(entriesDataSet)
        chart.data = lineData
        val description = Description()
        description.text = "Kurs %s z ostatnich %d dni".format(currency.currencyCode, days)
        chart.description = description
        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)
        chart.setBackgroundColor(getColor(R.color.white))
        chart.invalidate()
    }

    private fun handleNetworkError(error: VolleyError){
        println(error.toString())
        if (error is TimeoutError) {
            getData()
        }
    }



}