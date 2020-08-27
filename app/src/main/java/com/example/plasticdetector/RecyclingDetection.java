package com.example.plasticdetector;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

class RecyclingDetection{
    private Context c;
    public RecyclingDetection(Context c) {
        this.c = c;
    }
    private int degreesToFirebaseRotation(int degrees) {
        switch (degrees) {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException(
                        "Rotation must be 0, 90, 180, or 270.");
        }
    }
    //@Override
    public void analyze(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        //Image mediaImage = imageProxy.getImage();
        //int rotation = degreesToFirebaseRotation(degrees);
        FirebaseVisionImage image =
                FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer detect = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();
        Task<FirebaseVisionText> result =
                detect.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText resultText) {
                                String text = resultText.getText();
                                Log.i("myTag", text);

                                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                                if(text != null) {
                                    if (text.indexOf("PETE") != -1) {
                                        builder.setTitle("Your plastic is Polyethylene Terephthalate");
                                        builder.setMessage("PETE is a clear, tough plastic found in bottles. This can be easily recycled in a curbside recycling program.").setCancelable(true);
                                    } else if (text.indexOf("HDPE") != -1) {
                                        builder.setTitle("Your plastic is High Density Polyethylene");
                                        builder.setMessage("HDPE is a white plastic commonly used in packaging. This can be easily recycled in a curbside recycling program. However, some programs only allow containers with necks to be recycled.").setCancelable(true);
                                    } else if (text.indexOf("PVC") != -1) {
                                        builder.setTitle("Your plastic is Polyvinyl Chloride");
                                        builder.setMessage("PVC can either be hard and rigid or soft and flexible. It is commonly used in pipes, siding, and other building applications. PVC should never be burnt and is not easily recyclable. Some synthetic timber manufacturers may accept it.").setCancelable(true);
                                    } else if (text.indexOf("LDPE") != -1) {
                                        builder.setTitle("Your plastic is Low Density Polyethylene");
                                        builder.setMessage("LDPE is a soft, flexible plastic found in shopping bags, squeezable bottles, and food bags. It is not easily recycled in a curbside recycling program, but many stores have programs to recycle it.").setCancelable(true);
                                    }
                                    else if (text.indexOf("PP") != -1) {
                                        builder.setTitle("Your plastic is Polypropylene");
                                        builder.setMessage("PP is a hard, flexible plastic found in straws, ice cream containers, and lunch boxes. It is only recycled in some curbside programs.").setCancelable(true);
                                    }
                                    else if (text.indexOf("PS") != -1) {
                                        builder.setTitle("Your plastic is Polystyrene");
                                        builder.setMessage("PS is either rigid and brittle or foam. It is found in imitation crystal, cups, CD cases, and many more items. It is only recycled in some curbside programs.").setCancelable(true);
                                    }
                                    else if (text.indexOf("OTHER") != -1) {
                                        builder.setTitle("Your plastic is miscellaneous");
                                        builder.setMessage("This could be nylon, acrylic, polycarbonate, or many more types.").setCancelable(true);
                                    }
                                    else {
                                        builder.setTitle("No plastic was detected");
                                        builder.setMessage("Try repositioning your camera and focusing on the recycling symbol.").setCancelable(true);
                                    }
                                }
                                builder.show();
                            }
                        })
                        .addOnFailureListener(
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(c);
                                        Log.i("myTag", "failed");
                                        builder.setMessage("Failed");
                                        builder.show();
                                    }
                                });
    }
}

