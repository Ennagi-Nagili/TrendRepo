package com.annaginagili.trendrepo.model

import java.io.Serializable

data class ItemsModel(val id: Int, val name: String, val owner: OwnerModel, val description: String?,
                      val stargazers_count: Int, val language: String?, val forks: Int?,
                      val created_at: String?, val html_url: String?): Serializable