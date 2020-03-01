package ch.epfl.sdp.game;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentTargetSelectionBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TargetSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TargetSelectionFragment extends Fragment {
    public static int NO_TARGET = -1;
    public interface OnTargetSelectedListener {
        void OnTargetSelected(int targetID);
    }

    public static class TargetNotFound extends IndexOutOfBoundsException {}

    private static final String ARG_TARGETS = "targets";

    private FragmentTargetSelectionBinding binding;
    private AlertDialog targetSelectionDialog;
    private OnTargetSelectedListener listener;
    private Map<Integer, Player> targets;
    private int selectedTargetID;

    public TargetSelectionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param targets List of all selectable targets
     * @return A new instance of fragment TargetSelectionFragment.
     */
    public static <T extends List<Player> & Serializable> TargetSelectionFragment newInstance(T targets) {
        TargetSelectionFragment fragment = new TargetSelectionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TARGETS, targets);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //noinspection unchecked // TODO Do this in a cleaner way
            final List<Player> targetList = (List<Player>) getArguments().getSerializable(ARG_TARGETS);
            assert targetList != null; // TODO Do this in a cleaner way

            targets = new HashMap<>();
            for (Player p : targetList) {
                targets.put(p.getId(), p);
            }

            selectedTargetID = NO_TARGET;

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Select a target")
                    .setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, targetList), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            selectedTargetID = targetList.get(which).getId();
                            updateTargetDisplay();
                        }
                    });

            targetSelectionDialog = builder.create();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTargetSelectionBinding.inflate(inflater, container, false);
        updateTargetDisplay();

        binding.mainFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetSelectionDialog.show();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (OnTargetSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnTargetSelectedListener");
        }
    }

    public int getSelectedTargetID() {
        return selectedTargetID;
    }

    public void unsetSelectedTargetID() {
        selectedTargetID = NO_TARGET;
        updateTargetDisplay();
        listener.OnTargetSelected(selectedTargetID);
    }

    public void setSelectedTargetID(int targetID) {
        if (targets.containsKey(targetID)) {
            selectedTargetID = targetID;
            updateTargetDisplay();
            listener.OnTargetSelected(selectedTargetID);
        } else {
            throw new TargetNotFound();
        }
    }

    private void updateTargetDisplay() {
        if (selectedTargetID == NO_TARGET) {
            binding.currentTarget.setText(R.string.no_target);
        } else {
            binding.currentTarget.setText(String.format(Locale.getDefault(), "Player %d", selectedTargetID));
        }
    }
}
