package com.arincdogansener.finalrecipeapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arincdogansener.finalrecipeapp.R
import com.arincdogansener.finalrecipeapp.R.layout.item_rv_main_category
import com.arincdogansener.finalrecipeapp.entities.Recipies
import com.bumptech.glide.Glide
import android.widget.TextView
import com.arincdogansener.finalrecipeapp.R.layout.item_rv_sub_category
import com.arincdogansener.finalrecipeapp.entities.MealsItems

class SubCategoryAdapter:RecyclerView.Adapter<SubCategoryAdapter.RecipeViewHolder>() {

    var listener: SubCategoryAdapter.OnItemClickListener? = null
    var arrSubCategory = ArrayList<MealsItems>()
    var ctx: Context? = null
    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){
    }

    fun setData(arrData : List<MealsItems>){
        arrSubCategory = arrData as ArrayList<MealsItems>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(item_rv_sub_category,parent,false))
    }

    override fun getItemCount(): Int {
        return arrSubCategory.size
    }

    fun setClickListener(listener1: SubCategoryAdapter.OnItemClickListener){
        listener = listener1
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val dishNameTextView = holder.itemView.findViewById<TextView>(R.id.tv_dish_name)
        val dishImage = holder.itemView.findViewById<ImageView>(R.id.img_dish)
        dishNameTextView.text = arrSubCategory[position].strMeal
        Glide.with(ctx!!).load(arrSubCategory[position].strMealThumb).into(dishImage)

        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(arrSubCategory[position].idMeal)
        }
    }

    interface OnItemClickListener{
        fun onClicked(id:String)
    }
}