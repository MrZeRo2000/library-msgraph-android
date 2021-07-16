package com.romanpulov.library.msgraph.testapp;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HrPickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class    HrPickerFragment extends Fragment implements HrPickerScreen.OnHrPickerScreenUpdateListener {
    private static final String TAG = HrPickerFragment.class.getSimpleName();

    public static final String RESULT_KEY = HrPickerFragment.class.getSimpleName() + "_RESULT";
    public static final String RESULT_VALUE_KEY = HrPickerFragment.class.getSimpleName() + "_RESULT_VALUE";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INITIAL_PATH = "INITIAL_PATH";

    // TODO: Rename and change types of parameters
    private String mInitialPath;
    private boolean mIsEmpty = true;


    private TextView mHeader;
    private RecyclerView.Adapter<?> mAdapter;


    private HrPickerScreen mPickerScreen;

    public HrPickerScreen getPickerScreen() {
        return mPickerScreen;
    }

    public void setPickerScreen(HrPickerScreen mPickerScreen) {
        this.mPickerScreen = mPickerScreen;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        final View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mTextView = v.findViewById(R.id.hr_picker_text);
        }
    }

    public class HrPickerAdapter extends RecyclerView.Adapter<HrPickerFragment.ViewHolder> {
        private final List<HrPickerItem> mItems;

        HrPickerAdapter(List<HrPickerItem> items) {
            mItems = items;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_picker_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
            switch (mItems.get(position).itemType) {
                case HrPickerItem.ITEM_TYPE_PARENT:
                    holder.mTextView.setText("parent");
                    //holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_more_horiz, 0, 0, 0);
                    break;
                case HrPickerItem.ITEM_TYPE_FOLDER:
                    holder.mTextView.setText(mItems.get(position).name);
                    //holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_folder_closed, 0, 0, 0);
                    break;
                case HrPickerItem.ITEM_TYPE_FILE:
                    holder.mTextView.setText(mItems.get(position).name);
                    // holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_document, 0, 0, 0);
                    break;
                default:
                    holder.mTextView.setText(mItems.get(position).name);
                    break;
            }

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HrPickerItem selectedItem = mItems.get(holder.getAdapterPosition());

                    switch (selectedItem.itemType) {
                        case HrPickerItem.ITEM_TYPE_FILE:
                            Bundle result = new Bundle();
                            result.putString(RESULT_VALUE_KEY, HrPickerScreen.combinePath(mPickerScreen.getCurrentPath(), selectedItem));
                            HrPickerFragment.this.getParentFragmentManager().setFragmentResult(RESULT_KEY, result);
                            break;
                        case HrPickerItem.ITEM_TYPE_PARENT:
                            HrPickerFragment.this.mPickerScreen.navigate(
                                    getContext(),
                                    HrPickerFragment.this.mPickerScreen.getParentPath(),
                                    selectedItem
                                    );
                        case HrPickerItem.ITEM_TYPE_FOLDER:
                            HrPickerFragment.this.mPickerScreen.navigate(
                                    getContext(),
                                    HrPickerFragment.this.mPickerScreen.getCurrentPath(),
                                    selectedItem
                            );
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }


    @Override
    public void onUpdate(HrPickerScreen hrPickerScreen) {
        Log.d(TAG, "onUpdate");
        mAdapter.notifyDataSetChanged();
        mIsEmpty = false;
    }

    public HrPickerFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static HrPickerFragment newInstance(String initialPath) {
        HrPickerFragment fragment = new HrPickerFragment();
        Bundle args = new Bundle();
        args.putString("InitialPath", initialPath);
        fragment.setArguments(args);
        fragment.setPickerScreen(new HrPickerScreen(initialPath));
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mInitialPath = getArguments().getString(INITIAL_PATH);
        }
        setRetainInstance(true);

        mAdapter = new HrPickerAdapter(mPickerScreen.getItems());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_hr_picker, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.picker_list);
        if (getActivity() != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        recyclerView.setAdapter(mAdapter);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPickerScreen.setPickerScreenUpdateListener(this);

        if (mIsEmpty && getContext() != null) {
            Log.d(TAG, "The fragment is empty, navigating");
            mPickerScreen.navigate(getContext(), mPickerScreen.getCurrentPath(), null);
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}