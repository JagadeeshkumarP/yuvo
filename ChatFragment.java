package com.example.yuvo;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public int sp=0;
    public ChatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=LayoutInflater.from(getContext()).inflate(R.layout.fragment_chat, container, false);
        SharedPreferences pref1 = view.getContext().getSharedPreferences("lang", MODE_PRIVATE);
        String s=pref1.getString("lang", "en");
        if(s.equals("tm"))
            sp=1;
        TextView textView1=view.findViewById(R.id.disclimer);
        TextView textView2=view.findViewById(R.id.textView2);
        Button chat = view.findViewById(R.id.chat);
        if(sp==1){
            textView1.setText("அறிவிப்பு");
            textView2.setText("இந்த அரட்டை பொதுத் திறப்புகளுடன் சந்தேகங்களைத் தெளிவுபடுத்தும் நோக்கத்திற்காக மட்டுமே உருவாக்கப்பட்டது. இந்த பொது அரட்டை அமைப்பு அதாவது எந்த ஒருவரும் எந்த செய்திகளையும் படிக்கவும் எழுதவும் முடியும். தேவையற்ற அல்லது பயங்கரமான அல்லது மீறப்பட்ட செய்திகள் இந்த அரட்டையில் வெளியிடப்பட்டால், பயனர் ஐடி நிரந்தரமாக தடுக்கப்படும். வன்முறை அறிக்கை விருப்பம் அனைத்து பயனர் செய்திகளுக்கும் கிடைக்கிறது. மீறப்பட்ட அறிக்கையிடல் எண்ணிக்கை 10 க்கும் அதிகமாக இருந்தால், அவர்களின் செய்திகள் மற்றும் கணக்கு இடைநிறுத்தப்படும். அரட்டை அமைப்பில் உள்ள பிரச்சனைகளுக்கு குழு YUVO பொறுப்பேற்காது.\n" +
                    "இந்த விதிமுறைகள் மற்றும் நிபந்தனைகள் எந்த நேரத்திலும் அறிவிப்பு இல்லாமல் மாறக்கூடியவை எனவே தயவுசெய்து விதிமுறைகளை அடிக்கடி சரிபார்த்து விதிகள் மற்றும் ஒழுங்குமுறைகளை கண்டிப்பாக கடைபிடிக்கவும்");
            textView2.setTextSize(14);
            chat.setText("அரட்டைக்கு செல்");


        }
       chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),ChatActivity.class));
            }
        });

        return view;
    }
}