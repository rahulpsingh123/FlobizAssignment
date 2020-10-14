package com.l.fininassignment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.l.fininassignment.helper.*
import com.l.fininassignment.network.responseModel.UserData
import kotlinx.android.synthetic.main.item_layout.view.*
import kotlinx.android.synthetic.main.item_loading.view.*

class MainAdapter(val activity: MainActivity) : BaseAdapter<MainAdapter.ViewHolder>(activity) {

    private var userDataList = mutableListOf<UserData>()
    private val options = RequestOptions.circleCropTransform()
        .error(R.drawable.ic_person_placeholder)
        .placeholder(R.drawable.ic_person_placeholder)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return userDataList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userData = userDataList[position]
        with(holder.itemView) {
            name.text = userData.first_name + "  " + userData.last_name
            email.text = userData.email
            Glide.with(activity)
                .load(userData.avatar)
                .apply(options)
                .into(add_profile_pic)
        }
    }

    fun setData(it: List<UserData>, clearList: Boolean = false) {
        if (clearList) userDataList.clear()
        userDataList.addAll(it)
        userDataList.addAll(it)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}