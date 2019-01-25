package br.com.brunofernandowagner.models

import android.os.Parcel
import android.os.Parcelable

data class Problem (
    val id: String = "",
    val title: String = "",
    val detail: String = "",
    val photo: String = "",
    val datetime: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeString(photo)
        parcel.writeString(datetime)
        parcel.writeDouble(lat)
        parcel.writeDouble(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Problem> {
        override fun createFromParcel(parcel: Parcel): Problem {
            return Problem(parcel)
        }

        override fun newArray(size: Int): Array<Problem?> {
            return arrayOfNulls(size)
        }
    }
}