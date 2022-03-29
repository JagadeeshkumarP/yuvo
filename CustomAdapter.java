package com.example.yuvo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static android.content.Context.MODE_PRIVATE;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public int sp=0;
    private ArrayList<FriendlyMessage> listItem;
    private Context context;
    public String  temp="1";
    private static final int VIEW_HOLDER_ME = 0;
    private static final int VIEW_HOLDER_YOU = 1;

    private String mfromUserId;


    public CustomAdapter(ArrayList<FriendlyMessage> list, String fromUserId) {
        this.listItem = list;
        this.mfromUserId = fromUserId;
    }

    @Override
    public int getItemViewType(int position) {
        if (listItem.get(position).getFromUserId().equalsIgnoreCase(mfromUserId)) {
            return VIEW_HOLDER_ME;
        } else {
            return VIEW_HOLDER_YOU;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        switch (viewType) {
            case VIEW_HOLDER_ME:
                return new ViewHolderMe(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_message, viewGroup, false));
            case VIEW_HOLDER_YOU:
                return new ViewHolderYou(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.their_message, viewGroup, false));
            default:
                return null;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder v, int pos) {
        if (v instanceof ViewHolderMe) {
            ViewHolderMe viewHolderImage = (ViewHolderMe) v;
            viewHolderImage.messageBody.setText(String.format("%s", listItem.get(pos).getText().trim()));
            viewHolderImage.messageBody.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (sp==0){
                    AlertDialog alertDialog = new AlertDialog.Builder(viewHolderImage.messageBody.getContext())

                            .setTitle("Are you sure to Delete message")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    Query applesQuery = ref.child("message").orderByChild("text").equalTo(listItem.get(pos).getText().trim());
                                    final DataSnapshot[] temp = new DataSnapshot[1];
                                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                temp[0] =appleSnapshot;

                                            }
                                            temp[0].getRef().removeValue();
                                            Intent intent = new Intent(viewHolderImage.messageBody.getContext(),ChatActivity.class);
                                            viewHolderImage.messageBody.getContext().startActivity(intent);

                                            Toast.makeText(viewHolderImage.messageBody.getContext(),"message deletion successfull",Toast.LENGTH_LONG).show();
                                            }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                         }
                            })
                            .show();
                    alertDialog.setCanceledOnTouchOutside(true);
                             return false;}
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(viewHolderImage.messageBody.getContext())


                                .setMessage("நீங்கள் நிச்சயமாக செய்தியை நீக்க வேண்டுமா?")
                                .setPositiveButton("ஆம்", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                        Query applesQuery = ref.child("message").orderByChild("text").equalTo(listItem.get(pos).getText().trim());
                                        final DataSnapshot[] temp = new DataSnapshot[1];
                                        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                    temp[0] =appleSnapshot;

                                                }
                                                temp[0].getRef().removeValue();
                                                Intent intent = new Intent(viewHolderImage.messageBody.getContext(),ChatActivity.class);
                                                viewHolderImage.messageBody.getContext().startActivity(intent);
                                                Toast.makeText(viewHolderImage.messageBody.getContext(),"செய்தி நீக்கப்பட்டது",Toast.LENGTH_LONG).show();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }
                                })

                                .setNegativeButton("இல்லை", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .show();
                        alertDialog.setCanceledOnTouchOutside(true);
                        return false;
                    }
                }
            });
            viewHolderImage.itemView.setTag(viewHolderImage);
        } else if (v instanceof ViewHolderYou) {
            ViewHolderYou viewHolderYou = (ViewHolderYou) v;
            if(listItem.get(pos).getFromUserId().equals("qQ3GKWkMjRcMhjMDIrIG02cSU7A2")||listItem.get(pos).getFromUserId().equals("nPqogSSAWVQUqHE2foeQ102xwhB3")||listItem.get(pos).getFromUserId().equals("nAeBPzm58kaXFsHol3f1OW4BSZc2")||listItem.get(pos).getFromUserId().equals("7mnNt35aQpNLTy19gC0TcRYl3db2")||listItem.get(pos).getFromUserId().equals("Q8A7bC6SoVaCqPaDodYB91Nq84J3")||listItem.get(pos).getFromUserId().equals("hEmaiid3HhO3ZaHWWEB7PjgK1DH3"))
            viewHolderYou.ver.setVisibility(View.VISIBLE);



            /*StorageReference storageReference=FirebaseStorage.getInstance().getReference();
            StorageReference profileRef = storageReference.child("users/" +listItem.get(pos).getFromUserId()+ "/profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Uri url =uri;
                    SharedPreferences sharedPreferences= viewHolderYou.messageBody.getContext().getSharedPreferences("name",Context.MODE_WORLD_WRITEABLE);
                    SharedPreferences.Editor edt = sharedPreferences.edit();
                    edt.putString("temp",url.toString());
                    edt.commit();
                }
            });
            SharedPreferences sharedPreferences= viewHolderYou.messageBody.getContext().getSharedPreferences("name", MODE_PRIVATE);
            String temp=sharedPreferences.getString("temp","hello");
            Picasso.get()
                    .load(temp)
                    .transform(new RoundedTransformation(100, 0))
                    .fit()
                    .into(viewHolderYou.avatar);*/







viewHolderYou.dot.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(sp==0){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewHolderYou.messageBody.getContext());
        alertDialog.setTitle("Report Message");
        String[] items = {"Violated", "Abusive", "Sexual content", "Spam"};
        int checkedItem = 1;
        alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        temp = "1";
                        break;
                    case 1:
                        temp = "2";
                        break;
                    case 2:
                        temp = "3";
                        break;
                    case 3:
                        temp = "4";
                        break;

                }
            }
        });
        AlertDialog alert = alertDialog.create();

        alert.setCanceledOnTouchOutside(true);
        alertDialog
                .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = fstore.collection("REPORTS").document(listItem.get(pos).getFromUserId());
                        Map<String, Object> user = new HashMap<>();
                        user.put(mfromUserId, viewHolderYou.messageBody.getText() + temp);
                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                            }
                        });

                        Toast.makeText(viewHolderYou.messageBody.getContext(), "Report Sent Successful", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked

                    }
                })

                .show();
    }
        else{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(viewHolderYou.messageBody.getContext());
            alertDialog.setTitle("செய்தி அறிக்கை");
            String[] items = {"மீறப்பட்டது", "துஷ்பிரயோகம்","பாலியல் உள்ளடக்கம்", "ஸ்பேம்"};
            int checkedItem = 1;
            alertDialog.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            temp = "0";
                            break;
                        case 1:
                            temp = "1";
                            break;
                        case 2:
                            temp = "2";
                            break;
                        case 3:
                            temp = "3";
                            break;

                    }
                }
            });
            AlertDialog alert = alertDialog.create();

            alert.setCanceledOnTouchOutside(true);
            alertDialog
                    .setPositiveButton("அறிக்கை", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseFirestore fstore = FirebaseFirestore.getInstance();
                            DocumentReference documentReference = fstore.collection("REPORTS").document(listItem.get(pos).getFromUserId());
                            Map<String, Object> user = new HashMap<>();
                            user.put(mfromUserId, viewHolderYou.messageBody.getText() + temp);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });

                            Toast.makeText(viewHolderYou.messageBody.getContext(), "அறிக்கை வெற்றிகரமாக அனுப்பப்பட்டது", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("வெளியேறு", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //set what should happen when negative button is clicked

                        }
                    })

                    .show();
        }

    }
});


            viewHolderYou.name.setText(String.format("%s", listItem.get(pos).getName()));
            viewHolderYou.time.setText(String.format("%s", listItem.get(pos).getTimeStamp()));
            //viewHolderYou.time.setText(listItem.get(pos).getTimeStamp());
            viewHolderYou.messageBody.setText(String.format("%s", listItem.get(pos).getText()));
            viewHolderYou.itemView.setTag(viewHolderYou);


        }
    }

    public class ViewHolderMe extends RecyclerView.ViewHolder {

        public TextView messageBody;

        public ViewHolderMe(final View itemView) {
            super(itemView);
            messageBody = (TextView) itemView.findViewById(R.id.message_body);
        }
    }

    public class ViewHolderYou extends RecyclerView.ViewHolder {

        public ImageView avatar;
        public ImageView ver;
        public ImageView dot;
        public TextView name,time;
        public TextView messageBody;
        public ViewHolderYou(final View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            avatar.setImageResource(R.drawable.ic_baseline_account_circle_24);
            dot = (ImageView) itemView.findViewById(R.id.temp);
            ver = (ImageView) itemView.findViewById(R.id.ver);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
            messageBody = (TextView) itemView.findViewById(R.id.message_body);
            SharedPreferences pref1 = itemView.getContext().getSharedPreferences("lang", MODE_PRIVATE);
            String s=pref1.getString("lang", "en");
            if(s.equals("tm"))
                sp=1;

        }
    }





    @Override
    public int getItemCount() {
        return (null != listItem ? listItem.size() : 0);
    }
}
