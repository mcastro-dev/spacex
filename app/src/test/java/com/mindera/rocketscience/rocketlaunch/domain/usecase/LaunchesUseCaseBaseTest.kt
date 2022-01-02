package com.mindera.rocketscience.rocketlaunch.domain.usecase

import com.mindera.rocketscience.common.domain.model.Id
import com.mindera.rocketscience.rocketlaunch.domain.model.*
import com.mindera.rocketscience.rocketlaunch.domain.repository.IRocketLaunchRepository
import org.junit.Before
import org.mockito.kotlin.mock
import java.time.LocalDateTime
import java.time.Month

abstract class LaunchesUseCaseBaseTest {

    protected lateinit var repository: IRocketLaunchRepository

    protected val page = Page.first()
    protected val filter = Filter()
    protected val launches = listOf(
        RocketLaunch(
            id = Id("1"),
            missionName = "FalconSat",
            launchDateTime = LocalDateTime.of(2016, Month.MARCH, 24, 22, 30),
            rocket = Rocket(id = Id("falcon1"), name = "Falcon 1", type = "Merlin A"),
            status = RocketLaunch.Status.FAILURE,
            patchImageLink = Link("https://images2.imgbox.com/3c/0e/T8iJcSN3_o.png"),
            articleLink = Link("https://www.space.com/2196-spacex-inaugural-falcon-1-rocket-lost-launch.html"),
            wikipediaLink = Link("https://en.wikipedia.org/wiki/DemoSat"),
            videoLink = Link("https://www.youtube.com/watch?v=0a_00nJ_Y88")
        ),
        RocketLaunch(
            id = Id("2"),
            missionName = "Future",
            launchDateTime = LocalDateTime.of(3000, Month.JANUARY, 1, 12, 0),
            rocket = Rocket(id = Id("future0"), name = "Future Zero", type = "Future Zero A"),
            status = RocketLaunch.Status.TO_BE_DONE
        )
    )

    @Before
    open fun setUp() {
        repository = mock()
    }
}