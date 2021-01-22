package com.encora.musicplayer.data.local.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.encora.musicplayer.domain.SongEntry

/***
 * DTO class which contain with database table annotations
 * */
@Entity(tableName = SongEntity.TABLE_NAME)
class SongEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "imageUrl") var imageUrl: String?,
    @ColumnInfo(name = "audioLink") var audioLink: String?,
    @ColumnInfo(name = "content") var content: String?,
    @ColumnInfo(name = "artistName") var artistName: String?,
    @ColumnInfo(name = "price") var price: String?,
    @ColumnInfo(name = "categoryName") var categoryName: String?
) : Parcelable {
    constructor(title: String?, imageUrl: String?, audioLink: String?, content: String?,artistName: String?,price: String?,categoryName: String?) :
            this(0, title, imageUrl, audioLink, content,artistName,price,categoryName)

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeString(imageUrl)
        writeString(audioLink)
        writeString(content)
        writeString(artistName)
        writeString(price)
        writeString(categoryName)
    }

    companion object {

        const val TABLE_NAME = "songs"

        @JvmField
        val CREATOR: Parcelable.Creator<SongEntity> = object : Parcelable.Creator<SongEntity> {
            override fun createFromParcel(source: Parcel): SongEntity = SongEntity(source)
            override fun newArray(size: Int): Array<SongEntity?> = arrayOfNulls(size)
        }
    }
}

//extension function to covert model to DTO and change image path
fun SongEntry.mapDto() = SongEntity(title=title,
    imageUrl = "$imageUrl",
    audioLink = "${link.audioLink}",
    content = content,
    artistName="$artist",
    price="$price",
categoryName = category.label)


