package com.example.cardetailsapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Abstract Click Listener Factory for recycler view items
 */
abstract class CarDetailClickListenerFactory {
  final FragmentManager fragmentManager;
  final Context context;

  CarDetailClickListenerFactory(FragmentManager fragmentManager, Context context) {
    this.fragmentManager = fragmentManager;
    this.context = context;
  }

  /**
   * Create the click listener for a particular make, model and car selection
   * @param carBasedOnMakeAndModel Particular car of a make and model
   * @return {@link View.OnClickListener}
   */
  abstract View.OnClickListener create(CarBasedOnMakeAndModel carBasedOnMakeAndModel);

  /**
   * Fragment showing click listener for larger screen sizes [table landscape view]
   */
  private static class FragmentShowingClickListener implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private CarBasedOnMakeAndModel carBasedOnMakeAndModel;

    FragmentShowingClickListener(
        FragmentManager fragmentManager, CarBasedOnMakeAndModel carBasedOnMakeAndModel
    ) {
      this.fragmentManager = fragmentManager;
      this.carBasedOnMakeAndModel = carBasedOnMakeAndModel;
    }

    @Override
    public void onClick(View v) {
      Fragment fragment = fragmentManager.findFragmentById(R.id.car_details_fragment);
      FragmentTransaction ft = fragmentManager.beginTransaction();
      Bundle bundle = new Bundle();
      bundle.putParcelable("MakeAndModel", carBasedOnMakeAndModel);
      fragment.setArguments(bundle);
      if (fragment.isHidden()) {
        ft.show(fragment);
      }
      ft.commit();
    }
  }

  /**
   * Activity rendering click listener for smaller screen sizes
   */
  private static class ActivityCreatingClickListener implements View.OnClickListener {
    private CarBasedOnMakeAndModel carBasedOnMakeAndModel;
    private Context context;

    ActivityCreatingClickListener(Context context, CarBasedOnMakeAndModel carBasedOnMakeAndModel) {
      this.context = context;
      this.carBasedOnMakeAndModel = carBasedOnMakeAndModel;
    }

    @Override
    public void onClick(View v) {
      Intent i = new Intent(context, RetrievingContainerActivity.class);
      i.putExtra("MakeAndModel", this.carBasedOnMakeAndModel);
      context.startActivity(i);
    }
  }

  /**
   * Factory class which creates the {@link FragmentShowingClickListener}
   */
  static class FragmentListenerFactory extends CarDetailClickListenerFactory {
    FragmentListenerFactory(FragmentManager fragmentManager, Context context) {
      super(fragmentManager, context);
    }

    View.OnClickListener create(CarBasedOnMakeAndModel carBasedOnMakeAndModel) {
      return new FragmentShowingClickListener(fragmentManager, carBasedOnMakeAndModel);
    }
  }

  /**
   * Factory class which creates the {@link ActivityCreatingClickListener}
   */
  static class ActivityListenerFactory extends CarDetailClickListenerFactory {
    ActivityListenerFactory(FragmentManager fragmentManager, Context context) {
      super(fragmentManager, context);
    }

    @Override
    View.OnClickListener create(CarBasedOnMakeAndModel carBasedOnMakeAndModel) {
      return new ActivityCreatingClickListener(context, carBasedOnMakeAndModel);
    }
  }

}
