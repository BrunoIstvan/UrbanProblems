package br.com.brunofernandowagner.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
data class Problem (

    @PrimaryKey (autoGenerate = true)
    var id: Int? ,
    var title: String? ,
    var detail: String? ,
    var photo: String? ,
    var datetime: String? ,
    var userId: String? ,
    var lat: Double? ,
    var lon: Double?,
    var address: String?,
    var addressNumber: String?,
    var neighborhood: String?,
    var city: String?,
    var state: String?,
    var postalCode: String?

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeString(photo)
        parcel.writeString(datetime)
        parcel.writeString(userId)
        parcel.writeValue(lat)
        parcel.writeValue(lon)
        parcel.writeString(address)
        parcel.writeString(addressNumber)
        parcel.writeString(neighborhood)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(postalCode)
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