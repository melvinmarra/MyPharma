package fr.isen.emelian.mypharma.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val date_of_birth: String = "",
    var password: String = "",
    val email: String = "",
    val secu_number: String = "",
    val phone: String = "",
    val pictureUrl: String = "",
    val ordoUrl_one: String = "",
    var latestPharmacy: String = "",
    var stateRequest: String = "",
    val urlLatest: String = ""
): Parcelable
