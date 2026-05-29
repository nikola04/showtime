package rs.edu.raf.showtime.profile.data.mappers

import rs.edu.raf.showtime.network.model.profile.UserDto
import rs.edu.raf.showtime.profile.domain.User

fun UserDto.asDomain(): User {
    return User(
        id = id,
        username = username,
        fullName = fullName
    )
}