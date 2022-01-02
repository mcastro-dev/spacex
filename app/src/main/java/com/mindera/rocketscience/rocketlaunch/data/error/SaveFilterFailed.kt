package com.mindera.rocketscience.rocketlaunch.data.error

import com.mindera.rocketscience.common.domain.error.Failure

class SaveFilterFailed(cause: Throwable) : Failure(cause)