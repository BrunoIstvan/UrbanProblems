package br.com.brunofernandowagner.views.useful_telephones

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.brunofernandowagner.R
import br.com.brunofernandowagner.models.UsefulTelephone
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_useful_telephone.view.*

class UsefulTelephonesAdapter(

    private val context: Context,
    private val telephones: ArrayList<UsefulTelephone>,
    private val listener: (UsefulTelephone) -> Unit

) : RecyclerView.Adapter<UsefulTelephonesAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val telephone = telephones[position]
        holder.bindView(telephone, listener)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_useful_telephone, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount(): Int {

        return telephones.size

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(telephone: UsefulTelephone,
                     listener: (UsefulTelephone) -> Unit) = with(itemView) {

            val ivUsefulTelephone = ivUsefulTelephone
            val tvTelephoneName = tvTelephoneName
            val tvTelephoneNumber = tvTelephoneNumber

            tvTelephoneName.text = telephone.name
            tvTelephoneNumber.text = telephone.number.toString()

            Glide.with(this).load(telephone.image).into(ivUsefulTelephone)

            setOnClickListener { listener(telephone) }

        }

    }

}