package com.example.project_sesjopoli;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;

public class CameraFactory {

    public static final int X = 1030;
    public static final int Y = 1000;
    public static final int Z = -4100;
    public static final int NEAR_CLIP_VALUE = 1;
    public static final int FAR_CLIP_VALUE = 10000;

    Camera initCamera(){
        Camera camera = new PerspectiveCamera(true);
        camera.translateXProperty().set(X);
        camera.translateYProperty().set(Y);
        camera.translateZProperty().set(Z);
        camera.setNearClip(NEAR_CLIP_VALUE);
        camera.setFarClip(FAR_CLIP_VALUE);
        return camera;
    }
}
