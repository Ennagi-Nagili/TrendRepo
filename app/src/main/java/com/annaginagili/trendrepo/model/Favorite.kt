package com.annaginagili.trendrepo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Favorite(@PrimaryKey(autoGenerate = true) val id: Int,
                    @ColumnInfo(name = "repo_id") val repo_id: Int,
                    @ColumnInfo(name = "image") val image: String,
                    @ColumnInfo(name = "repo_name") val repo_name: String,
                    @ColumnInfo(name = "owner_name") val owner_name: String,
                    @ColumnInfo(name = "star") val star: String,
                    @ColumnInfo(name = "description") val description: String,
                    @ColumnInfo(name = "language") val language: String,
                    @ColumnInfo(name = "forks") val forks: String,
                    @ColumnInfo(name = "date") val date: String,
                    @ColumnInfo(name = "link") val link: String): Serializable