package com.example.weatherapp.service

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import com.example.weatherapp.utils.InjectorUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


private const val JOB_ID = 111

class WeatherSyncJobService : JobService() {

    private lateinit var disposable: Disposable

    companion object {

        fun schedule(context: Context, intervalMillis: Long) {
            println("schedule $JOB_ID")
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val componentName = ComponentName(context, WeatherSyncJobService::class.java)
            val builder = JobInfo.Builder(JOB_ID, componentName)
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            builder.setPeriodic(intervalMillis)
            jobScheduler.schedule(builder.build())
        }

        fun cancel(context: Context) {
            println("cancel $JOB_ID")
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.cancel(JOB_ID)
        }

    }

    override fun onStopJob(params: JobParameters?): Boolean {
        println("Job Stopped $JOB_ID")
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        println("Job Started $JOB_ID")
        val weatherRepository =
            InjectorUtils.provideWeatherRepositoryForWeatherSyncJobService(this.applicationContext)
        disposable = weatherRepository.syncAllDate().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe({
            this.jobFinished(params, false)
        }, {
            this.jobFinished(params, false)
        })
        return false
    }
}