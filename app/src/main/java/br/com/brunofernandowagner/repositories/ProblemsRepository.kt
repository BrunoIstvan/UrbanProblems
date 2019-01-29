package br.com.brunofernandowagner.repositories

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import br.com.brunofernandowagner.models.Problem


class ProblemsRepository {

    fun listAllProblemsByUser(uid: String,
                              onComplete: (ArrayList<Problem>) -> Unit,
                              onError: (String) -> Unit) {

        val finalList = ArrayList<Problem>()

//        FirebaseDatabase.getInstance().getReference("Problems").child(uid)
//            .addValueEventListener(object : ValueEventListener {
//                override fun onCancelled(error: DatabaseError) { onError(error.message) }
//
//                override fun onDataChange(snapshot: DataSnapshot) {
//
//                    snapshot.children.mapNotNullTo(finalList) { it.getValue<Problem>(Problem::class.java) }
//                    onComplete(finalList)
//
//                }
//
//            })

        onComplete(finalList)


    }


}