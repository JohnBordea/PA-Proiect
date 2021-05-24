package ro.uaic.info.view;

import javafx.stage.FileChooser;
import ro.uaic.info.view.MainFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel {
    final MainFrame frame;

    JButton loadBtn = new JButton("Load");
    JButton saveBtn = new JButton("Save");
    JButton resetBtn = new JButton("Reset");
    JButton exitBtn = new JButton("Exit");
    FileChooser fileChooser;

    public ControlPanel(MainFrame frame) {
        this.frame = frame;
        initialization();
    }

    private void initialization() {
        setLayout(new GridLayout(1, 4));
        loadBtn.addActionListener(this::load);
        saveBtn.addActionListener(this::save);
        resetBtn.addActionListener(this::reset);
        exitBtn.addActionListener(this::exit);

        add(loadBtn);
        add(saveBtn);
        add(resetBtn);
        add(exitBtn);
    }

    private void load(ActionEvent e) {
        try {
            BufferedImage png = ImageIO.read(new File("./test.png"));
            frame.canvas.graphics.setColor(Color.WHITE);
            frame.canvas.graphics.fillRect(0, 0, frame.canvas.getW(), frame.canvas.getH());
            frame.canvas.graphics.drawImage(png, 0, 0, null);
            frame.canvas.repaint();
        } catch (Exception ex) {
            System.err.println(ex);
        }
    }

    private void save(ActionEvent e) {
        try {
            ImageIO.write(frame.canvas.image, "PNG", new File("./test.png"));
        } catch (IOException ex) {
            System.err.println(ex);
        }
    }

    private void reset(ActionEvent e) {
        frame.canvas.graphics.setColor(Color.WHITE);
        frame.canvas.graphics.fillRect(0, 0, frame.canvas.getW(), frame.canvas.getH());
        frame.canvas.createdShapes.clear();
        frame.canvas.repaint();
    }

    private void exit(ActionEvent e) {
        System.exit(0);
    }
}
