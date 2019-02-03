package br.com.brunofernandowagner.extensions

import android.app.AlertDialog
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.brunofernandowagner.R
import dmax.dialog.SpotsDialog

var dialog: AlertDialog? = null

fun AppCompatActivity.showShortToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
fun AppCompatActivity.showLongToast(message: String) = Toast.makeText(this, message, Toast.LENGTH_LONG).show()
fun AppCompatActivity.showShortSnack(message: String) = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
fun AppCompatActivity.showLongSnack(message: String) = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()

//fun AppCompatActivity.showActionSnack(message: String,
//                                      onUndoClick: () -> Unit) {
//    val snack = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
//    snack.setAction(AppCtx.getInstance().ctx?.getText(R.string.snack_action_undo)) { onUndoClick() }
//    snack.show()
//}

fun AppCompatActivity.showActionDialog(
    title: String,
    message: String,
    onPositiveClick: () -> Unit,
    onNegativeClick: (() -> Unit)? = { },
    hasNeutralButton: Boolean = false,
    onNeutralClick: (() -> Unit)? = { } ) {

    // Initialize a new instance of
    val builder = AlertDialog.Builder(this)

    // Set the alert dialog title
    builder.setTitle(title)

    // Display a message on alert dialog
    builder.setMessage(message)

    // Set a positive button and its click listener on alert dialog
    builder.setPositiveButton(R.string.dialog_action_yes) { _, _ ->
        onPositiveClick()
    }

    // Display a negative button on alert dialog
    builder.setNegativeButton(R.string.dialog_action_no) { _, _ ->
        if( onNegativeClick != {} )
            onNegativeClick!!()
    }

    if(hasNeutralButton) {
        // Display a neutral button on alert dialog
        builder.setNeutralButton(R.string.dialog_action_cancel) { _, _ ->
            if(onNeutralClick != {})
                onNeutralClick!!()
        }
    }

    // Finally, make the alert dialog using builder
    val dialog: AlertDialog = builder.create()

    // Display the alert dialog on app interface
    dialog.show()

}

fun AppCompatActivity.showDialog() {
    dialog?.let {
        dialog = null
    }
    dialog = SpotsDialog.Builder()
        .setContext(this)
        .setMessage(R.string.dialog_progress_title)
        .setCancelable(false)
        .build()
    dialog?.show()
}

fun AppCompatActivity.hideDialog() {
    dialog?.let {
        it.dismiss()
        dialog = null
    }
}