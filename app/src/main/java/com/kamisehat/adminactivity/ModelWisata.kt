package com.kamisehat.adminactivity

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ModelWisata(
    var NamaWisata: String? = null,
    var Deskripsi: String? = null,
    var Gambar: String? = null,
    var Alamat: String? = null,
    var lat: String? = null,
    var long: String? = null,

    @DocumentId
    var strId: String? = null
):Parcelable
