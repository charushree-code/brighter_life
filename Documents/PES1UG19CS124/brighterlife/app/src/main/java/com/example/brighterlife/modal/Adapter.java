package com.example.brighterlife.modal;



import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.brighterlife.R;
import com.example.brighterlife.page.journal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<String> titles;
    List<String> content;
    List<Integer> feelings;




    public Adapter(List<String> title, List<String> content, List<Integer> feelings){
        this.titles=title;
        this.content=content;
        this.feelings=feelings;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.journal,parent,false);
        return new ViewHolder(view);

    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.noteTitle.setText(titles.get(position));
        holder.noteContent.setText(content.get(position));
        Integer image=imagesetting(holder,position);
        holder.imageView.setImageResource(image);
        Integer code =getRandomcolor();
        holder.cardView.setCardBackgroundColor(holder.view.getResources().getColor(code,null));

        holder.view.setOnClickListener((v)-> {
                Intent i =new Intent(v.getContext(), journal.class);
                i.putExtra("title",titles.get(position));
                i.putExtra("content",content.get(position));
                i.putExtra("code",code);
                i.putExtra("feelings",image);
                v.getContext().startActivity(i);

        });
    }

    public int imagesetting(ViewHolder holder, int position){
        List<Integer> images=new ArrayList<Integer>();
        images.add(R.drawable.frustrated);
        images.add(R.drawable.happy);
        images.add(R.drawable.sad);
        images.add(R.drawable.stressed);


        int image=images.get(feelings.get(position));
        return image;
    }
    private int getRandomcolor(){
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.red);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }
    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView noteTitle,noteContent;
        View view;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle=itemView.findViewById(R.id.titles);
            noteContent=itemView.findViewById(R.id.content);
            cardView=itemView.findViewById(R.id.noteCard);
            imageView=itemView.findViewById(R.id.imageView);
            view =itemView;
        }
    }

}
