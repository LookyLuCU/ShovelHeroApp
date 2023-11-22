package com.example.shovelheroapp.Controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.shovelheroapp.Models.Address;

import java.util.List;

public class AddressAdapter extends ArrayAdapter<Address> implements SpinnerAdapter {

    public AddressAdapter(Context context, List<Address> addresses) {
        super(context, 0, addresses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Address address = getItem(position);
        if (address != null) {
            textView.setText(address.toString());
        }

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Address address = getItem(position);
        if (address != null) {
            textView.setText(address.toString());
        }

        return convertView;
    }
}



//MY TRY EARLIER
/**
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
 **/
