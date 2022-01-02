package com.mindera.rocketscience.company.data.local.sharedprefs.source

import android.content.Context
import com.mindera.rocketscience.common.data.error.LocalDataNotFound
import com.mindera.rocketscience.company.data.local.ILocalCompanyDataSource
import com.mindera.rocketscience.company.data.local.sharedprefs.model.*
import com.mindera.rocketscience.company.domain.model.Company
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanySharedPrefsDataSource @Inject constructor(
    @ApplicationContext context: Context
) : ILocalCompanyDataSource {

    companion object {
        private const val SHARED_PREFS_NAME = "COMPANY_PREFS"
        private const val INVALID_INT = -1
        private const val INVALID_LONG = -1L
    }

    private val sharedPrefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override suspend fun saveInfo(company: Company) {
        with(sharedPrefs.edit()) {
            val prefsCompany = PrefsCompany.fromDomain(company)

            putString(KEY_COMPANY_NAME, prefsCompany.name)
            putString(KEY_FOUNDER, prefsCompany.founder)
            putInt(KEY_FOUNDED_YEAR, prefsCompany.foundedYear)
            putInt(KEY_EMPLOYEES_COUNT, prefsCompany.employeesCount)
            putInt(KEY_LAUNCH_SITES_COUNT, prefsCompany.launchSitesCount)
            putLong(KEY_VALUATION_IN_DOLLARS, prefsCompany.valuationInDollars)

            commit()
        }
    }

    override suspend fun getInfo(): Company {
        val companyName = sharedPrefs.getString(KEY_COMPANY_NAME, null)
        val founder = sharedPrefs.getString(KEY_FOUNDER, null)
        val foundedYear = sharedPrefs.getInt(KEY_FOUNDED_YEAR, INVALID_INT)
        val employeesCount = sharedPrefs.getInt(KEY_EMPLOYEES_COUNT, INVALID_INT)
        val launchSitesCount = sharedPrefs.getInt(KEY_LAUNCH_SITES_COUNT, INVALID_INT)
        val valuationInDollars = sharedPrefs.getLong(KEY_VALUATION_IN_DOLLARS, INVALID_LONG)

        if (companyName.isNullOrEmpty() ||
                founder.isNullOrEmpty() ||
                foundedYear == INVALID_INT ||
                employeesCount == INVALID_INT ||
                launchSitesCount == INVALID_INT ||
                valuationInDollars == INVALID_LONG) {
            throw LocalDataNotFound()
        }

        return PrefsCompany(
            name = companyName,
            founder = founder,
            foundedYear = foundedYear,
            employeesCount = employeesCount,
            launchSitesCount = launchSitesCount,
            valuationInDollars = valuationInDollars
        ).toDomain()
    }

    override suspend fun deleteInfo() {
        with(sharedPrefs.edit()) {

            remove(KEY_COMPANY_NAME)
            remove(KEY_FOUNDER)
            remove(KEY_FOUNDED_YEAR)
            remove(KEY_EMPLOYEES_COUNT)
            remove(KEY_LAUNCH_SITES_COUNT)
            remove(KEY_VALUATION_IN_DOLLARS)

            commit()
        }
    }
}