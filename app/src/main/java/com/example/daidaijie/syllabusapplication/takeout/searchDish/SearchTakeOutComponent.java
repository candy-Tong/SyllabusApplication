package com.example.daidaijie.syllabusapplication.takeout.searchDish;

import com.example.daidaijie.syllabusapplication.PerActivity;
import com.example.daidaijie.syllabusapplication.takeout.TakeOutModelComponent;

import dagger.Component;

/**
 * Created by daidaijie on 2016/10/8.
 */

@PerActivity
@Component(dependencies = TakeOutModelComponent.class, modules = SearchTakeOutModule.class)
public interface SearchTakeOutComponent {

    void inject(SearchTakeOutActivity searchTakeOutActivity);
}
