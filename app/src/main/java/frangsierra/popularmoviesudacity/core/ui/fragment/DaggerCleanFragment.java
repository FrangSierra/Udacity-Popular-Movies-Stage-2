package frangsierra.popularmoviesudacity.core.ui.fragment;

import android.os.Bundle;
import android.view.View;

import javax.inject.Inject;

import frangsierra.popularmoviesudacity.core.presentation.BasePresenter;

/**
 * Custom abstract class which extends from {@link DaggerFragment} and works together with Dagger
 * to join the lifecycle calls to the presenter and instanciate it.
 *
 * @param <C> associated component to the activity.
 * @param <P> associated presenter to the activity.
 * @param <V> associated view to the activity.
 */
public abstract class DaggerCleanFragment<P extends BasePresenter<V>, V, C> extends DaggerFragment<C> {

    @Inject
    P _presenter; //Can't be private, so _ to avoid confusion, don't use this

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        doInject(getComponent());
        //noinspection unchecked
        _presenter.bindView((V) this);
        _presenter.onCreateView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().onDestroyView();
        getPresenter().unBindView();
    }

    protected P getPresenter() {
        if (_presenter.getView() != this) throw new IllegalArgumentException("Not bound!");
        return _presenter;
    }
}
