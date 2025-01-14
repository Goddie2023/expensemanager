package com.naveenapps.expensemanager.core.database.utils

import androidx.room.TypeConverter
import java.util.Date

object DateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
