package com.example.networking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_veiw.view.*

class GithubUsersAdapter(val githubUsers:ArrayList<GithubUser>): RecyclerView.Adapter<GithubUsersAdapter.GithubUserViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            GithubUserViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.item_veiw,parent,false))

    override fun getItemCount(): Int =githubUsers.size

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder?.bind(githubUsers[position])
    }
    class GithubUserViewHolder(itemView:View?):RecyclerView.ViewHolder(itemView!!){

        fun bind(github:GithubUser){

            itemView?.tvlogin?.text=github.login
            itemView?.tvScore?.text=github.score.toString()
            itemView?.tvVeiwUrl.text=github.html_url

        }
    }

}