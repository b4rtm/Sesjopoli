package com.example.project_sesjopoli.game_objects;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

public class House extends Group {
    public House(){
        Image roofImage = new Image("roof.jpg");
        PhongMaterial roof = new PhongMaterial();
        roof.setDiffuseMap(roofImage);

        Image wallImage = new Image("wall.jpg");
        PhongMaterial wall = new PhongMaterial();
        wall.setDiffuseMap(wallImage);


        Box wallBox = new Box(70, 70, 80);
        Box roofBox = new Box(55, 68, 55);
        wallBox.setMaterial(wall);
        wallBox.setTranslateZ(-40);
        this.getChildren().add(wallBox);

        roofBox.setMaterial(roof);
        roofBox.setTranslateZ(-80);

        Rotate rotateX = new Rotate(45, Rotate.X_AXIS);
        Rotate rotateZ = new Rotate(90, Rotate.Z_AXIS);

        roofBox.getTransforms().addAll(rotateX, rotateZ);

        this.getChildren().add(roofBox);
    }
}
