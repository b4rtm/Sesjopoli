package com.example.project_sesjopoli;

import javafx.scene.Camera;
import javafx.scene.PerspectiveCamera;

public class CameraFactory {

    int X = 1000;
    int Y = 900;
    int Z = -4100;
    int nearClipValue = 1;
    int farClipValue = 6000;

    Camera initCamera(){
        Camera camera = new PerspectiveCamera(true);
        camera.translateXProperty().set(X);
        camera.translateYProperty().set(Y);
        camera.translateZProperty().set(Z);
        camera.setNearClip(nearClipValue);
        camera.setFarClip(farClipValue);
        return camera;
    }
}
