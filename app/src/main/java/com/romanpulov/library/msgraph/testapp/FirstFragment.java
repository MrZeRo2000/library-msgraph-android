package com.romanpulov.library.msgraph.testapp;

import android.os.Bundle;
import android.util.Log;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
                        "/",
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
                        "/Empty",
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

        binding.downloadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MSGraphHelper.getInstance().getBytesByPath(
                        getContext(),
                        "/Getting started with OneDrive.pdf",
                        new OnMSActionListener<byte[]>() {
                            @Override
                            public void onActionSuccess(int action, byte[] data) {
                                File file = new File(getContext().getCacheDir(), "test stream.pdf");
                                displaySuccess("Successfully obtained stream, writing to file " + file.getAbsolutePath());

                                try (
                                        InputStream in = new ByteArrayInputStream(data);
                                        OutputStream out = new FileOutputStream(file);
                                        )
                                {
                                    // Transfer bytes from in to out
                                    byte[] buf = new byte[1024];
                                    int len;
                                    while ((len = in.read(buf)) > 0) {
                                        out.write(buf, 0, len);
                                    }

                                } catch (IOException e) {
                                    displayFailure("Error writing output stream: " + e.getMessage());
                                }
                            }

                            @Override
                            public void onActionFailure(int action, String errorMessage) {
                                displayFailure("Error obtaining data: " + errorMessage);
                            }
                        }
                );
            }
        });

        binding.uploadFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(getContext().getCacheDir(), "test stream.pdf");

                try (
                        InputStream in = new FileInputStream(file);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        ) {
                    // Transfer bytes from in to out
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    byte[] bytes = out.toByteArray();

                    MSGraphHelper.getInstance().putBytesByPath(
                            getContext(),
                            "/test_put.pdf",
                            bytes,
                            new OnMSActionListener<String>() {
                                @Override
                                public void onActionSuccess(int action, String data) {
                                    displaySuccess("Successfully uploaded data: " + data);
                                }

                                @Override
                                public void onActionFailure(int action, String errorMessage) {
                                    displayFailure("Writing data: " + errorMessage);
                                }
                            }
                    );

                } catch (IOException e) {
                    displayFailure("Error writing ByteArrayOutputStream: " + e.getMessage());
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