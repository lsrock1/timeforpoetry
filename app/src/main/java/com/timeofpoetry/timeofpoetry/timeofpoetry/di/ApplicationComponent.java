package com.timeofpoetry.timeofpoetry.timeofpoetry.di;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by sangroklee on 2017. 12. 26..
 */
@Singleton
@Component(modules = {AppModule.class})
public interface ApplicationComponent {
    ActivityComponent plus(ActivityModule activityModule);
    ServiceComponent plus(ServiceModule serviceModule);
}
