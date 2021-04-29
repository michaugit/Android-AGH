package com.e.michal_pieniadz_czw_08_00

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import com.blongho.country_data.Country
import com.blongho.country_data.World
import org.json.JSONArray
import org.json.JSONObject
import java.security.KeyStore
import java.text.FieldPosition
import java.util.*
import kotlin.collections.ArrayList

object TemporaryData {
    private lateinit var data: ArrayList<CurrencyDetails>
    lateinit var  queue: RequestQueue
    private lateinit var flags: List<Country>

    fun prepare(context: Context){
        World.init(context)
        queue = newRequestQueue(context)
        val countriesList = World.getAllCountries()
        flags = (countriesList?.distinctBy{ it.currency.code } ?: emptyList<Country>())
        data = ArrayList()
    }

    fun getData(): ArrayList<CurrencyDetails>{
        return data
    }

    fun loadData(response: JSONArray?, isTableA: Boolean) {
        response?.let {
            if (response.length() == 2) {
                loadDataWithPreviousRate(response, isTableA)
            } else {
                val rates = response.getJSONObject(0).getJSONArray("rates")
                val ratesCount = rates.length()
                val tmpData = ArrayList<CurrencyDetails>()
                for (i in 0 until ratesCount) {
                    val currencyName = rates.getJSONObject(i).getString("currency")
                    val currencyCode = rates.getJSONObject(i).getString("code")
                    val currencyRate = rates.getJSONObject(i).getDouble("mid")
                    val flag = (flags.find { it.currency.code == currencyCode })?.flagResource
                            ?: World.getWorldFlag()
                    val currencyObject = CurrencyDetails(currencyCode, currencyName, currencyRate, flag, isTableA)
                    tmpData.add(currencyObject)
                }
                this.data = tmpData
            }
        }
    }

    fun loadDataWithPreviousRate(response: JSONArray?, isTableA: Boolean) {
        response?.let {
                val rates = response.getJSONObject(0).getJSONArray("rates")
                val ratesCount = rates.length()
                val tmpData = ArrayList<CurrencyDetails>()
                val ratesPrev = response.getJSONObject(1).getJSONArray("rates")
                for (i in 0 until ratesCount) {
                    val currencyName = rates.getJSONObject(i).getString("currency")
                    val currencyCode = rates.getJSONObject(i).getString("code")
                    val currencyRatePrev = rates.getJSONObject(i).getDouble("mid")
                    val currencyRate = ratesPrev.getJSONObject(i).getDouble("mid")
                    val rateIncrease: Boolean = currencyRate > currencyRatePrev
                    val flag = (flags.find { it.currency.code == currencyCode })?.flagResource
                            ?: World.getWorldFlag()
                    val currencyObject = CurrencyDetails(currencyCode, currencyName, currencyRate, flag, isTableA, rateIncrease)
                    tmpData.add(currencyObject)
                }
                this.data = tmpData
        }
    }

    fun loadDataForOneCurrency(response: JSONObject?, isTableA: Boolean): CurrencyDetails?{
        response?.let{
            val currencyName = response.getString("currency")
            val currencyCode = response.getString("code")
            val flag = (flags.find { it.currency.code == currencyCode })?.flagResource
                ?: World.getWorldFlag()

            val rates = response.getJSONArray("rates")
            val ratesCount = rates.length()
            val tmpHistoryData = ArrayList<Pair<String, Double>>()

            for(i in 0 until ratesCount){
                val date = rates.getJSONObject(i).getString("effectiveDate")
                val currencyRate = rates.getJSONObject(i).getDouble("mid")
                tmpHistoryData.add(Pair(date, currencyRate))
            }
            return CurrencyDetails(currencyCode, currencyName, tmpHistoryData.last().second, flag, isTableA, tmpHistoryData)
        }
        return null
    }
}

