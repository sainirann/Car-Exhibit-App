package com.example.cardetailsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter for recycler view to show cars in the recycler view
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<CarBasedOnMakeAndModel> carMakeAndModel;
    private CarDetailClickListenerFactory factory;

    RecyclerViewAdapter(Context mContext, List<CarBasedOnMakeAndModel> m, CarDetailClickListenerFactory factory) {
        context = mContext;
        carMakeAndModel = m;
        this.factory = factory;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.make_model_vehicles, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(carMakeAndModel.get(position).getImageUrl())
                .placeholder(R.drawable.not_found).into(holder.image);
        holder.imageName.setText(carMakeAndModel.get(position).toString());
        holder.modelName.setText(carMakeAndModel.get(position).getModel() + " - ");
        //Use factory to create a listener for each item using the position.
        holder.parent_layout.setOnClickListener(factory.create(carMakeAndModel.get(position)));
    }

    @Override
    public int getItemCount() {
        return carMakeAndModel.size();
    }

    /**
     * View holder for holding the recycler view items
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView imageName;
        private TextView modelName;
        private RelativeLayout parent_layout;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            modelName = itemView.findViewById(R.id.model_name);
            parent_layout = itemView.findViewById(R.id.parent_view);
        }
    }
}
