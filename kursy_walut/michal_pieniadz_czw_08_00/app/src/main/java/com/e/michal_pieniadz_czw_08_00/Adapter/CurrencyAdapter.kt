package com.e.michal_pieniadz_czw_08_00.Adapter

import android.content.Context
import android.content.Intent
import android.media.Image
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.michal_pieniadz_czw_08_00.Activities.DetailsActivity
import com.e.michal_pieniadz_czw_08_00.CurrencyDetails
import com.e.michal_pieniadz_czw_08_00.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view){
    val currencyCodeTextView: TextView
    val rateTextView: TextView
    val flagView: ImageView
    val rateArrow: ImageView

    init {
        currencyCodeTextView = view. findViewById(R.id.currency_code)
        rateTextView = view.findViewById(R.id.rate)
        flagView = view.findViewById(R.id.imageView)
        rateArrow = view.findViewById(R.id.rate_arrow)
    }
}

class CurrencyAdapter(var dataSet: ArrayList<CurrencyDetails>, val context: Context): RecyclerView.Adapter<ViewHolder>(){
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.single_element, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currency =  dataSet[position]
        holder.currencyCodeTextView.text = currency.currencyCode
        holder.rateTextView.text = currency.rate.toString()
        holder.flagView.setImageResource(currency.flag)
        holder.itemView.setOnClickListener{goToDetails(position)}
        when (currency.rateIncrease){
            true ->{
                holder.rateArrow.setImageResource(R.drawable.green_arrow)
                holder.rateArrow.visibility = View.VISIBLE
            }
            false ->{
                holder.rateArrow.setImageResource(R.drawable.red_arrow)
                holder.rateArrow.visibility = View.VISIBLE
            }

        }
    }

    private fun goToDetails(position: Int){
        val intent = Intent(context, DetailsActivity::class.java).apply {
            putExtra("currencyCode", dataSet[position].currencyCode)
            putExtra("isTableA", dataSet[position].isTableA)}
        context.startActivity(intent)
    }

    override fun getItemCount(): Int {
       return dataSet.size
    }

}