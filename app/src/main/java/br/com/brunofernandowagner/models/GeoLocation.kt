package br.com.brunofernandowagner.models

import android.os.Parcel
import android.os.Parcelable

class GeoLocation (
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

    companion object CREATOR : Parcelable.Creator<GeoLocation> {
        override fun createFromParcel(parcel: Parcel): GeoLocation {
            return GeoLocation(parcel)
        }

        override fun newArray(size: Int): Array<GeoLocation?> {
            return arrayOfNulls(size)
        }
    }
}