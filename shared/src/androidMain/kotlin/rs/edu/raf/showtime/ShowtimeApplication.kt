package rs.edu.raf.showtime

import android.app.Application

class ShowtimeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppContextHolder.init(this)
    }
}
