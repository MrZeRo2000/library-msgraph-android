package com.romanpulov.library.msgraph.testapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

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
import java.io.PrintWriter;

public class FirstFragment extends Fragment {
    private static final String TAG = FirstFragment.class.getSimpleName();

    private FragmentFirstBinding binding;
    private MainModel model;

    private ActivityResultLauncher<Intent> mPickerResult;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPickerResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    String pickerResult = result.getData().getStringExtra(HrPickerActivity.PICKER_RESULT);
                    Toast.makeText(getContext(), "Result from mPickerResult: " + pickerResult, Toast.LENGTH_SHORT).show();
                }
        );

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    private void displaySuccess(String message) {
        model.setResultState(new MainModel.ResultState(MainModel.Status.STATUS_SUCCESS, message));
    }

    private void displayFailure(String message) {
        model.setResultState(new MainModel.ResultState(MainModel.Status.STATUS_FAILURE, message));
    }

    private void updateTextResult(MainModel.ResultState resultState) {
        if (resultState != null) {
            int color = resultState.status().equals(MainModel.Status.STATUS_SUCCESS) ?
                    android.R.color.black : android.R.color.holo_red_dark;
            binding.textResult.setTextColor(ContextCompat.getColor(requireContext(), color));
            binding.textResult.setText(resultState.text());
        }
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model = new ViewModelProvider(requireActivity()).get(MainModel.class);
        model.getResultState().observe(requireActivity(), resultState -> {
            Log.d(TAG, "Updated resultState: " + resultState);
            updateTextResult(resultState);
        });

        binding.buttonFirst.setOnClickListener(view1 ->
                NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment));

        binding.buttonPicker.setOnClickListener(v ->
                mPickerResult.launch(new Intent(getActivity(), HrPickerActivity.class)));

        binding.loginButton.setOnClickListener(v -> {
            displaySuccess("Logging in ...");
            MSGraphHelper.getInstance().login(getActivity(), new OnMSActionListener<>() {
                @Override
                public void onActionSuccess(int action, String data) {
                    displaySuccess("Obtained data:" + data);
                }

                @Override
                public void onActionFailure(int action, String errorMessage) {
                    displayFailure("Login error:" + errorMessage);
                }
            });
        });

        binding.logoutButton.setOnClickListener(v -> {
            displaySuccess("Logging out ...");
            MSGraphHelper.getInstance().logout(getContext(), new OnMSActionListener<>() {
                @Override
                public void onActionSuccess(int action, Void data) {
                    displaySuccess("Successfully signed out");
                }

                @Override
                public void onActionFailure(int action, String errorMessage) {
                    displayFailure("Sign out error:" + errorMessage);
                }
            });
        });

        binding.loadButton.setOnClickListener(v ->
                MSGraphHelper.getInstance().load(getContext(), new OnMSActionListener<>() {
                    @Override
                    public void onActionSuccess(int action, Void data) {
                        displaySuccess("Successfully loaded account");
                    }

                    @Override
                    public void onActionFailure(int action, String errorMessage) {
                        displayFailure("Load account error:" + errorMessage);
                    }
                }));

        binding.listItemsButton.setOnClickListener(
                v -> MSGraphHelper.getInstance().listItems(
                getContext(),
                "/",
                        new OnMSActionListener<>() {
                            @Override
                            public void onActionSuccess(int action, JSONObject data) {
                                displaySuccess("Successfully obtained data: " + data.toString());
                            }

                            @Override
                            public void onActionFailure(int action, String errorMessage) {
                                displayFailure("Error obtaining data: " + errorMessage);
                            }
                        }
        ));

        binding.listItemsFolderButton.setOnClickListener(v -> MSGraphHelper.getInstance().listItems(
                getContext(),
                "/Empty",
                new OnMSActionListener<>() {
                    @Override
                    public void onActionSuccess(int action, JSONObject data) {
                        displaySuccess("Successfully obtained data: " + data.toString());
                    }

                    @Override
                    public void onActionFailure(int action, String errorMessage) {
                        displayFailure("Error obtaining data: " + errorMessage);
                    }
                }
        ));

        binding.downloadFileButton.setOnClickListener(v ->
                MSGraphHelper.getInstance().getBytesByPath(
                getContext(),
                "/Getting started with OneDrive.pdf",
                new OnMSActionListener<>() {
                    @Override
                    public void onActionSuccess(int action, byte[] data) {
                        File file = new File(getContext().getCacheDir(), "test stream.pdf");
                        displaySuccess("Successfully obtained stream, writing to file " + file.getAbsolutePath());

                        try (
                                InputStream in = new ByteArrayInputStream(data);
                                OutputStream out = new FileOutputStream(file)
                        ) {
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
        ));

        binding.uploadFileButton.setOnClickListener(v -> {
            File file = new File(getContext().getCacheDir(), "test stream.pdf");

            try (
                    InputStream in = new FileInputStream(file);
                    ByteArrayOutputStream out = new ByteArrayOutputStream()
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
                        "/New/test_put.pdf",
                        bytes,
                        new OnMSActionListener<>() {
                            @Override
                            public void onActionSuccess(int action, String data) {
                                displaySuccess("Successfully uploaded data: " + data);
                            }

                            @Override
                            public void onActionFailure(int action, String errorMessage) {
                                displayFailure("Error uploading data: " + errorMessage);
                            }
                        }
                );

            } catch (IOException e) {
                displayFailure("Error writing ByteArrayOutputStream: " + e.getMessage());
            }

        });

        binding.uploadFileListButton.setOnClickListener(v -> {
            // prepare files
            final int fileCount = 10;
            File[] files = new File[fileCount];

            for (int i = 0; i < fileCount; i++) {
                File f = new File(getContext().getCacheDir(), "f" + i + ".txt");
                try (
                        FileOutputStream outputStream = new FileOutputStream(f);
                        PrintWriter printWriter = new PrintWriter(outputStream)
                        ) {

                    printWriter.write("Data:" + i);

                } catch (IOException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                files[i] = f;
            }

            MSGraphHelper.getInstance().putFiles(
                    getContext(),
                    "/FilesToTest",
                    files,
                    new OnMSActionListener<>() {
                        @Override
                        public void onActionSuccess(int action, Void data) {
                            displaySuccess("Successfully written files");
                        }

                        @Override
                        public void onActionFailure(int action, String errorMessage) {
                            displayFailure("Error writing files: " + errorMessage);
                        }
                    }
            );
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}