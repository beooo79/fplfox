package com.github.beooo79;

import com.github.beooo79.plan.*;
import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstants;
import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Log
public class FoxFrame extends JFrame {

    private static final Font FONT2 = new Font("Monospaced", Font.BOLD, 14);
    private static final Font FONT3 = new Font("Monospaced", Font.PLAIN, 14);
    private static final String CLR = "CLR";
    private static final Color FPL_WORKING_AREA_COLOR = Color.YELLOW;

    @Serial
    private static final long serialVersionUID = -4885303017216749279L;

    private JTextArea textInfo;

    private Airways aw;
    private Airports ap;
    private Fixes fixDB;
    private final Plan[] plans = {null, null, null, null, null};

    private JTextArea[] textFPL;
    private JButton buttonRead;
    private JButton buttonExportFlightplans;
    private JButton buttonShowMap;

    private MapPanel mapPanel;
    private JTabbedPane viewPanel;

    public void sendPostRequest(String ADEP, String ADES) {

        // Build parameter string
        String data = "id1=" + ADEP + "&id2=" + ADES + "&minalt=FL200&maxalt=FL400&lvl=B&usesid=Y&usestar=Y&rnav=Y&dbid=1206";
        try {

            // Send the request
            URL url = new URL("http://rfinder.asalink.net/free/autoroute_rtx.php");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            // write parameters
            writer.write(data);
            writer.flush();

            // Get the response
            StringBuilder answer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                answer.append(line);
            }
            writer.close();
            reader.close();

            try {
                String route = answer.substring(answer.toString().indexOf("<hr><tt><b>" + ADEP), answer.toString().indexOf(ADES + "</b></tt><hr>")).replaceAll("<.*?>", "");
                textFPL[4].setText(route + " " + ADES);
            } catch (Exception e) {
                out(answer.toString());
            }

        } catch (Exception ex) {
            log.severe("sendPostRequest to rfinder.asalink.net failed: " + ex.getMessage());
        }
    }

    public FoxFrame() {
        initUI();
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                init();
            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowClosing(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void windowIconified(WindowEvent e) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void initUI() {
        double[][] size = {{TableLayoutConstants.PREFERRED, TableLayoutConstants.FILL, TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED}, {TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED, TableLayoutConstants.PREFERRED}};
        JPanel fplPanel = new JPanel(new TableLayout(size));
        JPanel cmdPanel = new JPanel(new FlowLayout());
        viewPanel = new JTabbedPane();
        JPanel logPanel = new JPanel(new BorderLayout(5, 5));
        mapPanel = new MapPanel(plans, aw, ap, fixDB);
        viewPanel.add("Log", logPanel);
        viewPanel.add("Flightplan Map View... click to center, + or - to zoom", mapPanel);

        // Info Area
        textInfo = new JTextArea(FoxMain.VERSION);
        textInfo.setFont(FONT2);
        JScrollPane textScroll = new JScrollPane(textInfo);
        JButton buttonClear = new JButton("Clear Log Area");
        logPanel.add(textScroll, BorderLayout.CENTER);
        logPanel.add(buttonClear, BorderLayout.SOUTH);

        // FPL Area
        textFPL = new JTextArea[]{new JTextArea(1, 80), new JTextArea(1, 80), new JTextArea(1, 80), new JTextArea(1, 80), new JTextArea(1, 80)};
        for (JTextArea area : textFPL) {
            area.setFont(FONT3);
        }

        JButton[] cmdFPL = new JButton[]{new FPLActionButton(textFPL[0]), 
            new FPLActionButton(textFPL[1]), new FPLActionButton(textFPL[2]), new FPLActionButton(textFPL[3]), new FPLActionButton(textFPL[4])};

        // fplScroll = new JScrollPane(textFPL);
        fplPanel.add(new JLabel("Input Routes - Format: <ADEP>   [SID|DCT]   <FPL ROUTE>   [STAR|DCT]   <ADES>"), "0, 0, 1, 0");

        fplPanel.add(new EmptyColorLabel(Color.RED), "0,1");
        fplPanel.add(new JScrollPane(textFPL[0]), "1,1,f,f");
        fplPanel.add(cmdFPL[0], "2,1");
        fplPanel.add(new JButton(new AbstractAction(CLR) {

            @Override
            public void actionPerformed(ActionEvent e) {
                textFPL[0].setText("");
            }
        }), "3,1");

        fplPanel.add(new EmptyColorLabel(Color.BLUE), "0,2");
        fplPanel.add(new JScrollPane(textFPL[1]), "1,2,f,f");
        fplPanel.add(cmdFPL[1], "2,2");
        fplPanel.add(new JButton(new AbstractAction(CLR) {

            @Override
            public void actionPerformed(ActionEvent e) {
                textFPL[1].setText("");
            }
        }), "3,2");

        fplPanel.add(new EmptyColorLabel(Color.BLACK), "0,3");
        fplPanel.add(new JScrollPane(textFPL[2]), "1,3,f,f");
        fplPanel.add(cmdFPL[2], "2,3");
        fplPanel.add(new JButton(new AbstractAction(CLR) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFPL[2].setText("");
            }
        }), "3,3");

        fplPanel.add(new EmptyColorLabel(Plan.SLATE_GRAY), "0,4");
        fplPanel.add(new JScrollPane(textFPL[3]), "1,4,f,f");
        fplPanel.add(cmdFPL[3], "2,4");
        fplPanel.add(new JButton(new AbstractAction(CLR) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFPL[3].setText("");
            }
        }), "3,4");

        fplPanel.add(new EmptyColorLabel(Plan.DARK_GREEN), "0,5");
        fplPanel.add(new JScrollPane(textFPL[4]), "1,5,f,f");
        fplPanel.add(cmdFPL[4], "2,5");
        fplPanel.add(new JButton(new AbstractAction(CLR) {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFPL[4].setText("");
            }
        }), "3,5");

        // Command Area

        JButton buttonFixInformation = new JButton("Fix Details");
        JButton buttonAutoRoute = new JButton("Auto Routing (Experimental)");
        JButton buttonRoutefinder = new JButton("RouteFinder Routing");
        buttonFixInformation.setBackground(FPL_WORKING_AREA_COLOR);
        buttonFixInformation.setOpaque(true);
        buttonAutoRoute.setBackground(FPL_WORKING_AREA_COLOR);
        buttonAutoRoute.setOpaque(true);
        buttonRoutefinder.setBackground(FPL_WORKING_AREA_COLOR);
        buttonRoutefinder.setOpaque(true);
        textFPL[4].setOpaque(true);
        textFPL[4].setBackground(FPL_WORKING_AREA_COLOR);

        buttonRead = new JButton("1...Process Flightplans");
        buttonShowMap = new JButton("2...Show Map");
        buttonExportFlightplans = new JButton("3...Export FS FPL, PMDG Files");
        JButton buttonProperties = new JButton("Properties");

        buttonClear.addActionListener(e -> textInfo.setText(""));
        buttonRead.addActionListener(e -> {
            outLine();
            actionRead();
            for (Plan plan : plans) {
                if (plan != null) {
                    outLine();
                    plan.walk();
                    outLine();
                }
            }
            outLine();
        });
        buttonExportFlightplans.addActionListener(e -> actionExportPlans());
        buttonShowMap.addActionListener(e -> actionGoogleMaps());
        buttonProperties.addActionListener(e -> actionProperties());
        buttonRoutefinder.addActionListener(arg0 -> {
            textFPL[4].setText(textFPL[4].getText().toUpperCase().trim());
            String[] fromto = textFPL[4].getText().split(" ");
            if (fromto.length == 2) {
                sendPostRequest(fromto[0], fromto[1]);
            } else {
                handleErrorOnAutoRouteText("ADEP ADES", 8);
            }
        });
        buttonAutoRoute.addActionListener(arg0 -> {
            textFPL[4].setText(textFPL[4].getText().toUpperCase().trim());
            String[] fromto = textFPL[4].getText().split(" ");
            if (fromto.length == 2) {
                autoRoute(fromto[0], fromto[1]);
            } else {
                handleErrorOnAutoRouteText("ADEP ADES", 8);
            }
        });
        buttonFixInformation.addActionListener(arg0 -> {
            textFPL[4].setText(textFPL[4].getText().toUpperCase().trim());
            String fix = textFPL[4].getText().trim();
            if (fix.split(" ").length == 1) {
                fixInfo(fix);
            } else {
                handleErrorOnAutoRouteText("SINGLEFIX", 2);
            }
        });
        buttonShowMap.setEnabled(false);
        buttonExportFlightplans.setEnabled(false);
        buttonRead.setEnabled(false);

        cmdPanel.add(buttonFixInformation);
        cmdPanel.add(buttonAutoRoute);
        cmdPanel.add(buttonRoutefinder);
        cmdPanel.add(buttonRead);
        cmdPanel.add(buttonShowMap);
        cmdPanel.add(buttonExportFlightplans);
        cmdPanel.add(new JLabel("                  "));
        cmdPanel.add(buttonProperties);

        fplPanel.add(cmdPanel, "0,6,2,6");

        this.setTitle(FoxMain.VERSION);

        // ///////////////////////////////////////////////////////////////
        fplPanel.setBorder(new EmptyBorder(20, 10, 0, 10));
        viewPanel.setBorder(new EmptyBorder(20, 10, 10, 10));
        setLayout(new BorderLayout(5, 5));
        add(fplPanel, BorderLayout.NORTH);
        add(viewPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        viewPanel.setPreferredSize(new Dimension(400, 400));
        pack();
        setLocationByPlatform(true);

        setVisible(true);

    }

    protected void fixInfo(String fix) {
        ArrayList<Fix> l = fixDB.get(fix.trim());
        if (l == null || l.isEmpty()) {
            out("Fix " + fix + " was not found in nav database!");
        } else {
            for (Fix ff : l) {
                out(ff.toString());
            }
        }
    }

    protected void autoRoute(String ADEP, String ADES) {
        // list of suitable! SID exit points @ADEP

        // list of suitable! STAR entry points @ADES

        // Keep track of all distances covered by paths
        // dispose of any path which adds huge distance in relation to the
        // others
        // Keep only the good ones, decide on one or a couple of paths for
        // connection SID_Exit
        // to STAR_Entry.
        // 1) Start a path for each SID exit point sid_points = [s1, s2, ...,
        // sn]
        // 2) get all airways connected to the path
        // 3) get nearest point on the airway
        // 4) if a possible next point on the airway increases distance, discard
        // it, path can be thrown away

        // TODO
    }


    protected void actionGoogleMaps() {
        mapPanel.initUI();
        viewPanel.setSelectedIndex(1);
    }

    protected void actionProperties() {
        new FoxPropertiesDialog(this, FoxProperties.getInstance());
    }

    protected void actionExportPlans() {
        // save file
        FoxProperties properties = FoxProperties.getInstance();
        try {
            for (Plan plan : plans) {
                if (plan != null) {

                    // FS200x
                    String filename = properties.getProperty("FS200x") + System.getProperty("file.separator") + plan.getAdep().icao() + plan.getAdes().icao() + "001.PLN";
                    FileWriter fw = new FileWriter(filename);
                    fw.write(plan.getFs200xString().toString());
                    fw.flush();
                    fw.close();
                    out("FS FPL export to " + filename + " finished.");

                    filename = properties.getProperty("PMDG") + System.getProperty("file.separator") + plan.getAdep().icao() + plan.getAdes().icao() + ".rte";
                    fw = new FileWriter(filename);
                    fw.write(plan.getPMDGRteString().toString());
                    fw.flush();
                    fw.close();
                    out("PMDG FS FPL plan export to " + filename + " finished.");
                }
            }
        } catch (Exception ex) {
            log.severe("actionExportPlans failed: " + ex.getMessage());
        }

    }

    protected void actionRead() {
        for (int i = 0; i < 5; i++) {
            String fpl = FoxProperties.clearFPL(textFPL[i].getText().trim());
            textFPL[i].setText(fpl);
            String[] sFPL = splitFPL(fpl);
            if (sFPL == null) {
                plans[i] = null;
            } else {
                out(fpl);
                plans[i] = new Plan(sFPL[0], sFPL[1], sFPL[2]);
                plans[i].build(aw, ap);
                buttonExportFlightplans.setEnabled(plans[i] != null);
                buttonShowMap.setEnabled(plans[i] != null);
                FoxProperties.getInstance().addFPL(fpl);
            }
        }
    }

    private void outLine() {
        out("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
    }

    private String[] splitFPL(String o) {
        // 0 - ADEP
        // 1 - ADES
        // 2 - FPL
        // return null, if no valid sequence
        String[] tmp = new String[]{"", "", ""};
        String[] t1 = o.split(" ");
        if (t1.length < 3) return null;
        tmp[0] = t1[0];
        tmp[1] = t1[t1.length - 1];
        for (int i = 1; i < t1.length - 1; i++) {
            tmp[2] += t1[i] + " ";
        }
        return tmp;
    }

    public void out(final String text) {
        SwingUtilities.invokeLater(() -> {
            textInfo.append(System.getProperty("line.separator"));
            textInfo.append(text);
            textInfo.setCaretPosition(textInfo.getText().length() - 1);
        });
    }

    private void init() {
        aw = null;
        ap = null;
        fixDB = null;
        final FoxFrame myself = this;
        Thread t = new Thread(() -> {
            out("Reading Navdata...");
            out("Airports...");
            ap = new Airports();
            out("Fixes...");
            fixDB = new Fixes();
            out(fixDB.size() + " fixes");
            out("Airways...");
            aw = new Airways(fixDB);
            out("Database ready.");

            SwingUtilities.invokeLater(() -> {
                buttonRead.setEnabled(true);
                try {
                    try (BufferedReader fr = new BufferedReader(new FileReader(FoxMain.PATH_NAVDATA + System.getProperty("file.separator") + "cycle_info.txt"))) {
                        String cycle = fr.readLine();
                        myself.setTitle(FoxMain.VERSION + "    " + cycle);
                    }
                } catch (Exception ex) {
                    log.severe("init failed: " + ex.getMessage());
                }
            });
        });
        t.start();
    }

    private void handleErrorOnAutoRouteText(String text, final int requiredSize) {
        textFPL[4].setText(text);
        textFPL[4].setBackground(Color.RED);
        textFPL[4].addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {
                if (textFPL[4].getText().length() >= requiredSize) {
                    textFPL[4].setBackground(Color.YELLOW);
                } else textFPL[4].setBackground(Color.RED);
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
    }
}
