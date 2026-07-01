package be.kdg.integration.gameapplication.view.settingsmenu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Arrays;
public class GameSettingsView extends BorderPane{
    private final ImageView goBackButton = new ImageView(new Image("/icons/exit_button.png"));

    private Slider leftFirstColorSlider1;
    private Slider leftFirstColorSlider2;
    private Slider leftFirstColorSlider3;
    private Slider leftSecondColorSlider1;
    private Slider leftSecondColorSlider2;
    private Slider leftSecondColorSlider3;
    private Slider leftThirdColorSlider1;
    private Slider leftThirdColorSlider2;
    private Slider leftThirdColorSlider3;

    private Slider rightFirstColorSlider1;
    private Slider rightFirstColorSlider2;
    private Slider rightFirstColorSlider3;
    private Slider rightSecondColorSlider1;
    private Slider rightSecondColorSlider2;
    private Slider rightSecondColorSlider3;
    private Slider rightThirdColorSlider1;
    private Slider rightThirdColorSlider2;
    private Slider rightThirdColorSlider3;

    private ArrayList<Slider> leftColorGradientsSliders;
    private ArrayList<Slider> rightColorGradientsSliders;

    private CheckBox defaultColorCheckBox;

    private Slider redSliderEnemyPiece;
    private Slider blueSliderEnemyPiece;
    private Slider greenSliderEnemyPiece;
    private Slider redSliderPlayerPiece;
    private Slider blueSliderPlayerPiece;
    private Slider greenSliderPlayerPiece;
    private VBox rightSliderGroup;
    private VBox leftSliderGroup;
    private Rectangle pieceEnemyToCheckColor;
    private Rectangle piecePlayerToCheckColor;
    //that settings page
    private ToggleButton[] configButtons;
    //config name
    private TextField textField;
    private Button saveButton;
    private Button defaultConfigNameButton;
    private Label currentConfigName;
    //screen resizer
    private ComboBox sizesComboBox;
    //music
    private Slider musicVolume;
    private Slider soundEffectsVolumeSlider;

    private VBox musicCheckBoxField;



