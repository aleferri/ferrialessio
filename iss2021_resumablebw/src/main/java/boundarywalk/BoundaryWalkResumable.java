/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boundarywalk;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author alessioferri
 */
public class BoundaryWalkResumable {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        var ws = new VirtualRobotWS("localHost:8091");
        var arilInterpreter = new VirtualRobotClientARIL(ws);
        var controller = new BoundaryWalkController(arilInterpreter, ws);
        
        var frame = new JFrame("Controllo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        var resume = new JButton("Resume");
        var stop = new JButton("Stop");
        var jPanel = new JPanel();
        jPanel.add(resume);
        jPanel.add(stop);
        frame.setContentPane(jPanel);
        frame.setSize(400, 400);
        frame.setLocationRelativeTo(null);
        
        final var resumableOperation = new ResumeController(controller::walkBoundary);
        
        resume.addActionListener((action) -> {
            resumableOperation.resume();
        });
        
        stop.addActionListener((action) -> {
            resumableOperation.stop();
        });
        
        frame.setVisible(true);
    }
    
}
