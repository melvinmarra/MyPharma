package fr.isen.emelian.mypharma.DataClass

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pharmacy(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val postcode: String = "",
    val city: String = "",
    val address: String = "",
    val country: String = "",
    val employee_one: String = "",
    val employee_two: String = "",
    val employee_three: String = "",
    val currentUser: String = "",
    val profileImageUrl: String = "",
    val box_one: String = "",
    val box_two: String = "",
    val box_three: String = ""
): Parcelable
