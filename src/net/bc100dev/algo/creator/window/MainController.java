package net.bc100dev.algo.creator.window;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;

public class MainController {

    public static String COMM_STR;

    @FXML
    private Canvas canvas;
    private GraphicsContext graphicsContext;
    private Affine transform = new Affine();

    private double canvasTranslateX = 0;
    private double canvasTranslateY = 0;
    private double scaleFactor = 1.0;

    private boolean threadStop = false;

    public void initialize() {
        graphicsContext = canvas.getGraphicsContext2D();

        new Thread(() -> {}).start();

        new Thread(() -> {
            while (true) {
                if (COMM_STR != null) {
                    switch (COMM_STR) {
                        case "shown" -> {
                            Platform.runLater(() -> {
                                Scene scene = canvas.getScene();
                                if (scene != null) {
                                    canvas.widthProperty().bind(scene.widthProperty());
                                    canvas.heightProperty().bind(scene.heightProperty());

                                    graphicsContext.setFill(Color.BLACK);
                                    graphicsContext.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

                                    updateCanvasTransform();
                                }
                            });
                        }
                        case "closing" -> {
                            canvas.widthProperty().unbind();
                            canvas.heightProperty().unbind();

                            threadStop = true;
                        }
                    }

                    COMM_STR = null;

                    if (threadStop)
                        break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignore) {
                    }
                }
            }
        }).start();

        canvas.sceneProperty().addListener((observableValue, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.widthProperty().addListener((observableValue1, number, t1) -> {
                    canvas.widthProperty().bind(newScene.widthProperty());

                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(0, 0, t1.doubleValue(), canvas.getHeight());
                });

                newScene.heightProperty().addListener((observableValue1, number, t1) -> {
                    canvas.heightProperty().bind(newScene.heightProperty());

                    graphicsContext.setFill(Color.BLACK);
                    graphicsContext.fillRect(0, 0, canvas.getWidth(), t1.doubleValue());
                });

                updateCanvasTransform();
            } else {
                canvas.widthProperty().unbind();
                canvas.heightProperty().unbind();
                canvas.setOnMouseDragged(null);
                canvas.setOnScroll(null);
            }
        });

        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnScroll(this::handleScroll);
    }

    private void updateCanvasTransform() {
        graphicsContext.setTransform(transform);
    }

    private void handleMouseDragged(MouseEvent event) {
        double deltaX = event.getX() - canvasTranslateX;
        double deltaY = event.getY() - canvasTranslateY;
        canvasTranslateX = event.getX();
        canvasTranslateY = event.getY();

        transform.appendTranslation(deltaX, deltaY);
        updateCanvasTransform();
    }

    private void handleScroll(ScrollEvent event) {
        double delta = event.getDeltaY() > 0 ? 1.1 : 0.9;
        scaleFactor *= delta;

        transform.appendScale(delta, delta, event.getX(), event.getY());
        updateCanvasTransform();
    }

    @FXML
    private void onCanvasDragOver() {
        // Handle canvas drag over event if needed
    }

}
