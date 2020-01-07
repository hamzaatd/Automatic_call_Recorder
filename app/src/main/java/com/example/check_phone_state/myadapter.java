package com.example.check_phone_state;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

  public class myadapter  extends RecyclerView.Adapter      <myadapter.myholder> {
    Context context;
    ArrayList<File> files ;
    ArrayList<String> numbers;
    dbhelper helper ;
    String []songnames;
      String number;
      String[] info;
       String id;

     myadapter(Context context ,ArrayList<File> files,ArrayList<String> numbers){
         this.context=context;
         this.files =files;
         this.numbers =numbers;
     }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_file,viewGroup,false);
        return  new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final myholder myholder, final int i) {

        final String name = files.get(i).getName().toString();

             if (numbers.size() > 0) {
            number = numbers.get(i);

            info = number.split("@");


               id = info[0];
            String user_no = info[1];
            String file_date = info[2];
           // String file_time = info[3];


            myholder.date.setText(file_date);
            myholder.date1.setText(user_no);
        }
         String info2[] =name.split("e");
         String file_name =info2[0];
         if(file_name.equals("Outgoingcall"))
         {
         myholder.img_callmade.setImageResource(R.drawable.ic_call_made_black_24dp);

         }
         else {

             myholder.img_callmade.setImageResource(R.drawable.ic_call_received_black_24dp);

         }





        myholder.textView.setText(file_name);


            myholder.design2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Media.class).putExtra("position", i)
                        .putExtra("songs", files));
            }
        });

        myholder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    myholder.del_img.setVisibility(View.VISIBLE);
                } else {
                    myholder.del_img.setVisibility(View.INVISIBLE);
                }

            }
        });


        myholder.del_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Toast.makeText(context, "occur", Toast.LENGTH_SHORT).show();


                Toast.makeText(context, "" +id, Toast.LENGTH_SHORT).show();

                File file = files.get(i).getAbsoluteFile();

                boolean b = file.delete();
                if (b) {
                    Toast.makeText(context, "file deleted", Toast.LENGTH_SHORT).show();
                }

                helper =new dbhelper(context);
                int check = helper.delete_row(id);
                 if(check>0)
                 {
                     Toast.makeText(context, "deleted", Toast.LENGTH_SHORT).show();
                 }


                files.remove(i);
                notifyItemRangeChanged(i, files.size());
                notifyDataSetChanged();


            }
        });
    }
    @Override
    public int getItemCount() {
        return files.size();
    }

    class  myholder extends RecyclerView.ViewHolder{

          TextView textView;
          CardView cardView;
          TextView date1;
          CheckBox checkBox;
          ImageView del_img,img_callmade;
          TextView  date;
          RelativeLayout design2;

        public myholder(@NonNull View itemView) {
            super(itemView);
            cardView =itemView.findViewById(R.id.card);
            design2 =itemView.findViewById(R.id.design);
            textView=itemView.findViewById(R.id.textname);
            date1 =itemView.findViewById(R.id.no);
            date =itemView.findViewById(R.id.date);
           checkBox =itemView.findViewById(R.id.check);
           del_img =itemView.findViewById(R.id.img_del);
          img_callmade =itemView.findViewById(R.id.outgoing);


        }
    }







}
