package br.com.brunofernandowagner.views.main

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.Problem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_problem.view.*

class MainAdapter(

    private val context: Context,
    private val myProblems: List<Problem>,
    private val listener: (Problem) -> Unit

) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val problem = myProblems[position]
        holder.bindView(problem, listener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_problem, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return myProblems.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(problem: Problem,
                     listener: (Problem) -> Unit) = with(itemView) {

            val ivProblem = ivProblem
            val tvTitle = tvTitle
            val tvDetail = tvDetail

            tvTitle.text = problem.title
            tvDetail.text = problem.detail

            if(problem.photo.isNullOrEmpty()) {
                Glide.with(this).load(R.drawable.no_cover_available).into(ivProblem)
            } else {
                Glide.with(this).load(problem.photo).into(ivProblem)
            }

            setOnClickListener { listener(problem) }

        }

    }

}