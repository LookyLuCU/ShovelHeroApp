package com.example.shovelheroapp.Controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shovelheroapp.Models.Address;
import com.example.shovelheroapp.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private List<Address> addressList;

    public AddressAdapter(List<Address> addressList) {
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Address address = addressList.get(position);

        holder.tvAddress.setText(address.getAddress());
        holder.tvCity.setText(address.getCity());
        holder.tvProvince.setText(address.getProvince());
        holder.tvPostalCode.setText(address.getPostalCode());
        holder.tvCountry.setText(address.getCountry());
        holder.tvAddressNotes.setText(address.getAddressNotes());
        holder.tvSquareFootage.setText(address.getDrivewaySquareFootage());
        holder.tvAccessible.setText(address.getAccessible());
        holder.tvShovelAvailable.setText(address.getShovelAvailable());

    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAddress;
        TextView tvCity;
        TextView tvProvince;
        TextView tvPostalCode;
        TextView tvCountry;
        TextView tvAddressNotes;
        TextView tvSquareFootage;
        TextView tvAccessible;
        TextView tvShovelAvailable;


        public ViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvCity = itemView.findViewById(R.id.tvCity);
            tvProvince = itemView.findViewById(R.id.tvProvince);
            tvPostalCode = itemView.findViewById(R.id.tvPostalCode);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvAddressNotes = itemView.findViewById(R.id.tvAddress);
            tvSquareFootage = itemView.findViewById(R.id.tvSquareFootage);
            tvAccessible = itemView.findViewById(R.id.tvAccessible);
            tvShovelAvailable = itemView.findViewById(R.id.tvShovelAvailable);
        }
    }
}
