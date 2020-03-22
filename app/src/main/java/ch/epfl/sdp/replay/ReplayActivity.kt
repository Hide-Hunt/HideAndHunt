package ch.epfl.sdp.replay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ch.epfl.sdp.databinding.ActivityReplayBinding
import ch.epfl.sdp.game.data.*
import ch.epfl.sdp.replay.game_event.CatchEvent
import ch.epfl.sdp.replay.game_event.LocationEvent
import org.mapsforge.map.android.graphics.AndroidGraphicFactory

class ReplayActivity : AppCompatActivity() {
    private lateinit var binding : ActivityReplayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AndroidGraphicFactory.createInstance(application)

        val events = listOf(
                LocationEvent(1584826586, 0, Location(46.51888166666666, 6.565925)),
                LocationEvent(1584826592, 1, Location(46.51888166666666, 6.565925)),
                LocationEvent(1584826592, 0, Location(46.5189, 6.565788333333334)),
                LocationEvent(1584826594, 1, Location(46.5189, 6.565788333333334)),
                LocationEvent(1584826594, 0, Location(46.51891833333334, 6.565696666666666)),
                LocationEvent(1584826597, 1, Location(46.51891833333334, 6.565696666666666)),
                LocationEvent(1584826597, 0, Location(46.51899333333334, 6.565713333333334)),
                LocationEvent(1584826599, 1, Location(46.51899333333334, 6.565713333333334)),
                LocationEvent(1584826599, 0, Location(46.51911833333334, 6.565706666666667)),
                LocationEvent(1584826601, 1, Location(46.51911833333334, 6.565706666666667)),
                LocationEvent(1584826601, 0, Location(46.51919666666666, 6.565715)),
                LocationEvent(1584826603, 1, Location(46.51919666666666, 6.565715)),
                LocationEvent(1584826603, 0, Location(46.51926, 6.565709999999999)),
                LocationEvent(1584826605, 1, Location(46.51926, 6.565709999999999)),
                LocationEvent(1584826605, 0, Location(46.51933833333332, 6.565706666666667)),
                LocationEvent(1584826607, 1, Location(46.51933833333332, 6.565706666666667)),
                LocationEvent(1584826607, 0, Location(46.51941166666667, 6.565709999999999)),
                LocationEvent(1584826609, 1, Location(46.51941166666667, 6.565709999999999)),
                LocationEvent(1584826609, 0, Location(46.519481666666664, 6.565731666666666)),
                LocationEvent(1584826611, 1, Location(46.519481666666664, 6.565731666666666)),
                LocationEvent(1584826611, 0, Location(46.51951333333333, 6.565811666666667)),
                LocationEvent(1584826614, 1, Location(46.51951333333333, 6.565811666666667)),
                LocationEvent(1584826614, 0, Location(46.51958333333334, 6.565836666666667)),
                LocationEvent(1584826615, 1, Location(46.51958333333334, 6.565836666666667)),
                LocationEvent(1584826615, 0, Location(46.51964833333333, 6.565788333333334)),
                LocationEvent(1584826617, 1, Location(46.51964833333333, 6.565788333333334)),
                LocationEvent(1584826617, 0, Location(46.519751666666664, 6.565781666666667)),
                LocationEvent(1584826619, 1, Location(46.519751666666664, 6.565781666666667)),
                LocationEvent(1584826619, 0, Location(46.51983499999999, 6.565768333333334)),
                LocationEvent(1584826621, 1, Location(46.51983499999999, 6.565768333333334)),
                LocationEvent(1584826621, 0, Location(46.519915000000005, 6.565884999999999)),
                LocationEvent(1584826624, 1, Location(46.519915000000005, 6.565884999999999)),
                LocationEvent(1584826624, 0, Location(46.519976666666665, 6.565935)),
                LocationEvent(1584826628, 1, Location(46.519976666666665, 6.565935)),
                LocationEvent(1584826628, 0, Location(46.520115000000004, 6.566011666666666)),
                LocationEvent(1584826630, 1, Location(46.520115000000004, 6.566011666666666)),
                LocationEvent(1584826630, 0, Location(46.52022166666667, 6.566025)),
                LocationEvent(1584826632, 1, Location(46.52022166666667, 6.566025)),
                LocationEvent(1584826632, 0, Location(46.52033333333334, 6.5660300000000005)),
                LocationEvent(1584826634, 1, Location(46.52033333333334, 6.5660300000000005)),
                LocationEvent(1584826634, 0, Location(46.52036, 6.566148333333333)),
                LocationEvent(1584826636, 1, Location(46.52036, 6.566148333333333)),
                LocationEvent(1584826636, 0, Location(46.520385000000005, 6.5662633333333345)),
                LocationEvent(1584826639, 1, Location(46.520385000000005, 6.5662633333333345)),
                LocationEvent(1584826639, 0, Location(46.520426666666665, 6.5662883333333335)),
                LocationEvent(1584826641, 1, Location(46.520426666666665, 6.5662883333333335)),
                LocationEvent(1584826641, 0, Location(46.520451666666666, 6.566381666666667)),
                LocationEvent(1584826643, 1, Location(46.520451666666666, 6.566381666666667)),
                LocationEvent(1584826643, 0, Location(46.52053833333333, 6.566386666666666)),
                LocationEvent(1584826646, 1, Location(46.52053833333333, 6.566386666666666)),
                LocationEvent(1584826646, 0, Location(46.52058, 6.566478333333333)),
                LocationEvent(1584826648, 1, Location(46.52058, 6.566478333333333)),
                LocationEvent(1584826648, 0, Location(46.52059333333333, 6.566653333333333)),
                LocationEvent(1584826651, 1, Location(46.52059333333333, 6.566653333333333)),
                LocationEvent(1584826651, 0, Location(46.52058999999999, 6.5669)),
                LocationEvent(1584826653, 1, Location(46.52058999999999, 6.5669)),
                LocationEvent(1584826653, 0, Location(46.520599999999995, 6.567328333333334)),
                LocationEvent(1584826656, 1, Location(46.520599999999995, 6.567328333333334)),
                LocationEvent(1584826656, 0, Location(46.52058, 6.567565)),
                LocationEvent(1584826658, 1, Location(46.52058, 6.567565)),
                LocationEvent(1584826658, 0, Location(46.52057666666666, 6.567776666666667)),
                LocationEvent(1584826661, 1, Location(46.52057666666666, 6.567776666666667)),
                LocationEvent(1584826661, 0, Location(46.52050666666667, 6.567841666666667)),
                LocationEvent(1584826662, 1, Location(46.52050666666667, 6.567841666666667)),
                CatchEvent(1584826663, 1, 0)
        )

