package com.mobiwarez.laki.seapp.dagger.application.module;

import com.mobiwarez.domain.domain.intereactions.toy.AddNewToyUseCase;
import com.mobiwarez.domain.domain.intereactions.toy.GetToysUseCase;
import com.mobiwarez.domain.domain.repository.ToyRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Laki on 24/10/2017.
 */

@Module
public class RealUseCaseModule {

    @Provides
    @Singleton
    AddNewToyUseCase provideAddNewToyUseCase(ToyRepository toyRepository) {
        return new AddNewToyUseCase(toyRepository);
    }

    @Provides
    @Singleton
    GetToysUseCase provideGetToysUsecase(ToyRepository toyRepository) {
        return new GetToysUseCase(toyRepository);
    }

    interface Exposes {

        AddNewToyUseCase addNewToyUseCase();

        GetToysUseCase getToysUsecase();

    }

    }
