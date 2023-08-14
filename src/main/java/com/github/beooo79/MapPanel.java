package com.github.beooo79;

import com.github.beooo79.plan.*;
import org.jdesktop.swingx.JXMapKit;
import org.jdesktop.swingx.JXMapViewer;
import org.jdesktop.swingx.mapviewer.GeoPosition;
import org.jdesktop.swingx.mapviewer.Waypoint;
import org.jdesktop.swingx.mapviewer.WaypointPainter;
import org.jdesktop.swingx.mapviewer.WaypointRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;


public class MapPanel extends JPanel {

    private Fixes fixDB;
    private Plan[] plans;
    private Airways aw;
    private Airports ap;
    private JXMapKit map;

    private static class CustomWaypoint implements Waypoint {
        private Fix fix;
        private Fix last;
        private Fix next;
        private int planIndex;

        private int sequenceNumber;

        public int getPlanIndex() {
            return planIndex;
        }

        public void setPlanIndex(int planIndex) {
            this.planIndex = planIndex;
        }

        public boolean hasNext() {
            return next != null;
        }

        public boolean hasLast() {
            return last != null;
        }

        public Fix getLast() {
            return last;
        }

        public void setLast(Fix last) {
            this.last = last;
        }

        public CustomWaypoint(Fix f, int no) {
            super();
            this.planIndex = no;
            this.fix = f;
        }

        public CustomWaypoint(Fix f, Fix last, int no) {
            this(f, no);
            this.last = last;
        }

        public CustomWaypoint(Fix f, Fix last, Fix next, int no) {
            this(f, last, no);
            this.next = next;
        }

        public Fix getFix() {
            return fix;
        }

        public void setFix(Fix fix) {
            this.fix = fix;
        }

        public Fix getNext() {
            return next;
        }

        public void setNext(Fix next) {
            this.next = next;
        }

        public Color getColor() {
            return switch (planIndex) {
                case 0 -> Color.RED;
                case 1 -> Color.BLUE;
                case 2 -> Plan.DARK_GREEN;
                case 3 -> Color.BLACK;
                case 4 -> Plan.SLATE_GRAY;
                default -> Color.MAGENTA;
            };
        }

        public void setSequenceNumber(int i) {
            this.sequenceNumber = i;
        }

        public int getSequenceNumber() {
            return sequenceNumber;
        }

        @Override
        public GeoPosition getPosition() {
            return null;
        }
    }

    public MapPanel(Plan[] plans, Airways aw, Airports ap, Fixes fixDB) {
        super();
        this.plans = plans;
        this.aw = aw;
        this.ap = ap;
        this.fixDB = fixDB;
        setLayout(new BorderLayout());
        map = new JXMapKit();
        map.setDefaultProvider(JXMapKit.DefaultProviders.OpenStreetMaps);

        map.getMainMap().addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent m) {
                Point p = m.getPoint();
                map.getMainMap().setCenterPosition(map.getMainMap().convertPointToGeoPosition(p));

            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void mouseClicked(MouseEvent arg0) {
                // TODO Auto-generated method stub

            }
        });
        map.getMainMap().addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent k) {
                if (k.getKeyChar() == '+') {
                    int z = map.getMainMap().getZoom();
                    map.getMainMap().setZoom(z - 1);
                }
                if (k.getKeyChar() == '-') {
                    int z = map.getMainMap().getZoom();
                    map.getMainMap().setZoom(z + 1);
                }
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
            }
        });
        add(map, BorderLayout.CENTER);
    }

    public void initUI() {
        initMap();
    }

    public void initMap() {
        // create Set of waypoints
        Set<CustomWaypoint> wpts = new HashSet<>();
        fillWaypoints(wpts, plans);
        // create a WaypointPainter to draw the points
        WaypointPainter<CustomWaypoint> painter = new WaypointPainter<>();
        painter.setWaypoints(wpts);
        painter.setRenderer(this::waypointRenderer);

        map.getMainMap().setOverlayPainter(painter);
        // map.setCenterPosition(adep.getPosition());
        map.setZoom(15);
    }

    private WaypointRenderer<CustomWaypoint> waypointRenderer(Graphics2D graphics2D, JXMapViewer jxMapViewer, CustomWaypoint customWaypoint) {
        return (g, jxMapViewer1, customWaypoint1) -> {
            Point2D p = jxMapViewer1.getTileFactory().geoToPixel(customWaypoint1.getPosition(),
                    jxMapViewer1.getZoom());
            g.setColor(customWaypoint1.getColor());

            // Fix
            g.setStroke(new BasicStroke(1));
            g.fillRect(-4, -4, 8, 8);
            g.setColor(Color.WHITE);
            g.fillRect(-2, -2, 4, 4);

            g.setStroke(new BasicStroke(2));
            g.setColor(customWaypoint1.getColor());
            // Label
            String label = customWaypoint1.getFix().getId() + ":"
                    + customWaypoint1.getSequenceNumber();
            if (customWaypoint1.hasNext()) {
                Fix next = customWaypoint1.getNext();
                Point2D pNext = jxMapViewer1.getTileFactory().geoToPixel(
                        new GeoPosition(customWaypoint1.getNext().getLat(), customWaypoint1
                                .getNext().getLon()), jxMapViewer1.getZoom());
                if (pNext.getY() > p.getY()) {
                    g.drawString(label, +6, -4);
                } else {
                    g.drawString(label, +6, +10);
                }
            } else {
                g.drawString(label, +6, +10);
            }

            // Vector
            if (customWaypoint1.hasNext()) {
                Point2D pnext = jxMapViewer1.getTileFactory().geoToPixel(
                        new GeoPosition(customWaypoint1.getNext().getLat(), customWaypoint1
                                .getNext().getLon()), jxMapViewer1.getZoom());
                double dx = pnext.getX() - p.getX();
                double dy = pnext.getY() - p.getY();
                g.drawLine((int) dx, (int) dy, 0, 0);
            }
        };
    }

    private void fillWaypoints(Set<CustomWaypoint> wpts, Plan[] p) {
        for (int i = 0; i < p.length; i++) {
            Plan plan = p[i];
            if (plan != null) {
                // fill waypoint data
                CustomWaypoint adep = new CustomWaypoint(
                        plan.getAdep().toFix(), i);
                adep.setSequenceNumber(0);
                CustomWaypoint wpLast = adep;
                Fix last = plan.getAdep().toFix();
                // Points
                wpts.add(adep);
                int s = 1;
                for (Fix f : plan.getFixes()) {
                    CustomWaypoint w = new CustomWaypoint(f, last, i);
                    w.setSequenceNumber(s);
                    s++;
                    wpts.add(w);
                    last = f;
                    if (wpLast != null) {
                        wpLast.setNext(f);
                        wpLast = w;
                    }
                }
                CustomWaypoint ades = new CustomWaypoint(
                        plan.getAdes().toFix(), last, i);
                ades.setSequenceNumber(s);
                wpLast.setNext(plan.getAdes().toFix());
                wpts.add(ades);
            }
        }

    }
}
