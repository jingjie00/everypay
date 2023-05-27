package com.makeitfeature.everypay.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.display.DisplayManager;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.CameraSelector;
import androidx.fragment.app.Fragment;

import com.makeitfeature.everypay.R;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {

    public static ArrayList<Bitmap> current_input_faces = null; //Only valid face
     //for add, only valid face
    @SuppressLint("StaticFieldLeak")
    static Context context;

    ImageButton otherCamera;

    TextView textView;

    public CameraFragment() {
        super(R.layout.fragment_camera);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.viewFrag);
        f.onStop();
        getActivity().getSupportFragmentManager().beginTransaction().remove(f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera, parent, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getContext();

        InternalCameraFragment.currentCamera = CameraSelector.LENS_FACING_BACK;
        current_input_faces = null;

        if (savedInstanceState == null) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.viewFrag, InternalCameraFragment.class, null)
                    .commit();
        }

        otherCamera = getActivity().findViewById(R.id.OtherCamera);
        otherCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current_input_faces=null;

                if (InternalCameraFragment.currentCamera == CameraSelector.LENS_FACING_BACK) {
                    InternalCameraFragment.currentCamera = CameraSelector.LENS_FACING_FRONT;
                    otherCamera.setContentDescription("Switch to back camera");
                } else {
                    InternalCameraFragment.currentCamera = CameraSelector.LENS_FACING_BACK;
                    otherCamera.setContentDescription("Switch to front camera");
                }
                Fragment f = getActivity().getSupportFragmentManager().findFragmentById(R.id.viewFrag);
                f.onStop();
                getActivity().getSupportFragmentManager().beginTransaction().remove(f);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.viewFrag, InternalCameraFragment.class, null)
                        .commit();
            }
        });

        DisplayManager.DisplayListener mDisplayListener = new DisplayManager.DisplayListener() {
            @Override
            public void onDisplayAdded(int displayId) {
            }

            @Override
            public void onDisplayChanged(int displayId) {
            }

            @Override
            public void onDisplayRemoved(int displayId) {
            }
        };
        DisplayManager displayManager = (DisplayManager) getContext().getSystemService(Context.DISPLAY_SERVICE);
        displayManager.registerDisplayListener(mDisplayListener, new Handler());

    }

    public static Bitmap getCropBitmapByCPU(Bitmap source, RectF cropRectF) {
        Bitmap resultBitmap = Bitmap.createBitmap((int) cropRectF.width(),
                (int) cropRectF.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(resultBitmap);

        // draw background
        Paint paint = new Paint(Paint.FILTER_BITMAP_FLAG);
        paint.setColor(Color.WHITE);
        canvas.drawRect(//from  w w  w. ja v  a  2s. c  om
                new RectF(0, 0, cropRectF.width(), cropRectF.height()),
                paint);

        Matrix matrix = new Matrix();
        matrix.postTranslate(-cropRectF.left, -cropRectF.top);

        canvas.drawBitmap(source, matrix, paint);


        return resultBitmap;
    }

    public static Bitmap rotateBitmap(
            Bitmap bitmap, int rotationDegrees, boolean flipX, boolean flipY) {

        Matrix matrix = new Matrix();

        // Rotate the image back to straight.
        matrix.postRotate(rotationDegrees);

        // Mirror the image along the X or Y axis.
        matrix.postScale(flipX ? -1.0f : 1.0f, flipY ? -1.0f : 1.0f);
        Bitmap rotatedBitmap;
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        // Recycle the old bitmap if it has changed.
        if (rotatedBitmap != bitmap) {
            bitmap.recycle();
        }
        return rotatedBitmap;
    }

    public static Bitmap imageToBitmap(Image image) {
        Image.Plane[] planes = image.getPlanes();
        ByteBuffer yBuffer = planes[0].getBuffer();
        ByteBuffer uBuffer = planes[1].getBuffer();
        ByteBuffer vBuffer = planes[2].getBuffer();

        int ySize = yBuffer.remaining();
        int uSize = uBuffer.remaining();
        int vSize = vBuffer.remaining();

        byte[] nv21 = new byte[ySize + uSize + vSize];
        //U and V are swapped
        yBuffer.get(nv21, 0, ySize);
        vBuffer.get(nv21, ySize, vSize);
        uBuffer.get(nv21, ySize + vSize, uSize);

        YuvImage yuvImage = new YuvImage(nv21, ImageFormat.NV21, image.getWidth(), image.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, yuvImage.getWidth(), yuvImage.getHeight()), 75, out);

        byte[] imageBytes = out.toByteArray();
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }




    public static void refresh(Context context, ArrayList<Bitmap> bitmap) {
        if (context == null)
            return;
        CameraFragment.current_input_faces = bitmap;
    }



}