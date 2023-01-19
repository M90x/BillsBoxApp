package com.example.billsboxproject.Model.Bill

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.billsboxproject.R
import com.example.billsboxproject.View.FavoriteBillsActivity
import com.example.billsboxproject.databinding.BillRowBinding
import kotlinx.android.synthetic.main.bill_row.view.*

class FavBillsAdapter (var activity: FavoriteBillsActivity, private var billsList: List<Bills>):
    RecyclerView.Adapter<FavBillsAdapter.ItemViewHolder>() {
    class ItemViewHolder(binding: BillRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        return ItemViewHolder(
            BillRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val billList = billsList[position]
        holder.itemView.apply {
            tvStoreName.text = billList.storeName
            tvBillDate.text = "Bill Date ${billList.billDate}"

            ///////////////////////////////////////////////
            /////////////// Favorite Bill /////////////////
            if (billList.isFav){
                icFavorite.setImageResource(R.drawable.ic_baseline_favorite_24)
            }

            icFavorite.setOnClickListener {
                if (billList.isFav) {
                    billList.isFav = false
                    activity.updateBill(billList)
                } else {
                    billList.isFav = true
                    activity.updateBill(billList)
                }
            }
            ////////////////////////////////////////////
            ////////////////////////////////////////////

            billCard.setOnClickListener { activity.billDetails(billList) }

            //////////////////////////////////////////////
            /////////////////// Logo Image ///////////////
            val logo : String
            val jarir = "https://i.ibb.co/BTQ2GZc/jarir.jpg"
            val extra = "https://i.ibb.co/26q5FYN/extra.png"
            val stc = "https://i.ibb.co/WxmSrcg/STC.png"
            val ikea = "https://i.ibb.co/qxrtQWH/ikea.png"
            val other = "https://i.ibb.co/f9DBmfs/other.png"

            logo = when(billList.storeName){
                "JARIR Store" -> jarir
                "EXTRA Store" -> extra
                "STC Store" -> stc
                "IKEA Store" -> ikea
                else -> other
            }
            try {
                Glide.with(this)
                    .load(logo)
                    .override(600, 200)
                    .into(ivStore)
            } catch (e:Exception){
                Log.d("Catch", "No image: $e")
            }
        }

    }

    override fun getItemCount() = billsList.size

    fun updateBillsList(billsListUpdate: List<Bills>){
        this.billsList = billsListUpdate
        notifyDataSetChanged()
    }
}