package com.romanpulov.library.msgraph.testapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.romanpulov.library.msgraph.MSActionException;
import com.romanpulov.library.msgraph.OnMSActionListener;
import com.romanpulov.library.msgraph.testapp.databinding.FragmentFirstBinding;


import org.json.JSONObject;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private void displaySuccess(String message) {
        //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        binding.textResult.setTextColor(getResources().getColor(android.R.color.black));
        binding.textResult.setText(message);
    }

    private void displayFailure(String message) {
        //Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        binding.textResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        binding.textResult.setText(message);
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSGraphHelper.getInstance().login(getActivity(), new OnMSActionListener<String>() {
                    @Override
                    public void onActionSuccess(int action, String data) {
                        displaySuccess("Obtained data:" + data);
                    }

                    @Override
                    public void onActionFailure(int action, String errorMessage) {
                        displayFailure("Login error:" + errorMessage);
                    }
                });
            }
        });

        binding.logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSGraphHelper.getInstance().logout(getContext(), new OnMSActionListener<Void>() {
                    @Override
                    public void onActionSuccess(int action, Void data) {
                        displaySuccess("Successfully signed out");
                    }

                    @Override
                    public void onActionFailure(int action, String errorMessage) {
                        displayFailure("Sign out error:" + errorMessage);
                    }
                });
            }
        });

        binding.listItemsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSGraphHelper.getInstance().listItems(
                        getContext(),
                        "root",
                        new OnMSActionListener<JSONObject>() {
                            @Override
                            public void onActionSuccess(int action, JSONObject data) {
                                displaySuccess("Successfully obtained data: " + data.toString());
                            }

                            @Override
                            public void onActionFailure(int action, String errorMessage) {
                                displayFailure("Error obtaining data: " + errorMessage);
                            }
                        }
                );
            }
        });

        binding.listItemsFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSGraphHelper.getInstance().listItems(
                        getContext(),
                        "root:/Empty:",
                        new OnMSActionListener<JSONObject>() {
                            @Override
                            public void onActionSuccess(int action, JSONObject data) {
                                displaySuccess("Successfully obtained data: " + data.toString());
                            }

                            @Override
                            public void onActionFailure(int action, String errorMessage) {
                                displayFailure("Error obtaining data: " + errorMessage);
                            }
                        }
                );
            }
        });


        binding.listItemsSyncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject result = MSGraphHelper.getInstance().listItemsSync(getContext());
                    displaySuccess("Successfully obtained data: " + (result == null? "null" : result.toString()));
                } catch (MSActionException e) {
                    e.printStackTrace();
                    displayFailure(e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}