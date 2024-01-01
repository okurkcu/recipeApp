package com.arincdogansener.finalrecipeapp.adapter

import android.content.Context
import android.media.Image
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
import com.arincdogansener.finalrecipeapp.entities.Category
import com.arincdogansener.finalrecipeapp.entities.CategoryItems

class MainCategoryAdapter:RecyclerView.Adapter<MainCategoryAdapter.RecipeViewHolder>() {

    var listener: OnItemClickListener? = null
    var ctx:Context? = null
    var arrMainCategory = ArrayList<CategoryItems>()
    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){
    }

    fun setData(arrData : List<CategoryItems>){
        arrMainCategory = arrData as ArrayList<CategoryItems>
    }

    fun setClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(item_rv_main_category,parent,false))
    }

    override fun getItemCount(): Int {
        return arrMainCategory.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val dishNameTextView = holder.itemView.findViewById<TextView>(R.id.tv_dish_name)
        val dishImage = holder.itemView.findViewById<ImageView>(R.id.img_dish)
        dishNameTextView.text = arrMainCategory[position].strCategory

        Glide.with(ctx!!).load(arrMainCategory[position].strCategoryThumb).into(dishImage)
        holder.itemView.rootView.setOnClickListener {
            listener!!.onClicked(arrMainCategory[position].strCategory)
        }
    }

    interface OnItemClickListener{
        fun onClicked(categoryName:String)
    }
}