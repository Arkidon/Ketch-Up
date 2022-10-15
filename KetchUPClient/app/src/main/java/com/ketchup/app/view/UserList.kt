package com.ketchup.app

import android.os.Bundle
import com.ketchup.app.R.drawable.*
import com.ketchup.app.models.UserData

class UserList {
    companion object{
        val userList = listOf<UserData>(
            UserData("Arkidon", "Mustard hype","https://pbs.twimg.com/media/E_XBP6iWYAIJpZk?format=png&name=small"),
            UserData("Daviis", "Me quiero morir con el volley","https://media.karousell.com/media/photos/products/2022/1/19/haikyuu_nekoma_cosplay_jacket_1642613569_821cae80_progressive.jpg"),
            UserData("Alice", "Apetece un sushi la verdad","https://pbs.twimg.com/media/E_XBP6YXsAoOJHN?format=png&name=small")
        )
    }
}