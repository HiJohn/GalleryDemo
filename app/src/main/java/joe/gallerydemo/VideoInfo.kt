package joe.gallerydemo

import android.os.Parcel
import android.os.Parcelable

data class VideoInfo(var path:String? = "",var rotation:Int=0,var width:Int=0,var height:Int=0,var duration:Long=0,var bitrate:Long=0) : Parcelable {


    constructor(path: String?):this(

    )

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readLong(),
            parcel.readLong())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeInt(rotation)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeLong(duration)
        parcel.writeLong(bitrate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VideoInfo> {
        override fun createFromParcel(parcel: Parcel): VideoInfo {
            return VideoInfo(parcel)
        }

        override fun newArray(size: Int): Array<VideoInfo?> {
            return arrayOfNulls(size)
        }
    }


}
