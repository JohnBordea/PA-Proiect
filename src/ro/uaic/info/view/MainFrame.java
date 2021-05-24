package ro.uaic.info.view;

import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    ConfigPanel configPanel;
    ControlPanel controlPanel;
    DrawingPanel canvas;
    Stage S;

    public MainFrame() {
        super("GeoGebra by Ion Bordea");
        initialization();
    }

    public MainFrame(Stage s) {
        super("GeoGebra by Ion Bordea");
        S = s;
        initialization();
    }

    private void initialization() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        canvas = new DrawingPanel(this);
        configPanel = new ConfigPanel(this);
        controlPanel = new ControlPanel(this);

        add(canvas, BorderLayout.CENTER);
        add(configPanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
    }
}
