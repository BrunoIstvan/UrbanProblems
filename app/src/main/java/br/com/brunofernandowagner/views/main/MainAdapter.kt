package br.com.brunofernandowagner.views.main

import android.content.Context
import android.graphics.Color
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
    private val clickListener: (Problem) -> Unit,
    private val longClickListener: (Problem, Boolean) -> Boolean,
    private val shareClickListener: (Problem) -> Unit

) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val problem = myProblems[position]
        holder.bindView(problem, clickListener, longClickListener, shareClickListener)

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
                     clickListener: (Problem) -> Unit,
                     longClickListener: (Problem, Boolean) -> Boolean,
                     shareClickListener: (Problem) -> Unit) = with(itemView) {

            val ivProblem = ivProblem
            val ibShareProblem = ibShareProblem
            val tvTitle = tvTitle
            val tvDetail = tvDetail

            tvTitle.text = problem.title
            problem.detail?.let {
                tvDetail.text = if (problem.detail.toString().length > 50) {
                    problem.detail.toString().substring(0, 49) } else { problem.detail.toString() }
            } ?: run {
                tvDetail.text = ""
            }

            if(problem.photo.isNullOrEmpty()) {
                Glide.with(this).load(R.drawable.no_cover_available).into(ivProblem)
            } else {
                Glide.with(this).load(problem.photo).into(ivProblem)
            }

            setOnClickListener { clickListener(problem) }
            setOnLongClickListener {
                if(itemView.tag == null || itemView.tag == 0) {
                    itemView.setBackgroundColor(Color.parseColor("#aaaaaa"))
                    itemView.tag = 1
                    longClickListener(problem, true)
                } else {
                    itemView.setBackgroundColor(Color.parseColor("#ffffff"))
                    itemView.tag = 0
                    longClickListener(problem, false)
                }
            }
            ibShareProblem.setOnClickListener { shareClickListener(problem) }

        }

    }

}