package com.yueban.sunflower.data

import androidx.room.Embedded
import androidx.room.Relation

/**
 * @author yueban
 * @date 2018/12/17
 * @email fbzhh007@gmail.com
 */
class PlantAndGardenPlantings {
    @Embedded
    lateinit var plant: Plant

    @Relation(parentColumn = "id", entityColumn = "plantId")
    var gardenPlantings: List<GardenPlanting> = arrayListOf()
}