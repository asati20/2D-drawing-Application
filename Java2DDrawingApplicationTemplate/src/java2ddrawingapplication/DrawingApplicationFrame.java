/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author nishant asati
 */
public class DrawingApplicationFrame extends JFrame
{

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
   
    private final JPanel line1 = new JPanel();
    private final JPanel line2 = new JPanel();
    private final JPanel all = new JPanel();
    private final JButton Clear, Undo; 
    private final JComboBox<String> shapes;
    private static final String[] shape = {"rectangle", "oval", "line"};
    private final JCheckBox Gradient, Filled, Dashed;
    private final JButton Foreground_COLOR, Background_COLOR;	
    private final JTextField LineWidth, DashLength;
    private final JLabel Width_label, Dash_label,shapeslst;
    private Color Color_one= Color.BLACK;
    private Color Color_two= Color.WHITE;
    private Paint paint;

    // Variables for drawPanel.
    private ArrayList<MyShapes> DrawnitemsList = new ArrayList<MyShapes>();
    private DrawingApplicationFrame.DrawPanel Dp;
    private Point pos;
    // add status label
    private final JLabel mouse_curr_poss;
    // Constructor for DrawingApplicationFrame
    
    
    
    
    public DrawingApplicationFrame()
    {
        super("JAVA 2D Drawings");
        // add widgets to panels
    Dp = new DrawPanel();
    all.setLayout(new GridLayout(2,1));
    shapes = new JComboBox<String>(shape);
    Clear = new JButton("Clear");
    Undo = new JButton("Undo");
    Clear.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
                if (Clear == event.getSource())
                {
                DrawnitemsList.clear();
                repaint();
                }}});
    shapeslst = new JLabel("Shapes");
    
    Undo.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
                if (DrawnitemsList.size() != 0) 
                {
                DrawnitemsList.remove(DrawnitemsList.size() - 1);
                repaint();
                }}});
    
    
    line1.add(Undo);
    line1.add(Clear);
    line1.add(shapeslst);
    line1.add(shapes);
    Foreground_COLOR = new JButton("1st Color");// button with text on it.
     // default clor set to black.
    Foreground_COLOR.addActionListener(new ActionListener()
            {@Override
            public void actionPerformed(ActionEvent event)
            {
            Color_one = JColorChooser.showDialog(DrawingApplicationFrame.this, "Choose color 1", Color_one);
            }});
    Background_COLOR = new JButton("2nd Color");
    
    Background_COLOR.addActionListener(new ActionListener()
            {@Override
            public void actionPerformed(ActionEvent event)
            {
            Color_two= JColorChooser.showDialog(DrawingApplicationFrame.this, "Choose color 2",Color.WHITE);
            }});
    
    Filled= new JCheckBox("Filled");
    line1.add(Filled);
    //line1.setLayout(new BorderLayout());
    //line2.setLayout(new BorderLayout());
    //all.setLayout(new BorderLayout());
    mouse_curr_poss= new JLabel("(0,0)");// can add mouse_curr_poss.setToolTipText("mouse position");
        // firstLine widgets
    Gradient= new JCheckBox("Use Gradient");
    line2.add(Gradient);
    line2.add(Foreground_COLOR);
    line2.add(Background_COLOR);
    Dashed= new JCheckBox("Dashed");
    LineWidth= new JTextField("10");
    DashLength = new JTextField("15");
    Width_label = new JLabel("Line Width");
    Dash_label = new JLabel("Dash Length");
    line2.add(Dashed);
    line2.add(Width_label);
    line2.add(LineWidth);
    line2.add(Dash_label);
    line2.add(DashLength);
         // secondLine widgets

        // add top panel of two panels

        // add topPanel to North, drawPanel to Center, and statusLabel to South
    all.add(line1);
    all.add(line2);
    super.add(all,BorderLayout.NORTH);
    super.add(Dp,BorderLayout.CENTER);
    super.add(mouse_curr_poss,BorderLayout.SOUTH);
        //add listeners and event handlers
    }

    // Create event handlers, if needed

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
            
        this.setBackground(Color.WHITE);
        addMouseListener(new MouseHandler());
        addMouseMotionListener(new MouseHandler());
           
        }
        @Override
        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            //loop through and draw each shape in the shapes arraylist
            for(MyShapes x: DrawnitemsList) 
            {
            x.draw(g2d);
            }
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
            paint = Color_one;
            
            int line_Width = Integer.parseInt(LineWidth.getText());
                        
            Stroke mouse_stroke = new BasicStroke(line_Width, BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND);
                     
            float[] dash_Length = {Float.parseFloat(DashLength.getText())};

            if(Gradient.isSelected()) 
            {
            paint = new GradientPaint(0, 0, Color_one, 50, 50,Color_two, true);
            } 
            else 
            {
            paint = Color_one;
            }
            
            
            if(Dashed.isSelected()) 
            {
            mouse_stroke = new BasicStroke(line_Width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dash_Length, 0);
            } 
            else
            {
            mouse_stroke = new BasicStroke(line_Width,BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
            }
            
            
            if((String) shapes.getSelectedItem() == "line")
            {
            MyLine line = new MyLine(event.getPoint(), event.getPoint(), paint, mouse_stroke);
            DrawnitemsList.add(line);
            }
            else if((String) shapes.getSelectedItem() == "rectangle") 
            {
            MyRectangle rectangle = new MyRectangle(event.getPoint(), event.getPoint(),paint, mouse_stroke, Filled.isSelected());
            DrawnitemsList.add(rectangle);
            }

            
            else if((String) shapes.getSelectedItem() == "oval") 
            {
            MyOval oval = new MyOval(event.getPoint(), event.getPoint(), paint, mouse_stroke,
            Filled.isSelected());
            DrawnitemsList.add(oval);
            }
            
            
            repaint();
            }

            public void mouseReleased(MouseEvent event)
            {
            String mouse_position = "(" + event.getPoint().x +","+ event.getPoint().y +")";
            mouse_curr_poss.setText(mouse_position);
            repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
            Point XY = new Point(event.getX(), event.getY());
            DrawnitemsList.get(DrawnitemsList.size()-1).setEndPoint(XY);
            mouse_curr_poss.setText(String.format("(%d,%d)", event.getX(), event.getY()));
            repaint();
            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
            mouse_curr_poss.setText(String.format("(%d,%d)", event.getX(), event.getY()));
            }
        }

    }
}