    public GameSettingsView(){
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes(){

        //that settings page
        this.configButtons = new ToggleButton[5];
        //config name
        this.textField = new TextField();
        this.saveButton = new Button("Save");
        this.currentConfigName = new Label();
        this.defaultConfigNameButton = new Button("Set default");

        //screen resizer
        this.sizesComboBox = new ComboBox();

        //music and sound effects
        this.musicVolume = new Slider();
        this.soundEffectsVolumeSlider = new Slider();

        leftFirstColorSlider1 = new Slider();
        leftFirstColorSlider2 = new Slider();
        leftFirstColorSlider3 = new Slider();
        leftSecondColorSlider1 = new Slider();
        leftSecondColorSlider2 = new Slider();
        leftSecondColorSlider3 = new Slider();
        leftThirdColorSlider1 = new Slider();
        leftThirdColorSlider2 = new Slider();
        leftThirdColorSlider3 = new Slider();

        rightFirstColorSlider1 = new Slider();
        rightFirstColorSlider2 = new Slider();
        rightFirstColorSlider3 = new Slider();
        rightSecondColorSlider1 = new Slider();
        rightSecondColorSlider2 = new Slider();
        rightSecondColorSlider3 = new Slider();
        rightThirdColorSlider1 = new Slider();
        rightThirdColorSlider2 = new Slider();
        rightThirdColorSlider3 = new Slider();

        leftColorGradientsSliders = new ArrayList<>(Arrays.asList(
                leftFirstColorSlider1, leftFirstColorSlider2, leftFirstColorSlider3,
                leftSecondColorSlider1, leftSecondColorSlider2, leftSecondColorSlider3,
                leftThirdColorSlider1, leftThirdColorSlider2, leftThirdColorSlider3
        ));

        rightColorGradientsSliders = new ArrayList<>(Arrays.asList(
                rightFirstColorSlider1, rightFirstColorSlider2, rightFirstColorSlider3,
                rightSecondColorSlider1, rightSecondColorSlider2, rightSecondColorSlider3,
                rightThirdColorSlider1, rightThirdColorSlider2, rightThirdColorSlider3
        ));

        defaultColorCheckBox = new CheckBox();

        this.redSliderPlayerPiece = new Slider();
        this.blueSliderPlayerPiece = new Slider();
        this.greenSliderPlayerPiece = new Slider();
        leftSliderGroup = new VBox();

        this.redSliderEnemyPiece = new Slider();
        this.blueSliderEnemyPiece = new Slider();
        this.greenSliderEnemyPiece = new Slider();
        rightSliderGroup = new VBox();

        this.pieceEnemyToCheckColor = new Rectangle();
        this.piecePlayerToCheckColor = new Rectangle();

        this.musicCheckBoxField = new VBox();
    }

    private void layoutNodes(){

        BorderPane leftFirstColorBorderPane = new BorderPane();
        BorderPane leftSecondColorBorderPane = new BorderPane();
        BorderPane leftThirdColorBorderPane = new BorderPane();

        BorderPane rightFirstColorBorderPane = new BorderPane();
        BorderPane rightSecondColorBorderPane = new BorderPane();
        BorderPane rightThirdColorBorderPane = new BorderPane();

        Circle circleToCheckColor = new Circle();
        ScrollPane scrollPane = new ScrollPane();
        StackPane stackPaneWindowForPiece = new StackPane();
        VBox content = new VBox();

        ArrayList<StackPane> firstColorPanes = new ArrayList<>(Arrays.asList(
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane()
        ));

        ArrayList<StackPane> secondColorPanes = new ArrayList<>(Arrays.asList(
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane()
        ));

        ArrayList<StackPane> thirdColorPanes = new ArrayList<>(Arrays.asList(
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane(), new StackPane(),
                new StackPane(), new StackPane(), new StackPane()
        ));

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        setCenter(scrollPane);

        setAlignment(scrollPane, Pos.CENTER);

        content.setAlignment(Pos.CENTER);
        content.setFillWidth(true);
        content.setSpacing(20);

        getStyleClass().add("settings-bg-color");
        scrollPane.getStyleClass().addAll("settings-bg-color");
        content.getStyleClass().addAll("settings-bg-color");

        goBackButton.setFitHeight(60);
        goBackButton.setFitWidth(60);
        goBackButton.setTranslateX(12);
        goBackButton.setTranslateY(12);
        setAlignment(goBackButton, Pos.TOP_LEFT);
        setLeft(goBackButton);
        setMinWidth(300);

        VBox blockConfigs = new VBox();

        HBox configList = new HBox();
        ToggleGroup configsToggleGroup = new ToggleGroup();
        setMargin(configList, new Insets(10));
        Label selectConfigLabel = new Label("Choose a config:");
        selectConfigLabel.getStyleClass().add("settings-labels");

        for(int i = 0; i < configButtons.length; i++){
            configButtons[i] = new ToggleButton(String.valueOf(i + 1));
            configButtons[i].setToggleGroup(configsToggleGroup);
            configButtons[i].setMinWidth(50);
            configButtons[i].setMaxWidth(50);
            configList.getChildren().add(configButtons[i]);
        }
        configButtons[0].setSelected(true);

        configList.setAlignment(Pos.CENTER);
        blockConfigs.getChildren().addAll(selectConfigLabel, configList);
        blockConfigs.setAlignment(Pos.CENTER);

        Label currentConfigNameText = new Label("Your current config name: ");
        currentConfigNameText.getStyleClass().add("current-config-label");
        currentConfigName.getStyleClass().add("current-config-name");
        HBox fieldAndLabel = new HBox(currentConfigNameText, currentConfigName);
        fieldAndLabel.setAlignment(Pos.CENTER);

        HBox allButtons = new HBox(saveButton, defaultConfigNameButton);
        HBox.setMargin(saveButton, new Insets(10));
        allButtons.setAlignment(Pos.CENTER);

        VBox allNodes = new VBox(fieldAndLabel, textField, allButtons);
        allNodes.setAlignment(Pos.CENTER);
        allNodes.setFillWidth(true);
        VBox.setMargin(textField, new Insets(20));

        blockConfigs.getChildren().addAll(fieldAndLabel, allNodes);

        VBox blockScreenResizer = new VBox();
        blockScreenResizer.getChildren().addAll(sizesComboBox);
        VBox.setMargin(sizesComboBox, new Insets(20));
        setAlignment(blockScreenResizer, Pos.CENTER);
        blockScreenResizer.setAlignment(Pos.CENTER);

        VBox pieces = new VBox();

        Label playerLabel = new Label("Player");
        Label enemyLabel = new Label("Enemy");
        playerLabel.getStyleClass().add("settings-labels");
        enemyLabel.getStyleClass().add("settings-labels");
        VBox piecesLabels = new VBox(playerLabel, enemyLabel);
        piecesLabels.setSpacing(10);

        piecePlayerToCheckColor.setHeight(37.5);
        piecePlayerToCheckColor.setWidth(45);
        piecePlayerToCheckColor.setArcHeight(30);
        piecePlayerToCheckColor.setArcWidth(15);
        piecePlayerToCheckColor.setStrokeWidth(3);
        piecePlayerToCheckColor.setStroke(Color.BLACK);

        pieceEnemyToCheckColor.setHeight(37.5);
        pieceEnemyToCheckColor.setWidth(45);
        pieceEnemyToCheckColor.setArcHeight(30);
        pieceEnemyToCheckColor.setArcWidth(15);
        pieceEnemyToCheckColor.setStrokeWidth(3);
        pieceEnemyToCheckColor.setStroke(Color.BLACK);

        pieces.getChildren().addAll(piecePlayerToCheckColor, pieceEnemyToCheckColor);

        circleToCheckColor.setFill(Color.rgb(99, 32, 36));
        piecePlayerToCheckColor.setFill(Color.LIGHTBLUE);
        pieceEnemyToCheckColor.setFill(Color.POWDERBLUE);

        Rectangle rectPegToCheckColor = new Rectangle();
        rectPegToCheckColor.setFill(Color.rgb(213, 204, 171));
        rectPegToCheckColor.setWidth(28);
        rectPegToCheckColor.setHeight(90);
        rectPegToCheckColor.setArcHeight(20);
        rectPegToCheckColor.setArcWidth(20);

        circleToCheckColor.setRadius(75);

        stackPaneWindowForPiece.setPrefSize(150, 150);
        stackPaneWindowForPiece.setMaxSize(150, 150);

        pieces.setTranslateY(15);
        piecesLabels.setTranslateY(15);

        pieces.setAlignment(Pos.CENTER);
        piecesLabels.setAlignment(Pos.CENTER);

        stackPaneWindowForPiece.getChildren().addAll(circleToCheckColor, rectPegToCheckColor, pieces, piecesLabels);
        StackPane.setAlignment(pieces, Pos.CENTER);
        StackPane.setAlignment(piecesLabels, Pos.CENTER);
        stackPaneWindowForPiece.setAlignment(Pos.CENTER);

        HBox leftAndRightGroupsOfSlidersWithLabels = new HBox();
        VBox middleSlidersLabels = new VBox();

        leftSliderGroup.getChildren().addAll(redSliderPlayerPiece, greenSliderPlayerPiece, blueSliderPlayerPiece);
        rightSliderGroup.getChildren().addAll(redSliderEnemyPiece, greenSliderEnemyPiece, blueSliderEnemyPiece);

        VBox.setMargin(redSliderPlayerPiece, new Insets(2, 0, 0, 0));
        VBox.setMargin(greenSliderPlayerPiece, new Insets(2, 0, 0, 0));
        VBox.setMargin(blueSliderPlayerPiece, new Insets(3, 0, 0, 0));

        VBox.setMargin(redSliderEnemyPiece, new Insets(2, 0, 0, 0));
        VBox.setMargin(greenSliderEnemyPiece, new Insets(2, 0, 0, 0));
        VBox.setMargin(blueSliderEnemyPiece, new Insets(3, 0, 0, 0));

        leftSliderGroup.setMaxWidth(Double.MAX_VALUE);
        rightSliderGroup.setMaxWidth(Double.MAX_VALUE);
        leftSliderGroup.setPrefWidth(300);
        rightSliderGroup.setPrefWidth(300);

        middleSlidersLabels.setMaxWidth(Region.USE_PREF_SIZE);
        Label redLabel = new Label("Red");
        Label greenLabel = new Label("Green");
        Label blueLabel = new Label("Blue");
        redLabel.getStyleClass().add("settings-labels");
        greenLabel.getStyleClass().add("settings-labels");
        blueLabel.getStyleClass().add("settings-labels");

        middleSlidersLabels.getChildren().addAll(redLabel, greenLabel, blueLabel);
        middleSlidersLabels.setAlignment(Pos.CENTER);
        leftAndRightGroupsOfSlidersWithLabels.getChildren().addAll(leftSliderGroup, middleSlidersLabels, rightSliderGroup);
        HBox.setHgrow(leftSliderGroup, Priority.ALWAYS);
        HBox.setHgrow(middleSlidersLabels, Priority.NEVER);
        HBox.setHgrow(rightSliderGroup, Priority.ALWAYS);
        content.setPadding(new Insets(20));
        leftAndRightGroupsOfSlidersWithLabels.setSpacing(20);
        leftAndRightGroupsOfSlidersWithLabels.setAlignment(Pos.CENTER);

        VBox playGroundExampleAndAllSliders = new VBox();
        playGroundExampleAndAllSliders.setAlignment(Pos.CENTER);
        playGroundExampleAndAllSliders.getChildren().addAll(stackPaneWindowForPiece, leftAndRightGroupsOfSlidersWithLabels);

        VBox blockMusicVolume = new VBox();
        blockMusicVolume.setAlignment(Pos.CENTER);
        Label musicLabel = new Label("Music Volume");
        musicLabel.setTextFill(Color.WHITE);
        blockMusicVolume.getChildren().addAll(musicLabel, musicVolume);

        VBox blockSoundEffects = new VBox();
        blockSoundEffects.setAlignment(Pos.CENTER);
        Label soundEffectslabel = new Label("Sound Effects Volume");
        soundEffectslabel.setTextFill(Color.WHITE);
        blockSoundEffects.getChildren().addAll(soundEffectslabel, soundEffectsVolumeSlider);

        Region spacing = new Region();
        HBox slidersForVolume = new HBox();
        slidersForVolume.getChildren().addAll(blockMusicVolume, spacing, blockSoundEffects);
        HBox.setHgrow(blockMusicVolume, Priority.ALWAYS);
        HBox.setHgrow(spacing, Priority.ALWAYS);
        HBox.setHgrow(blockSoundEffects, Priority.ALWAYS);

        VBox checkBoxPane = new VBox();
        Label checkLabel = new Label("Use default colors");
        checkBoxPane.getChildren().addAll(defaultColorCheckBox, checkLabel);
        checkBoxPane.setAlignment(Pos.CENTER);
        VBox gradientBlock = new VBox();
        initializeSlider(gradientBlock, leftColorGradientsSliders, rightColorGradientsSliders, firstColorPanes, 0);
        initializeSlider(gradientBlock, leftColorGradientsSliders, rightColorGradientsSliders, secondColorPanes, 3);
        initializeSlider(gradientBlock, leftColorGradientsSliders, rightColorGradientsSliders, thirdColorPanes, 6);
        HBox gradientOrDefault = new HBox();

        VBox leftSamples = new VBox();
        VBox rightSamples = new VBox();


        leftFirstColorBorderPane.getStyleClass().add("left-first-sample");
        leftSecondColorBorderPane.getStyleClass().add("left-second-sample");
        leftThirdColorBorderPane.getStyleClass().add("left-third-sample");
        rightFirstColorBorderPane.getStyleClass().add("right-first-sample");
        rightSecondColorBorderPane.getStyleClass().add("right-second-sample");
        rightThirdColorBorderPane.getStyleClass().add("right-third-sample");

        initializeColorsSamples(leftFirstColorBorderPane, leftSamples);
        initializeColorsSamples(leftSecondColorBorderPane, leftSamples);
        initializeColorsSamples(leftThirdColorBorderPane, leftSamples);

        initializeColorsSamples(rightFirstColorBorderPane, rightSamples);
        initializeColorsSamples(rightSecondColorBorderPane, rightSamples);
        initializeColorsSamples(rightThirdColorBorderPane, rightSamples);

        gradientOrDefault.getChildren().addAll(leftSamples, gradientBlock, rightSamples, checkBoxPane);
        HBox bottomSettings = new HBox();
        bottomSettings.getChildren().addAll(gradientOrDefault, musicCheckBoxField);
        content.getChildren().addAll(blockConfigs, blockScreenResizer, slidersForVolume, playGroundExampleAndAllSliders, bottomSettings);
        scrollPane.setContent(content);
    }

    private void initializeColorsSamples(BorderPane borderPane, VBox vBox){
        borderPane.setMinSize(60, 60);
        borderPane.setPrefSize(60, 60);
        Border windowsBorder = new Border(
                new BorderStroke(
                        Color.BLACK,
                        BorderStrokeStyle.DASHED,
                        new CornerRadii(10),
                        new BorderWidths(2)
                )
        );
        borderPane.setBorder(windowsBorder);
        VBox.setMargin(borderPane, new Insets(10));
        vBox.getChildren().add(borderPane);
    }

    private void initializeSlider(VBox gradientBlock, ArrayList<Slider> allLeftSliders, ArrayList<Slider> allRightSliders, ArrayList<StackPane> allStackPane, int whichBlock){
        String className = switch(whichBlock / 3){
            case 0 -> "first-color-pane-gr-";
            case 1 -> "second-color-pane-gr-";
            case 2 -> "third-color-pane-gr-";
            default -> "X";
        };
        for(int i = whichBlock; i < whichBlock + 3; i++){
            String letter = switch(i % 3){
                case 0 -> "R";
                case 1 -> "G";
                case 2 -> "B";
                default -> "X";
            };

            HBox sliderLine = new HBox();
            Slider leftSlider = allLeftSliders.get(i);
            Slider rightSlider = allRightSliders.get(i);

            HBox.setMargin(rightSlider, new Insets(5));
            HBox.setMargin(leftSlider, new Insets(5));
            Label label = new Label(letter);
            label.getStyleClass().add("settings-labels");
            sliderLine.getChildren().addAll(label, leftSlider);

            HBox examples = new HBox();
            examples.setMinSize(350, 25);
            if(i == whichBlock + 1){
                int x = 1;
                for(StackPane pane : allStackPane){
                    String classToAdd = className + x;
                    x++;
                    HBox.setMargin(pane, new Insets(5));
                    pane.getStyleClass().add(classToAdd);
                    leftSlider.setMin(0);
                    leftSlider.setMax(255);
                    pane.setPrefSize(20, 20);
                    pane.setBackground(new Background(new BackgroundFill(Color.RED, new CornerRadii(5), Insets.EMPTY)));
                    examples.getChildren().add(pane);
                }
            }
            sliderLine.getChildren().addAll(examples, rightSlider);
            HBox.setMargin(sliderLine, new Insets(10));
            gradientBlock.getChildren().add(sliderLine);
        }
    }

    Slider getMusicVolume(){
        return musicVolume;
    }

    Label getCurrentConfigName(){
        return currentConfigName;
    }

    Button getDefaultConfigNameButton(){
        return defaultConfigNameButton;
    }

    Button getSaveButton(){
        return saveButton;
    }

    ComboBox getSizesComboBox(){
        return sizesComboBox;
    }

    TextField getTextField(){
        return textField;
    }

    ImageView getGoBackButton(){
        return goBackButton;
    }

    ToggleButton[] getConfigButtons(){
        return configButtons;
    }

    Slider getBlueSliderEnemyPiece(){
        return blueSliderEnemyPiece;
    }

    Slider getBlueSliderPlayerPiece(){
        return blueSliderPlayerPiece;
    }

    Slider getGreenSliderEnemyPiece(){
        return greenSliderEnemyPiece;
    }

    Slider getGreenSliderPlayerPiece(){
        return greenSliderPlayerPiece;
    }

    Rectangle getPieceEnemyToCheckColor(){
        return pieceEnemyToCheckColor;
    }

    Rectangle getPiecePlayerToCheckColor(){
        return piecePlayerToCheckColor;
    }

    Slider getRedSliderEnemyPiece(){
        return redSliderEnemyPiece;
    }

    Slider getRedSliderPlayerPiece(){
        return redSliderPlayerPiece;
    }

    Slider getSoundEffectsVolumeSlider(){
        return soundEffectsVolumeSlider;
    }

    ArrayList<Slider> getLeftColorGradientsSliders(){
        return leftColorGradientsSliders;
    }

    CheckBox getDefaultColorCheckBox(){
        return defaultColorCheckBox;
    }

    Slider getLeftFirstColorSlider1(){
        return leftFirstColorSlider1;
    }

    Slider getLeftFirstColorSlider2(){
        return leftFirstColorSlider2;
    }

    Slider getLeftFirstColorSlider3(){
        return leftFirstColorSlider3;
    }

    Slider getLeftSecondColorSlider1(){
        return leftSecondColorSlider1;
    }

    Slider getLeftSecondColorSlider2(){
        return leftSecondColorSlider2;
    }

    Slider getLeftSecondColorSlider3(){
        return leftSecondColorSlider3;
    }

    Slider getLeftThirdColorSlider1(){
        return leftThirdColorSlider1;
    }

    Slider getLeftThirdColorSlider2(){
        return leftThirdColorSlider2;
    }

    Slider getLeftThirdColorSlider3(){
        return leftThirdColorSlider3;
    }

    Slider getRightFirstColorSlider1(){
        return rightFirstColorSlider1;
    }

    Slider getRightFirstColorSlider2(){
        return rightFirstColorSlider2;
    }

    Slider getRightFirstColorSlider3(){
        return rightFirstColorSlider3;
    }

    Slider getRightSecondColorSlider1(){
        return rightSecondColorSlider1;
    }

    Slider getRightSecondColorSlider2(){
        return rightSecondColorSlider2;
    }

    Slider getRightSecondColorSlider3(){
        return rightSecondColorSlider3;
    }

    Slider getRightThirdColorSlider1(){
        return rightThirdColorSlider1;
    }

    Slider getRightThirdColorSlider2(){
        return rightThirdColorSlider2;
    }

    Slider getRightThirdColorSlider3(){
        return rightThirdColorSlider3;
    }

    ArrayList<Slider> getRightColorGradientsSliders(){
        return rightColorGradientsSliders;
    }

    VBox getMusicCheckBoxField(){
        return musicCheckBoxField;
    }
}