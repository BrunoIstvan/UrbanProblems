package br.com.brunofernandowagner

//import android.graphics.Bitmap
//import android.net.Uri
//import android.os.Environment
//import br.com.brunofernandowagner.utils.AppCtx
////import com.developers.imagezipper.ImageZipper
//import com.google.android.gms.tasks.Tasks
//import com.google.firebase.storage.FirebaseStorage
//import com.google.firebase.storage.StorageReference
//import id.zelory.compressor.Compressor
//import java.io.File

class UploadFile {

//    companion object {
//
//        // Create a storage reference from our app
//        private val storageProfileRef: StorageReference = FirebaseStorage.getInstance().reference.child("profile/thumbs/")
//        private val storageProblemRef: StorageReference = FirebaseStorage.getInstance().reference.child("problemLiveData/thumbs/")
//
//        private fun upload(
//            uid: String,
//            path: String,
//            storage: StorageReference,
//            onComplete: (uri: Uri?) -> Unit,
//            onError: (error: String) -> Unit
//        ) {
//
//            val file = File(path)
//            //val path = Uri.fromFile(File(path))
//
//            val thumbsRef = storage.child("$uid.jpg")
//
//            //val compressedImageFile: File = ImageZipper(AppCtx.getInstance().ctx)
//            //    .setQuality(30)
//                //.setMaxWidth(200)
//                //.setMaxHeight(200)
//             //   .compressToFile(file)
//
//            val compressedImageFile = Compressor(AppCtx.getInstance().ctx!!)
//                .setMaxWidth(640)
//                .setMaxHeight(480)
//                .setQuality(30)
//                .setCompressFormat(Bitmap.CompressFormat.WEBP)
//                //.setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
//                //    Environment.DIRECTORY_PICTURES).getAbsolutePath())
//                .compressToFile(file)
//
//            val uri = Uri.fromFile(compressedImageFile)
//
//            Thread(Runnable {
//                try {
//                    val task = thumbsRef.putFile(uri).continueWithTask { uploadTask ->
//                        if (!uploadTask.isSuccessful) {
//                            throw uploadTask.exception!!
//                        }
//                        //Now that the file is uploaded, we can retrieve the downloadUrl from the original reference
//                        return@continueWithTask thumbsRef.downloadUrl
//                    }
//
//                    val downloadUrl = Tasks.await(task)
//                    onComplete(downloadUrl)
//                    //Do whatever you want with the Url
//                } catch (e: Exception) {
//                    //manage error
//                    onError(e.message!!)
//                }
//            }).start()
//
//
//        }
//
//        fun uploadProfileImage(
//            uid: String,
//            path: String,
//            onComplete: (uri: Uri?) -> Unit,
//            onError: (error: String) -> Unit
//        ) {
//
//            upload(uid, path, storageProfileRef,
//                onComplete = { it -> onComplete(it) },
//                onError = { it -> onError(it) })
//
//        }
//
//        fun uploadProblemImage(
//            problemUid: String,
//            path: String,
//            onComplete: (uri: Uri?) -> Unit,
//            onError: (error: String) -> Unit
//        ) {
//
//            upload(problemUid, path, storageProblemRef,
//                onComplete = { it -> onComplete(it) },
//                onError = { it -> onError(it) })
//
//        }
//
//    }


}