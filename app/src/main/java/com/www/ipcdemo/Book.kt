package com.www.ipcdemo

import android.os.Parcel
import android.os.Parcelable


class Book(private val bookId: Int, private val bookName: String) : Parcelable {


    /**
     * 将当前对象写入序列化结构中，通过一系列的 write 方法完成
     * 其中 flags 有两种值：0 或 1，
     * <p> 为 1 时标识当前对象需要作为返回值返回，不能立即释放资源
     * 几乎所有情况都返回 0
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(bookId)
        parcel.writeString(bookName)
    }

    /**
     * 返回当前对象的描述，如果含有文件扫描符，返回1，否则返回0
     * 几乎所有情况都返回 0
     */
    override fun describeContents(): Int {
        return 0
    }

    /**
     * 反序列化
     */
    companion object CREATOR : Parcelable.Creator<Book> {

        /**
         * 从序列化后对象中创建原始对象
         */
        override fun createFromParcel(source: Parcel): Book {
            return Book(source.readInt(), source.readString()!!)
        }

        override fun newArray(size: Int): Array<Book?> {
            /**
             * 创建指定长度的原始对象数组
             */
            return arrayOfNulls(size)
        }
    }
}