package com.example.yuvo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> data;
FragmentActivity context;


    Adapter(Context context, List<String> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.custom_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        // bind the textview with data received

        String title = data.get(i);
        viewHolder.textTitle.setText(title);
if(title=="WEATHER REPORT"||title=="வானிலை அறிக்கை"){
    viewHolder.img.setImageResource(R.mipmap.weather);
}
        if(title=="FERTILIZER DETAILS"||title=="உரம் விவரங்கள்"){
            viewHolder.img.setImageResource(R.mipmap.fertlizer);
        }

        if(title=="SEED AVAILABILITY"||title=="விதை விவரங்கள்"){
            viewHolder.img.setImageResource(R.mipmap.seeds);
        }
        if(title=="RESERVOIRS"||title=="அணை நீர்மட்டம்"){
            viewHolder.img.setImageResource(R.mipmap.reservoir);
        }
        if(title=="SCHEMES"||title=="திட்டங்கள்"){
            viewHolder.img.setImageResource(R.mipmap.schemes);
        }

        if(title=="LAND COVERAGE"||title=="நிலப்பரப்பு"){
            viewHolder.img.setImageResource(R.mipmap.landcoverage);
        }
        if(title=="WORKING UNITS"||title=="வேலை அலகுகள்"){
            viewHolder.img.setImageResource(R.mipmap.workingunits);
        }
        if(title=="AGRI MEET"||title=="அதிகாரி சந்திப்பு"){
            viewHolder.img.setImageResource(R.mipmap.formerofficer);
        }
        if(title=="GODOWN REPORT"||title=="குடோன் அறிக்கை"){
            viewHolder.img.setImageResource(R.mipmap.godown);
        }
        if(title=="KURUVAI SPECIAL"||title=="குருவய் சிறப்பு"){
            viewHolder.img.setImageResource(R.mipmap.kuruvai);

        }
        if(title=="ORGANIC FARM"||title=="இயற்கை விவசாயம்"){
            viewHolder.img.setImageResource(R.mipmap.organic);
        }
        if(title=="CUSTOM HIRING"||title=="இயந்திர தேடல்"){
            viewHolder.img.setImageResource(R.mipmap.custom);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textTitle,textDescription;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.get(getAdapterPosition())=="WEATHER REPORT"||data.get(getAdapterPosition())=="வானிலை அறிக்கை"){

                        Intent i = new Intent(v.getContext(),WeatherActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);

                    //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                }
                    if(data.get(getAdapterPosition())=="FERTILIZER DETAILS"||data.get(getAdapterPosition())=="உரம் விவரங்கள்"){

                        Intent i = new Intent(v.getContext(),FertilizerActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="SEED AVAILABILITY"||data.get(getAdapterPosition())=="விதை விவரங்கள்"){

                        Intent i = new Intent(v.getContext(),SeedActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="RESERVOIRS"||data.get(getAdapterPosition())=="அணை நீர்மட்டம்"){

                        Intent i = new Intent(v.getContext(),ReservoirActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }

                    if(data.get(getAdapterPosition())=="SCHEMES"||data.get(getAdapterPosition())=="திட்டங்கள்"){

                        Intent i = new Intent(v.getContext(),SchemesActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }

                    if(data.get(getAdapterPosition())=="LAND COVERAGE"||data.get(getAdapterPosition())=="நிலப்பரப்பு"){

                        Intent i = new Intent(v.getContext(),LandcoverageActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="WORKING UNITS"||data.get(getAdapterPosition())=="வேலை அலகுகள்"){

                        Intent i = new Intent(v.getContext(),WorkingunitActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="AGRI MEET"||data.get(getAdapterPosition())=="அதிகாரி சந்திப்பு"){

                        Intent i = new Intent(v.getContext(),FormerofficerActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="GODOWN REPORT"||data.get(getAdapterPosition())=="குடோன் அறிக்கை"){

                        Intent i = new Intent(v.getContext(),GodownActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="KURUVAI SPECIAL"||data.get(getAdapterPosition())=="குருவய் சிறப்பு"){

                        Intent i = new Intent(v.getContext(),KuruvaispecialActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="ORGANIC FARM"||data.get(getAdapterPosition())=="இயற்கை விவசாயம்"){

                        Intent i = new Intent(v.getContext(),OrganicActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                    if(data.get(getAdapterPosition())=="CUSTOM HIRING"||data.get(getAdapterPosition())=="இயந்திர தேடல்"){

                        Intent i = new Intent(v.getContext(),CustomActivity.class);
                        i.putExtra("title",data.get(getAdapterPosition()));
                        v.getContext().startActivity(i);
                        //Toast.makeText(v.getContext(),data.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();
                    }
                }
            });

            textTitle = itemView.findViewById(R.id.textTitle);
            textDescription = itemView.findViewById(R.id.textDesc);
            img=itemView.findViewById(R.id.imageView);
        }
    }
}

