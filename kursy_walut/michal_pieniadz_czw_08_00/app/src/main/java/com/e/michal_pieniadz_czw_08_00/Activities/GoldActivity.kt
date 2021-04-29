package com.e.michal_pieniadz_czw_08_00.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.e.michal_pieniadz_czw_08_00.R
import com.e.michal_pieniadz_czw_08_00.TemporaryData
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import org.json.JSONArray

class GoldActivity : AppCompatActivity() {
    internal lateinit var goldRateTV: TextView
    internal lateinit var thirtyDaysChart: LineChart
    internal lateinit var historicRates: ArrayList<Pair<String,Double>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gold)
        goldRateTV = findViewById(R.id.gold_rate_tv)
        thirtyDaysChart = findViewById(R.id.gold_thirty_days_chart)
        setGoldRate()
    }

    private fun setGoldRate() {
        val url = "https://api.nbp.pl/api/cenyzlota/last/30/?format=json"
        val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    println("Success!")
                    loadData(response)
                    showData()
                },
                Response.ErrorListener { error->
                    handleNetworkError(error)
                }
        )
        TemporaryData.queue.add(jsonObjectRequest)
    }

    private fun handleNetworkError(error: VolleyError){
        println(error.toString())
        if (error is TimeoutError) {
            setGoldRate()
        }
    }

    private fun loadData(response: JSONArray){
        val count = response.length()
        historicRates = ArrayList()
        for(i in 0 until count){
            val date = response.getJSONObject(i).getString("data")
            val rate = response.getJSONObject(i).getDouble("cena")
            historicRates.add(Pair(date, rate))
        }
    }

    private fun showData() {
        val text = getString(R.string.gold_rate_tv, historicRates.takeLast(1)[0].second.toString())
        goldRateTV.text = text
        drawChart(thirtyDaysChart, 30)
    }

    private fun drawChart(chart: LineChart, days: Int){
        var entries = ArrayList<Entry>()
        for((index, element) in historicRates.takeLast(days).withIndex()){
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
        description.text = "Kurs złota z ostatnich %d dni".format(days)
        chart.description = description
        chart.xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)
        chart.axisRight.setDrawGridLines(false)
        chart.setBackgroundColor(getColor(R.color.white))
        chart.invalidate()
    }

}