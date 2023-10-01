package game_2048;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.*;


public class GUI1024 extends JPanel {
    public static void main(String arg[]){
        JFrame gui = new JFrame ("Welcome to 1024!");
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menu = new JMenuBar();
        
        JMenu gameMenu = new JMenu("File");
        
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem reset = new JMenuItem("Reset");
        JMenuItem setGoal = new JMenuItem("Set New Goal");
        
        gameMenu.add(exit);
        gameMenu.add(reset);
        gameMenu.add(setGoal);
        
        menu.add(gameMenu);
        gui.setJMenuBar(menu);
        
        GUI1024Panel jItems = new GUI1024Panel(exit, reset, setGoal);
//        GUI1024Panel panel = new GUI1024Panel();
        //panel.setFocusable(true);
        gui.setLayout(new GridLayout());
        gui.getContentPane().add(jItems);
        
        
        
//        exit.addActionListener(new ActionListener());
        
//        class MenuListener implements ActionListener {
//        	public void actionPerformed(ActionEvent e){
//        		if(e.getSource() == quitItem){
//        		}
//        	}
//        }
        
		gui.getContentPane().add(jItems);

        gui.setSize(jItems.getSize());
        gui.setVisible(true);
    }
    
}