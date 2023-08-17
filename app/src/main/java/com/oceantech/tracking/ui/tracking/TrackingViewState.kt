package com.oceantech.tracking.ui.tracking

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.Loading
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import com.oceantech.tracking.data.model.Tracking

data class TrackingViewState(
    val asyncTrackingArray: Async<List<Tracking>> = Uninitialized,
    val asyncSaveTracking: Async<Tracking> = Uninitialized,
    val asyncUpdateTracking: Async<Tracking> =Uninitialized,
    val asyncDeleteTracking: Async<Tracking> = Uninitialized
    ):MvRxState {
        fun isLoading() = asyncTrackingArray is Loading ||
                asyncSaveTracking is Loading ||
                asyncUpdateTracking is Loading ||
                asyncDeleteTracking is Loading
}