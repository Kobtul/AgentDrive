/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.agents.highway.vis;

import cz.agents.alite.configurator.Configurator;
import cz.agents.alite.vis.Vis;
import cz.agents.alite.vis.layer.VisLayer;
import cz.agents.alite.vis.layer.common.CommonLayer;
import cz.agents.alite.vis.layer.toggle.KeyToggleLayer;
import cz.agents.highway.environment.roadnet.XMLReader;
import cz.agents.highway.storage.RoadObject;
import cz.agents.highway.vanet.Vanet;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;

/**
 *
 * @author ondra
 */
public class VanetVisLayer extends CommonLayer{
    private final int WIDTH_LINE = 4;

    private boolean display = true;

    private final Vanet vanet;

    public VanetVisLayer(Vanet vanet){
        this.vanet = vanet;
    }
    
    @Override
    public void paint(Graphics2D canvas) {
        BasicStroke stroke = new BasicStroke(0.2f,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND,
                1f,
                new float[] {2f},
                0f);
        canvas.setStroke(stroke);
        canvas.setColor(Color.white);
        for (RoadObject object : vanet.getIncludedObjects().values()){
            for (RoadObject another : vanet.getConnectedObjects().get(object.getId())){
                AffineTransform t = canvas.getTransform();
                canvas.translate(Vis.transX(object.getPosition().getX()), Vis.transY(object.getPosition().getY()));
                canvas.scale(Vis.getZoomFactor(), Vis.getZoomFactor());
                canvas.draw(new Line2D.Double(0,0, another.getPosition().getX() - object.getPosition().getX(),
                                                   another.getPosition().getY() - object.getPosition().getY()));
                canvas.setTransform(t);
            }
        }
    }
    
    
    
    public static VisLayer create(Vanet vanet) {
        KeyToggleLayer toggle = KeyToggleLayer.create("o");
        toggle.addSubLayer(new VanetVisLayer(vanet));
        return toggle;
    }
    
    @Override
    public void init(Vis vis) {
        super.init(vis);

        vis.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'v') {
                    display = !display;
                }
            }
        });
    }
    
    @Override
    public String getLayerDescription() {
        String description = "[Vis Vanet] The layer shows network connections between objects, to hide press 'v'";
        return buildLayersDescription(description);
    }

}
