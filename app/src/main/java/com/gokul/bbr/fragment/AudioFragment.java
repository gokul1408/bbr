package com.gokul.bbr.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gokul.bbr.databinding.FragmentSecondBinding;
import com.gokul.bbr.model.Music;
import com.gokul.bbr.viewmodel.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AudioFragment extends Fragment {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private final String[] permissions = {Manifest.permission.RECORD_AUDIO};
    FragmentSecondBinding binding;
    HomeViewModel homeViewModel;
    boolean mStartRecording = true;
    private String fileName = null;
    private MediaRecorder recorder = null;

    @Override
    public View onCreateView(
            @NotNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVm();
        initView();

    }

    private void initView() {
        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        binding.button.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);
            } else {
                if (mStartRecording) {
                    onRecord(true);
                    binding.button.setText("Stop Recording");
                } else {
                    onRecord(false);
                    binding.button.setText("Start Recording");
                }
                mStartRecording = !mStartRecording;
            }
        });
    }

    private void initVm() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        // Record to the external cache directory for visibility
        fileName = getContext().getExternalCacheDir().getAbsolutePath();
        String time = String.valueOf(System.currentTimeMillis());
        fileName += "/" + time + ".mp3";
        Log.d("Hello", fileName);

        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("AUDIO_FRAG", "prepare() failed");
        }
        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
        updateAudioList();
    }

    private void updateAudioList() {
        Music music = new Music();
        music.setType(1);
        String audioName = "BBR" + System.currentTimeMillis();
        music.setTitle(audioName);
        music.setSource(fileName);
        homeViewModel.getAudio().postValue(music);
        Toast.makeText(getContext(), "Audio file saved", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            boolean permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }
    }
}