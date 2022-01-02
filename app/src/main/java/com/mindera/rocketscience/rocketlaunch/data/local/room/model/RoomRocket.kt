package com.mindera.rocketscience.rocketlaunch.data.local.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.data.local.room.model.table.RocketTable
import com.mindera.rocketscience.rocketlaunch.domain.model.Rocket

@Entity(tableName = RocketTable.TABLE_NAME)
data class RoomRocket(
    @ColumnInfo(name = RocketTable.COLUMN_ID)
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = RocketTable.COLUMN_NAME)
    val name: String,
    @ColumnInfo(name = RocketTable.COLUMN_TYPE)
    val type: String,
) {
    companion object {
        fun fromDomain(rocket: Rocket) : RoomRocket {
            return RoomRocket(
                id = rocket.id.value,
                name = rocket.name,
                type = rocket.type
            )
        }
    }

    fun toDomain() : Rocket {
        return Rocket(
            id = Id(id),
            name = name,
            type = type
        )
    }
}