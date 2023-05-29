package com.example.project_sesjopoli;

import javafx.scene.Group;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

class SmartGroup extends Group {
    public static final int ROTATION_POINT_X = 1000;
    public static final int ROTATION_POINT_Y = 1000;
    private Rotate r;
    private Transform t = new Rotate();

    void rotateByX(int ang) {
        r = new Rotate(ang, Rotate.X_AXIS);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }

    void rotateByZ(double ang) {
        r = new Rotate(ang, ROTATION_POINT_X, ROTATION_POINT_Y);
        t = t.createConcatenation(r);
        this.getTransforms().clear();
        this.getTransforms().addAll(t);
    }


}