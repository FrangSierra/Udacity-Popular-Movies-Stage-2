package frangsierra.popularmoviesudacity.core.ui;

import android.os.Bundle;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.core.presentation.BasePresenter;


public abstract class DaggerCleanActivity<P extends BasePresenter<V>, V, C> extends DaggerActivity<C> {

    @Inject
    P presenter; //Dagger injections can't be private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //noinspection unchecked
        presenter.bindView((V) this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.presenter.onCreateView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getPresenter().onDestroyView();
        getPresenter().unBindView();
    }

    protected P getPresenter() {
        if (presenter.getView() != this) throw new IllegalArgumentException("Not bound!");
        return presenter;
    }

}
