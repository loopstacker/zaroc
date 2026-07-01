package be.kdg.integration.gameapplication.view.customalert;

import be.kdg.integration.gameapplication.model.alertproperties.AlertSetting;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class CustomAlertCall{
    private CustomAlert alert;
    private Timeline disappearAnimation;
    private float animationDuration;
    private AlertType alertType;
    private Pane source;

    public CustomAlertCall(AlertType alertType, AlertSetting alertSetting, Pane sourceWindow){
        this.alert = new CustomAlert();
        this.source = sourceWindow;
        this.alertType = alertType;
        this.animationDuration = alertSetting.getDuration();
        handleAlertContent(alertSetting.getTitle(), alertSetting.getMessage());
        handleAlertType(alertType);
        handleScreenBlocker(alertSetting.isScreenBlocked());
        initializeAnimation();
        callAlert();
    }

    private void handleScreenBlocker(boolean isScreenBlocked){
        alert.setMouseTransparent(!isScreenBlocked);
    }

    private void handleAlertContent(String title, String message){
        if(title.isEmpty()) title = alertType.name().toLowerCase();
        alert.getTitle().setText(title);
        alert.getDescription().setText(message);
    }

    private void callAlert(){
        Scene scene = source.getScene();
        StackPane root = wrapAsStackPane(scene);
        root.getChildren().add(alert);
        disappearAnimation.setOnFinished(e -> {
            root.getChildren().remove(alert);
            if(scene.getRoot() != root){
                root.getChildren().clear();
                return;
            }
            unwrap(scene, root);
        });
        disappearAnimation.play();
    }

    private void unwrap(Scene scene, StackPane root){
        //we have to unwrap it back
        //otherwise we won't be able to run same screen twice
        //because it will be a prt of another pane
        if(!(scene.getRoot() instanceof StackPane wrappedStack && wrappedStack == source)){
            if(root.getChildren().size() == 1){
                Pane source = (Pane) root.getChildren().getFirst(); //so we are safely extracting it
                root.getChildren().clear(); //removing all leftovers
                scene.setRoot(source); //and returning unwrapped source pane to scene
            }
        }
    }

    private StackPane wrapAsStackPane(Scene scene){
        //we have to wrap it in a stack pane, so we can place error on top of the screen
        if(scene.getRoot() instanceof StackPane sp){
            return sp;
        }

        StackPane wrapper = new StackPane(scene.getRoot()); //getting parent node
        wrapper.getStylesheets().addAll(scene.getStylesheets()); //keeping old style sheets
        wrapper.setBackground(Background.EMPTY);
        scene.setRoot(wrapper);
        return wrapper;
    }

    private void handleAlertType(AlertType alertType){
        ObservableList<String> style = alert.getErrorLayout().getStyleClass();
        style.clear();
        switch(alertType){
            case AlertType.CONFIRMATION -> style.add("custom-alert-confirmation");
            case AlertType.ERROR -> style.add("custom-alert-error");
            case AlertType.INFORMATION -> style.add("custom-alert-information");
            default -> style.add("custom-alert-others");
        }
    }


    private void initializeAnimation(){
        disappearAnimation = new Timeline();
        final float seconds = animationDuration; //adjust that one to change duration.
        // Everything else would be calculated
        final float framesPerSecond = 60;
        final float speed = 10F / seconds;

        for(int i = 1; i < framesPerSecond * seconds; i++){
            float x = (i / framesPerSecond) / (10F / speed);
            float ratio = 1F - x;
            disappearAnimation.getKeyFrames().add(
                    new KeyFrame(Duration.seconds(i / framesPerSecond), e -> {
                        alert.opacityProperty().set(ratio);
                    })
            );
        }
    }
}
