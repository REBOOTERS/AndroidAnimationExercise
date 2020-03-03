

package com.engineer.imitate.ui.widget.transformationlayout

import android.app.Activity
import android.view.View
import android.view.Window
import androidx.core.view.ViewCompat
import com.engineer.imitate.ui.widget.transformationlayout.TransformationLayout
import com.engineer.imitate.ui.widget.transformationlayout.getMaterialContainerTransform
import com.google.android.material.transition.MaterialContainerTransformSharedElementCallback

/** sets an exit shared element callback to the activity for implementing shared element transition. */
fun Activity.onTransformationStartContainer() {
  window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
  setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
  window.sharedElementsUseOverlay = false
}

/** sets an enter shared element callback to the activity for implementing shared element transition. */
fun Activity.onTransformationEndContainer(
  params: TransformationLayout.Params?
) {
  requireNotNull(
    params) { "TransformationLayout.Params must not be a null. check your intent key value is correct." }
  window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
  ViewCompat.setTransitionName(findViewById<View>(android.R.id.content), params.transitionName)
  setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
  window.sharedElementEnterTransition = params.getMaterialContainerTransform(this)
  window.sharedElementReturnTransition = params.getMaterialContainerTransform(this)
}
