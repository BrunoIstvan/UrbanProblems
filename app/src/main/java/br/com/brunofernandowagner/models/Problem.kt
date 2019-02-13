package br.com.brunofernandowagner.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable

@Entity
data class Problem (

    @PrimaryKey (autoGenerate = true)
    var id: Int? = null,
    var title: String? = null,
    var detail: String? = null,
    var photo: String? = null,
    var datetime: String? = null,
    var userId: String? = null,
    var address: String? = null,
    var addressNumber: String? = null,
    var neighborhood: String? = null,
    var city: String? = null,
    var state: String? = null,
    var postalCode: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(title)
        parcel.writeString(detail)
        parcel.writeString(photo)
        parcel.writeString(datetime)
        parcel.writeString(userId)
        parcel.writeString(address)
        parcel.writeString(addressNumber)
        parcel.writeString(neighborhood)
        parcel.writeString(city)
        parcel.writeString(state)
        parcel.writeString(postalCode)
        parcel.writeValue(latitude)
        parcel.writeValue(longitude)
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