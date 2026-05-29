package rs.edu.raf.showtime.profile.data

import rs.edu.raf.showtime.network.ProfileAPI
import rs.edu.raf.showtime.profile.data.mappers.asDomain

class ProfileRepository(val profileAPI: ProfileAPI) {
    suspend fun getProfile() = profileAPI.profileMe().asDomain()
}