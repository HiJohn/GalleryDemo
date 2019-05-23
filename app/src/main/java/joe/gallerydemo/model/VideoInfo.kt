package joe.gallerydemo.model

import android.os.Parcel
import android.os.Parcelable
import java.io.File

data class VideoInfo(var path:String? = "") : Parcelable {

    var displayName: String? = ""
    var width:Int = 0
    var height:Int = 0
    var duration:Long = 0
//    var file:File = File(path)

    constructor( mPath:String,displayName: String,  width:Int,
                 height:Int,duration:Long):this(mPath){
        this.displayName = displayName
        this.width = width
        this.height = height
        this.duration = duration
    }


    constructor(parcel: Parcel) : this() {
        path = parcel.readString()
        displayName = parcel.readString()
        width = parcel.readInt()
        height = parcel.readInt()
        duration = parcel.readLong()
//        file = parcel.readSerializable() as File

    }
    override fun writeToParcel(dest: Parcel?, flags: Int) {

        dest?.writeString(path)
        dest?.writeString(displayName)
        dest?.writeInt(width)
        dest?.writeInt(height)
        dest?.writeLong(duration)
//        dest?.writeSerializable(file)
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
