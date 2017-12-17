package com.mobiwarez.data.toy;

import com.mobiwarez.data.toy.db.ToyDao;
import com.mobiwarez.data.toy.db.definition.ToysDatabase;
import com.mobiwarez.domain.domain.models.Toy;
import com.mobiwarez.domain.domain.repository.ToyRepository;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;

/**
 * Created by Laki on 24/10/2017.
 */

public class ToyRepoImpl implements ToyRepository {

    private ToyDao dao;
    private Scheduler scheduler;

    public ToyRepoImpl(ToysDatabase toysDatabase, Scheduler backgroundScheduler){
        this.dao = toysDatabase.toyDao();
        this.scheduler = backgroundScheduler;
    }


    @NotNull
    @Override
    public Single<List<Toy>> getUserToy() {
        return null;
    }

    @NotNull
    @Override
    public Single<List<Toy>> getToys(int feedId) {
        return null;
    }

    @NotNull
    @Override
    public Single<Boolean> toyExists(@NotNull String feedUrl) {
        return null;
    }

    @NotNull
    @Override
    public Completable createNewToy(@NotNull Toy toy) {
        return null;
    }

    @NotNull
    @Override
    public Completable deleteToy(int feedId) {
        return null;
    }

    @NotNull
    @Override
    public Single<List<Toy>> getToysFromLocal() {
        return null;
    }
}
