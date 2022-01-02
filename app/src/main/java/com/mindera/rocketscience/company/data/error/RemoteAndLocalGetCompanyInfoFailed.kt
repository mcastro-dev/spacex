package com.mindera.rocketscience.company.data.error

import com.mindera.rocketscience.common.domain.error.Failure

class RemoteAndLocalGetCompanyInfoFailed(cause: Throwable) : Failure(cause)