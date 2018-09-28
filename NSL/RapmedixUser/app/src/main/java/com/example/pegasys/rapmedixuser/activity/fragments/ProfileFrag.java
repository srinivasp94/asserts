package com.example.pegasys.rapmedixuser.activity.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.pegasys.rapmedixuser.R;
import com.example.pegasys.rapmedixuser.activity.newactivities.DoctorDescription;
import com.example.pegasys.rapmedixuser.activity.pojo.DoctorAword;
import com.example.pegasys.rapmedixuser.activity.pojo.DoctorExperience;
import com.example.pegasys.rapmedixuser.activity.pojo.DoctorPresentation;
import com.example.pegasys.rapmedixuser.activity.pojo.DoctorRegnumber;
import com.example.pegasys.rapmedixuser.activity.pojo.Doctorworkingdatail;

import java.util.ArrayList;

/**
 * Created by pegasys on 12/28/2017.
 */

public class ProfileFrag extends Fragment {
    ListView xperiance, qualify, membership, registrationNumber, seminars;
    TextView exp;
    ArrayList<String> Xlist = new ArrayList<>();
    ArrayList<String> Qlist = new ArrayList<>();
    ArrayList<String> Mlist = new ArrayList<>();
    ArrayList<String> Rlist = new ArrayList<>();
    ArrayList<String> Slist = new ArrayList<>();

    ArrayList<DoctorExperience> experianceList = DoctorDescription.Listexperience;
    ArrayList<DoctorPresentation> presentList = DoctorDescription.presentationArrayList;
    ArrayList<DoctorRegnumber> regnumbers = DoctorDescription.regnumberArrayList;
    ArrayList<DoctorAword> awordsList = DoctorDescription.doctorAwordList;

    ArrayAdapter<String> adapterexpe;
    String mTxtexp;


    public ProfileFrag() {
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            experianceList = getArguments().getParcelableArrayList("KeyExperiance");
            presentList = getArguments().getParcelableArrayList("KeyPresentation");
            regnumbers = getArguments().getParcelableArrayList("KeyRegistration");
            awordsList = getArguments().getParcelableArrayList("KeyAwards");
            mTxtexp = getArguments().getString("KeyExp");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_doc, container, false);
        xperiance = view.findViewById(R.id.list_xperiance);
        qualify = view.findViewById(R.id.list_qualification);
        membership = view.findViewById(R.id.list_membership);
        registrationNumber = view.findViewById(R.id.list_registration_numbers);
        seminars = view.findViewById(R.id.list_seminars);
        exp = view.findViewById(R.id.doc_exp);
        exp.setText(mTxtexp);

        for (int i = 0; i < experianceList.size(); i++) {
            String str = "From "+experianceList.get(i).startYear + "to " + experianceList.get(i).endYear;
            Xlist.add("" + str);
            adapterexpe = new ArrayAdapter<String>(getActivity(), R.layout.timeslot_list, R.id.timeslot, Xlist);
            xperiance.setAdapter(adapterexpe);
        }
        for (int i = 0; i < presentList.size(); i++) {
            String str = presentList.get(i).seminorsDetails;
            Slist.add("" + str);
            adapterexpe = new ArrayAdapter<String>(getActivity(), R.layout.timeslot_list, R.id.timeslot, Slist);
            xperiance.setAdapter(adapterexpe);
        }
        for (int i = 0; i < regnumbers.size(); i++) {
            String str = regnumbers.get(i).registrationNumber;
            Rlist.add("Position" + str);
            adapterexpe = new ArrayAdapter<String>(getActivity(), R.layout.timeslot_list, R.id.timeslot, Rlist);
            xperiance.setAdapter(adapterexpe);
        }
        for (int i = 0; i < awordsList.size(); i++) {
            String str = awordsList.get(i).doctorMembershipAwordsDetails;
            Mlist.add("Position" + str);
            adapterexpe = new ArrayAdapter<String>(getActivity(), R.layout.timeslot_list, R.id.timeslot, Mlist);
            xperiance.setAdapter(adapterexpe);
        }
        return view;
    }
}
