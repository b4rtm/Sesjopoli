package com.example.project_sesjopoli.game_objects;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class House extends Group {

    private int ownerId;
    private List<Color> colors;
    public House(int id){
        this.ownerId=id;
        colors = new ArrayList<>(Arrays.asList(Color.YELLOW, Color.GREEN, Color.BLUE, Color.ORANGE));

        PhongMaterial roof = new PhongMaterial();
        roof.setDiffuseColor(colors.get(id));

        PhongMaterial wall = new PhongMaterial();
        wall.setDiffuseColor(colors.get(id));


        Box wallBox = new Box(70, 70, 80);
        Box roofBox = new Box(55, 67, 55);
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

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
