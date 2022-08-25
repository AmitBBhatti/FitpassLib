package com.fitpass.libfitpass.base.http_client

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class UnlockFitfeastModel(@SerializedName("cta_text")
                               var cta_text:@RawValue String?,

                               @SerializedName("unlock_description")
                               var unlock_description:@RawValue String?,

                               @SerializedName("unlock_heading")
                               var unlock_heading:@RawValue String?,

                               @SerializedName("unlock_photo")
                               var unlock_photo:@RawValue String?): Parcelable {
}
