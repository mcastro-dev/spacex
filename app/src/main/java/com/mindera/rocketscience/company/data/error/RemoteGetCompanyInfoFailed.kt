package com.mindera.rocketscience.company.data.error

import com.mindera.rocketscience.common.domain.error.Failure

class RemoteGetCompanyInfoFailed(cause: Throwable) : Failure(cause)