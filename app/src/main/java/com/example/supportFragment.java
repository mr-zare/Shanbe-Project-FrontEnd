package com.example;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.entity.Movie;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link supportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class supportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public supportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment supportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static supportFragment newInstance(String param1, String param2) {
        supportFragment fragment = new supportFragment();
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
        // Inflate the layout for this fragment
        RecyclerView recyclerView;
        View rootView = inflater.inflate(R.layout.fragment_support, container, false);
        List<Movie> movieList;
        movieList = new ArrayList<>();
        movieList.add(new Movie("How to sign up?", "", "", "If you don't have one, You can easily make one in sign up page by entering username, email and password."));
        movieList.add(new Movie("How to login?", "", "", "After you made an account you can go to login page and enter your username and password."));
        movieList.add(new Movie("I can't remember my password", "", "", "if you forgot your password, click on the button 'I forgot password' then we email verification code to your email so you can reset your password."));
        movieList.add(new Movie("i can't update photo profile", "", "", "Check permission to access local files so you can choose on and upload it."));
        movieList.add(new Movie("How to add task?", "", "", "In calendar page, go to a specific day(by pressing on day number) then you can see a plus sign in right corner.\nNow enter task information then press save button."));
        movieList.add(new Movie("Where can i see my tasks?", "", "", "You can see your current day tasks in list below calendar.\nAlso you can see tasks in day page(Press on day number to go to day page)."));
        movieList.add(new Movie("How to edit my profile?","","","In sidebar you can see an item named 'Edit Profile',Press on that and then press on Edit button again,Now enter you information and press button."));
        movieList.add(new Movie("What is an event?","","","Basically an event is a task shared between a group of users,you can create one in My event item."));
        movieList.add(new Movie("Where can i see my events?","","","There is an item named 'My events',You can find it in sidebar."));
        movieList.add(new Movie("How to join an event?","","","If it is private go to private section in side bar else go to Search & Join"));
        movieList.add(new Movie("How to sync with google calendar?","","","There is a button named sync you can enter you google account information there."));
        movieList.add(new Movie("What are sessions in event?","","","Basically sessions divide event to various time parts,So you can reserve a specific time."));
        MovieAdapter movieAdapter = new MovieAdapter(movieList);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(movieAdapter);

        return rootView;
    }
}