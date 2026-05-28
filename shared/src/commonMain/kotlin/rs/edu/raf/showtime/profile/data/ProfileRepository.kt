package rs.edu.raf.showtime.profile.data

import rs.edu.raf.showtime.network.ProfileAPI

class ProfileRepository(val profileAPI: ProfileAPI) {
    suspend fun getProfile() = profileAPI.profileMe()
}