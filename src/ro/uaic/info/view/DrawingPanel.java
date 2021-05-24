package ro.uaic.info.view;

import ro.uaic.info.CreatedShape;
import ro.uaic.info.NodeShape;
import ro.uaic.info.RegularPolygon;
import ro.uaic.info.entity.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.LinkedList;

public class DrawingPanel extends JPanel {
    final MainFrame frame;
    final static int W = 900, H = 600;

    BufferedImage image;
    BufferedImage oldImage;
    Graphics2D graphics;

    //Used for deleting shapes
    LinkedList<Object> createdShapes = new LinkedList<>();

    boolean deletedObject;

    Object movedObject;

    //Used for Segment
    ro.uaic.info.entity.Point pointStart, pointEnd;

    public DrawingPanel(MainFrame frame) {
        this.frame = frame;
        createOffscreenImage();
        initialization();
    }

    private void createOffscreenImage() {
        image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
        graphics.setColor(Color.WHITE); //fill the image with white
        graphics.fillRect(0, 0, W, H);
    }

    private void initialization() {
        setPreferredSize(new Dimension(W, H));
        setBorder(BorderFactory.createEtchedBorder());

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (frame.configPanel.getModeMode()) {
                    //Deplasare
                    case 0 -> {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            for (int i = 0; i < createdShapes.size(); i++) {
                                if (createdShapes.get(i) instanceof ro.uaic.info.entity.Point)
                                    if (getAppropriateDistance(e.getX(), e.getY(), createdShapes.get(i))) {
                                        eraseObject(createdShapes.get(i));
                                        movedObject = createdShapes.get(i);
                                        ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) movedObject;
                                        for (Object o : point.constrains)
                                            eraseObject(o);
                                        i = createdShapes.size();
                                    }
                            }
                            oldImage = deepCopy(image);
                            //Redraw Other Components
                            for (Object i : createdShapes) {
                                redrawObject(i);
                            }
                            repaint();
                        }
                    }
                    //Punct
                    case 1 -> {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            oldImage = deepCopy(image);
                            drawPoint(e.getX(), e.getY(), new Color(102, 102, 255), new Color(51, 51, 255));
                            repaint();
                        }
                    }
                    case 2 -> {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            pointStart = null;
                            //Find The Nearest Point
                            for (int i = createdShapes.size() - 1; i >= 0 && !createdShapes.isEmpty(); i--) {
                                if (createdShapes.get(i) instanceof ro.uaic.info.entity.Point &&
                                        getAppropriateDistance(e.getX(), e.getY(), createdShapes.get(i))) {
                                    pointStart = (ro.uaic.info.entity.Point) createdShapes.get(i);
                                    i = -1;
                                }
                            }
                            if (pointStart == null) {
                                pointStart = new ro.uaic.info.entity.Point(e.getX(), e.getY());
                                drawPoint(e.getX(), e.getY(), new Color(51, 51, 255), new Color(0, 0, 230));
                            }

                            oldImage = deepCopy(image);

                            repaint();
                        }
                    }
                    //Sterge
                    case 5 -> {
                        deletedObject = false;
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            //Find The Element That Needs To Be Deleted(Point)
                            for (int i = 0; i < createdShapes.size(); i++) {
                                if (createdShapes.get(i) instanceof ro.uaic.info.entity.Point)
                                    if (getAppropriateDistance(e.getX(), e.getY(), createdShapes.get(i))) {
                                        deleteObject(createdShapes.get(i));
                                        i--;
                                        deletedObject = true;
                                    }
                            }

                            if (deletedObject == false)
                                for (int i = createdShapes.size() - 1; i >= 0 && !createdShapes.isEmpty(); i--) {
                                    if (createdShapes.get(i) instanceof Segment)
                                        if (getAppropriateDistance(e.getX(), e.getY(), createdShapes.get(i))) {
                                            deleteObject(createdShapes.get(i));
                                            i = -1;
                                            deletedObject = true;
                                        }
                                }

                            //Redraw Other Components
                            if (deletedObject)
                                for (Object i : createdShapes) {
                                    redrawObject(i);
                                }
                            repaint();
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                switch (frame.configPanel.getModeMode()) {
                    case 0 -> {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (movedObject instanceof ro.uaic.info.entity.Point) {
                                drawPoint(e.getX(), e.getY(), new Color(51, 51, 255), new Color(0, 0, 230));
                                ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) movedObject;
                                point.setX(e.getX());
                                point.setY(e.getY());
                                for (Object i : createdShapes) {
                                    redrawObject(i);
                                }
                                repaint();
                            }
                        }
                    }
                    case 1 -> {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            graphics.drawImage(oldImage, 0, 0, null);
                            drawPoint(e.getX(), e.getY(), new Color(51, 51, 255), new Color(0, 0, 230));
                            createdShapes.add(new ro.uaic.info.entity.Point(e.getX(), e.getY()));
                            repaint();
                        }
                    }
                    case 2 -> {
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            graphics.drawImage(oldImage, 0, 0, null);

                            pointEnd = null;

                            //Find The Nearest Point
                            for (int i = createdShapes.size() - 1; i >= 0 && !createdShapes.isEmpty(); i--) {
                                if (createdShapes.get(i) instanceof ro.uaic.info.entity.Point &&
                                        getAppropriateDistance(e.getX(), e.getY(), createdShapes.get(i))) {
                                    pointEnd = (ro.uaic.info.entity.Point) createdShapes.get(i);
                                    i = -1;
                                }
                            }
                            if (pointEnd == null) {
                                pointEnd = new ro.uaic.info.entity.Point(e.getX(), e.getY());
                                drawPoint(e.getX(), e.getY(), new Color(51, 51, 255), new Color(0, 0, 230));
                            }

                            drawLine(pointStart.getX(), pointStart.getY(), pointEnd.getX(), pointEnd.getY(), new Color(115, 115, 115));
                            Segment segment = new Segment(pointStart, pointEnd);
                            pointStart.constrains.add(segment);
                            pointEnd.constrains.add(segment);

                            createdShapes.add(segment);
                            if (!createdShapes.contains(pointStart))
                                createdShapes.add(pointStart);
                            createdShapes.add(pointEnd);
                            repaint();
                        }
                    }
                }
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (frame.configPanel.getModeMode()) {
                    case 0 -> {
                        if (movedObject instanceof ro.uaic.info.entity.Point) {
                            graphics.drawImage(oldImage, 0, 0, null);
                            drawPoint(e.getX(), e.getY(), new Color(102, 102, 255), new Color(51, 51, 255));
                            ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) movedObject;
                            point.setX(e.getX());
                            point.setY(e.getY());

                            for (Object i : createdShapes) {
                                redrawObject(i);
                            }
                            repaint();
                        }
                    }
                    case 1 -> {
                        graphics.drawImage(oldImage, 0, 0, null);
                        drawPoint(e.getX(), e.getY(), new Color(102, 102, 255), new Color(51, 51, 255));
                        repaint();
                    }
                    case 2 -> {
                        graphics.drawImage(oldImage, 0, 0, null);
                        drawPoint(e.getX(), e.getY(), new Color(102, 102, 255), new Color(51, 51, 255));
                        drawLine(pointStart.getX(), pointStart.getY(), e.getX(), e.getY(), new Color(153, 153, 153));
                        repaint();
                    }
                }
            }
        });
    }

    public Boolean getAppropriateDistance(int x, int y, Object object) {
        if (object instanceof Segment) {
            Segment segment = (Segment) object;
            int x1, x2, y1, y2;
            x1 = Math.min(segment.getX1().getX(), segment.getX2().getX());
            x2 = Math.max(segment.getX1().getX(), segment.getX2().getX());
            y1 = Math.min(segment.getX1().getY(), segment.getX2().getY());
            y2 = Math.max(segment.getX1().getY(), segment.getX2().getY());
            if ((x < x1 - 20 || x > x2 + 20) || (y < y1 - 20 || y > y2 + 20))
                return false;
            if (x1 == x2)
                if (Math.abs(x - x1) <= 10)
                    return true;
            if (y1 == y2)
                if (Math.abs(y - y1) <= 10)
                    return true;
            /// |ax_0 + y_0 + c|/sqrt(a^2+1)
            /// a = ( segment.getX2().getY() - segment.getX1().getY() )/( segment.getX2().getX() - segment.getX1().getX() )
            /// c = segment.getX1().getY() - segment.getX1().getX()*( segment.getX2().getY() - segment.getX1().getY() )/( segment.getX2().getX() - segment.getX1().getX() )
            if (Math.abs((segment.getX2().getY() - segment.getX1().getY()) / (segment.getX2().getX() -
                    segment.getX1().getX()) * x - y + segment.getX1().getY() - segment.getX1().getX() *
                    (segment.getX2().getY() - segment.getX1().getY()) / (segment.getX2().getX() -
                    segment.getX1().getX())) / Math.sqrt((double) (segment.getX2().getY() - segment.getX1().getY()) /
                    (segment.getX2().getX() - segment.getX1().getX()) * (segment.getX2().getY() - segment.getX1().getY())
                    / (segment.getX2().getX() - segment.getX1().getX()) + 1) <= 10)
                return true;
        }
        if (object instanceof ro.uaic.info.entity.Point) {
            ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) object;
            return Math.sqrt((x - point.getX()) * (x - point.getX()) + (y - point.getY()) * (y - point.getY())) <= 15;
        }
        return false;
    }

    private void drawPoint(int x, int y, Color colorInterior, Color colorExterior) {
        graphics.setColor(colorExterior);
        graphics.fill(new NodeShape(x, y, 15));
        graphics.setColor(colorInterior);
        graphics.fill(new NodeShape(x, y, 10));
    }

    private void drawLine(int x1, int y1, int x2, int y2, Color color) {
        graphics.setColor(color);
        graphics.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        graphics.drawLine(x1, y1, x2, y2);
    }

    private void eraseObject(Object object) {
        graphics.setColor(Color.WHITE);
        if (object instanceof ro.uaic.info.entity.Point) {
            ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) object;
            graphics.fill(new NodeShape(point.getX(), point.getY(), 15));
        }
        if (object instanceof Segment) {
            Segment segment = (Segment) object;
            drawLine(segment.getX1().getX(), segment.getX1().getY(), segment.getX2().getX(), segment.getX2().getY(), Color.WHITE);
        }
    }

    private void redrawObject(Object object) {
        if (object instanceof ro.uaic.info.entity.Point) {
            ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) object;
            drawPoint(point.getX(), point.getY(), new Color(51, 51, 255), new Color(0, 0, 230));
        }
        if (object instanceof Segment) {
            Segment segment = (Segment) object;
            drawLine(segment.getX1().getX(), segment.getX1().getY(), segment.getX2().getX(), segment.getX2().getY(), new Color(115, 115, 115));
        }
    }

    private void deleteObject(Object object) {
        if (object instanceof ro.uaic.info.entity.Point) {
            ro.uaic.info.entity.Point point = (ro.uaic.info.entity.Point) object;
            for (int i = 0; i < point.constrains.size(); i++) {
                deleteObject(point.constrains.get(i));
                i--;
                deletedObject = true;
            }
            eraseObject(object);
            createdShapes.remove(object);
        } else if (object instanceof Segment) {
            Segment segment = (Segment) object;
            segment.getX1().constrains.remove(segment);
            segment.getX2().constrains.remove(segment);
            eraseObject(segment);
            createdShapes.remove(segment);
        }
    }

    @Override
    public void update(Graphics g) {
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, this);
    }

    public int getW() {
        return W;
    }

    public int getH() {
        return H;
    }

    static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