        val p0 = Prey(0).also {
            it.lastKnownLocation = (events.first {event -> event is LocationEvent && event.playerID == it.id } as LocationEvent).location
        }
        val p1 = Predator(1).also {
            it.lastKnownLocation = (events.first {event -> event is LocationEvent && event.playerID == it.id } as LocationEvent).location
        }

        val firstLocation = (events.first { it is LocationEvent } as LocationEvent).location
        val gameArea = events.fold(Area(firstLocation, firstLocation), {tmpArea, event ->
            if (event is LocationEvent) {
                val bottomLeftLat = minOf(tmpArea.bottomLeft.latitude, event.location.latitude)
                val bottomLeftLon = minOf(tmpArea.bottomLeft.longitude, event.location.longitude)
                val topRightLat = maxOf(tmpArea.topRight.latitude, event.location.latitude)
                val topRightLon = maxOf(tmpArea.topRight.longitude, event.location.longitude)
                Area(Location(bottomLeftLat, bottomLeftLon), Location(topRightLat, topRightLon))
            } else tmpArea
        })

        val history = GameHistory(
                0,
                listOf(p0, p1),
                gameArea,
                events
        )

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(binding.replayControl.id, ReplayControlFragment.newInstance(ArrayList(history.events.map { it.timestamp })))
                    .replace(binding.replayMap.id, ReplayFragment.newInstance(history))
                    .commitNow()
        }
    }
}
