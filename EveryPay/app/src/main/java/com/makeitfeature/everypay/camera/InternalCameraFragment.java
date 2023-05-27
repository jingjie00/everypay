package com.makeitfeature.everypay.camera;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;


import com.google.common.util.concurrent.ListenableFuture;
import com.makeitfeature.everypay.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * create an instance of this fragment.
 */
public class InternalCameraFragment extends Fragment {
    public InternalCameraFragment() {
        super(R.layout.internal_camera);
    }

    private PreviewView previewView;
    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    ProcessCameraProvider cameraProvider;

    ImageCapture imageCapture;
    TextView textView;

    static int currentCamera = CameraSelector.LENS_FACING_BACK;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.internal_camera, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onViewCreated(view, savedInstanceState);
        camBind(currentCamera);
        FrameLayout frameLayout = view.findViewById(R.id.internalCamView);
        frameLayout.addView(previewView);
        textView = getActivity().findViewById(R.id.outputText);

    }

    void camBind(int cur) {
        previewView = null;
        previewView = new PreviewView(getContext());
        previewView.setScaleType(PreviewView.ScaleType.FIT_CENTER);
        previewView.setBackgroundColor(255);

        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (InterruptedException | ExecutionException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }


    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {


        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(currentCamera)
                .build();

        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        imageCapture =
                new ImageCapture.Builder()
                        .build();

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();


        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new ImageAnalysis.Analyzer() {

            @Override
            public void analyze(@NonNull ImageProxy imageProxy) {


                @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
                // Camera Feed-->Analyzer-->ImageProxy-->mediaImage-->InputImage(needed for ML kit face detection)

                Image mediaImage = imageProxy.getImage();
                Log.d("aa", "|"+imageProxy.getFormat());
                //code
            }
        });

        cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, imageCapture, imageAnalysis, preview);

    }


}