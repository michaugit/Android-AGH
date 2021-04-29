package com.e.michal_pieniadz_czw_08_00

import com.blongho.country_data.Currency
import com.blongho.country_data.World
import org.json.JSONObject

class CurrencyDetails (var currencyCode: String, var rate: Double, var flag: Int, var isTableA: Boolean? = null, var rateIncrease: Boolean? = null){
    var currencyName: String? = null
    var historicRates: ArrayList<Pair<String, Double>>? = null


    constructor(currency: Currency): this(
        currency.code,
        0.0,
        World.getCountryFrom(currency.country).flagResource
    )

    constructor(currencyCode: String, currencyName: String, rate: Double, flag: Int, isTableA: Boolean) : this(
        currencyCode,
        rate,
        flag,
        isTableA
    ){
        this.currencyName = currencyName
    }

    constructor(currencyCode: String, currencyName: String, rate: Double, flag: Int, isTableA: Boolean, rateIncrease: Boolean) : this(
            currencyCode,
            rate,
            flag,
            isTableA,
            rateIncrease
    ){
        this.currencyName = currencyName
    }

    constructor(currencyCode: String, currencyName: String, rate: Double, flag: Int, isTableA: Boolean, historicRates: ArrayList<Pair<String, Double>>) : this(
        currencyCode,
        rate,
        flag,
        isTableA
    ){
        this.currencyName = currencyName
        this.historicRates = historicRates
    }


    init {
        if( currencyCode == "USD"){
            val usa = World.getCountryFrom(840)
            this.flag = usa.flagResource
        } else if (currencyCode == "EUR"){
            this.flag = R.drawable.eu
        } else if(currencyCode == "GBP"){
            this.flag = R.drawable.gb
        } else if(currencyCode == "HKD"){
            this.flag = R.drawable.hk
        } else if(currencyCode == "CHF"){
            this.flag = R.drawable.ch
        }
    }

    fun getLast(n: Int): ArrayList<Pair<String, Double>>{
        return ArrayList(historicRates?.reversed()?.take(n))
    }

    fun getTodayRate(): Double{
        return this.getLast(1)[0].second
    }
    fun getYesterdayRate(): Double{
        return this.getLast(2)[1].second
    }
}