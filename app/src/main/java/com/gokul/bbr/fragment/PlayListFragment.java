package com.gokul.bbr.fragment;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gokul.bbr.R;
import com.gokul.bbr.adapter.AudioListAdapter;
import com.gokul.bbr.databinding.FragmentFirstBinding;
import com.gokul.bbr.interfaces.PlayListClickListener;
import com.gokul.bbr.model.Music;
import com.gokul.bbr.viewmodel.HomeViewModel;

import java.util.List;

public class PlayListFragment extends Fragment implements PlayListClickListener {

    FragmentFirstBinding binding;
    HomeViewModel homeViewModel;
    AudioListAdapter audioListAdapter;

    MediaPlayer mediaPlayer;
    double current_pos, total_duration;
    int audio_index = 0;
    List<Music> audioArrayList;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRvAdapter();
        initVm();

    }

    private void initRvAdapter() {
        audioListAdapter = new AudioListAdapter(getContext(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(audioListAdapter);
    }

    private void initVm() {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getMusicModelMutableLiveData().observe(getViewLifecycleOwner(), item -> {
            if (item != null) {
                audioListAdapter.setmList(item);
                initPlayer();

            }
        });
        homeViewModel.getAudio().observe(getViewLifecycleOwner(), audio -> {
            if (audio != null) {
                audioListAdapter.getmList().add(0, audio);
                audioListAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initPlayer() {
        audioArrayList = audioListAdapter.getmList();
        mediaPlayer = new MediaPlayer();

        //seekbar change listner
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                current_pos = seekBar.getProgress();
                mediaPlayer.seekTo((int) current_pos);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                audio_index++;
                if (audio_index < (audioArrayList.size())) {
                    playAudio(audio_index);
                } else {
                    audio_index = 0;
                    playAudio(audio_index);
                }

            }
        });

        if (!audioArrayList.isEmpty()) {
//            playAudio(audio_index);
            prevAudio();
            nextAudio();
            setPause();
        }

    }

    //play audio file
    public void playAudio(int pos) {
        try {
            mediaPlayer.reset();
            //set file path
            int type = audioListAdapter.getmList().get(pos).getType();
            String fileName = "";
            if (type == 1) {
                fileName = audioListAdapter.getmList().get(pos).getSource();
            } else {
                fileName = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-7.mp3";
            }
            Uri uri = Uri.parse(fileName);
            mediaPlayer.setDataSource(getContext(), uri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            binding.pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            binding.audioName.setText(audioListAdapter.getmList().get(pos).getTitle());
            audio_index = pos;
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAudioProgress();
    }

    //set audio progress
    public void setAudioProgress() {
        //get the audio duration
        current_pos = mediaPlayer.getCurrentPosition();
        total_duration = mediaPlayer.getDuration();

        //display the audio duration
        binding.total.setText(timerConversion((long) total_duration));
        binding.current.setText(timerConversion((long) current_pos));
        binding.seekbar.setMax((int) total_duration);
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    current_pos = mediaPlayer.getCurrentPosition();
                    binding.current.setText(timerConversion((long) current_pos));
                    binding.seekbar.setProgress((int) current_pos);
                    handler.postDelayed(this, 1000);
                } catch (IllegalStateException ed) {
                    ed.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 1000);
    }

    //play previous audio
    public void prevAudio() {
        binding.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio_index > 0) {
                    audio_index--;
                    playAudio(audio_index);
                } else {
                    audio_index = audioArrayList.size() - 1;
                    playAudio(audio_index);
                }
            }
        });
    }

    //play next audio
    public void nextAudio() {
        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audio_index < (audioArrayList.size() - 1)) {
                    audio_index++;
                    playAudio(audio_index);
                } else {
                    audio_index = 0;
                    playAudio(audio_index);
                }
            }
        });
    }

    //pause audio
    public void setPause() {
        binding.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    binding.pause.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                } else {
                    mediaPlayer.start();
                    binding.pause.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                }
            }
        });
    }

    //time conversion
    public String timerConversion(long value) {
        String audioTime;
        int dur = (int) value;
        int hrs = (dur / 3600000);
        int mns = (dur / 60000) % 60000;
        int scs = dur % 60000 / 1000;

        if (hrs > 0) {
            audioTime = String.format("%02d:%02d:%02d", hrs, mns, scs);
        } else {
            audioTime = String.format("%02d:%02d", mns, scs);
        }
        return audioTime;
    }

    @Override
    public void onClick(int pos) {
        playAudio(pos);
    }
}