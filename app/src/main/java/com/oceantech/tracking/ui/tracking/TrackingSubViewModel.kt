    package com.oceantech.tracking.ui.tracking


    import com.airbnb.mvrx.ActivityViewModelContext
    import com.airbnb.mvrx.Async
    import com.airbnb.mvrx.FragmentViewModelContext
    import com.airbnb.mvrx.Loading
    import com.airbnb.mvrx.MvRxViewModelFactory
    import com.airbnb.mvrx.Success
    import com.airbnb.mvrx.ViewModelContext
    import com.oceantech.tracking.core.TrackingViewModel
    import com.oceantech.tracking.data.model.Tracking
    import com.oceantech.tracking.data.repository.TrackingRepository
    import dagger.assisted.Assisted
    import dagger.assisted.AssistedFactory
    import dagger.assisted.AssistedInject
    import io.reactivex.Observer
    import io.reactivex.android.schedulers.AndroidSchedulers
    import io.reactivex.disposables.Disposable
    import javax.inject.Inject

    class TrackingSubViewModel @AssistedInject constructor(
        @Assisted val state: TrackingViewState,
        val repository: TrackingRepository
    ) : TrackingViewModel<TrackingViewState, TrackingViewAction, TrackingViewEvent>(state) {
        var language: Int = 1

        override fun handle(action: TrackingViewAction) {
            when (action) {
                is TrackingViewAction.GetAllTrackingByUser -> handleGetAllTracking()
                is TrackingViewAction.ResetLang -> handleResetLang()
                is TrackingViewAction.NavigateToAddDialog -> handleNavigateToAddDialog()
                is TrackingViewAction.PostNewTracking -> handlePostNewTracking(action.tracking)
                is TrackingViewAction.UpdateTracking -> handleUpdateTracking(action.id,action.tracking)
                is TrackingViewAction.DeleteTracking -> handleDeleteTracking(action.id)
            }
        }

        private fun handleDeleteTracking(id: Int) {
        setState {
            this.copy(asyncDeleteTracking=Loading())
        }
            repository.deleteTracking(id).execute {
                copy(asyncDeleteTracking=it)
            }
        }

        private fun handleUpdateTracking(id: Int, tracking: Tracking) {
        setState {
            this.copy(asyncUpdateTracking = Loading())
        }
            repository.updateTracking(id,tracking).execute {
                copy(asyncUpdateTracking=it)
            }
        }

        private fun handlePostNewTracking(tracking: Tracking) {
            setState {
                this.copy(asyncSaveTracking = Loading())
            }
           repository.postNewTracking(tracking).execute {
               copy(asyncSaveTracking=it)
           }


        }



        private fun handleResetLang() {
            _viewEvents.post(TrackingViewEvent.ResetLanguege)
        }

        private fun handleGetAllTracking() {
            setState {
                copy(asyncTrackingArray = Loading())
            }
            repository.getAllTrackingByUser().execute {
                copy(asyncTrackingArray = it)
            }
        }

        private fun handleNavigateToAddDialog() {
            _viewEvents.post(TrackingViewEvent.NavigateToAddDialog)
        }

        @AssistedFactory
        interface Factory {
            fun create(initialState: TrackingViewState): TrackingSubViewModel
        }

        companion object : MvRxViewModelFactory<TrackingSubViewModel, TrackingViewState> {
            @JvmStatic
            override fun create(
                viewModelContext: ViewModelContext,
                state: TrackingViewState
            ): TrackingSubViewModel {
                val factory = when (viewModelContext) {
                    is FragmentViewModelContext -> viewModelContext.fragment as? Factory
                    is ActivityViewModelContext -> viewModelContext.activity as? Factory
                }
                return factory?.create(state)
                    ?: error("You should let your activity/fragment implements Factory interface")
            }
        }
    }