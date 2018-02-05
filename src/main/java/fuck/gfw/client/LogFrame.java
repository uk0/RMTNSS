// Copyright (c) 2015 firsh.ME
package fuck.gfw.client;

import fuck.gfw.utils.LogListener;
import fuck.gfw.utils.LogOutputStream;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LogFrame extends JFrame implements LogListener {
	
	private static final long serialVersionUID = 8642892909397273483L;

	Client ui;

	JTextArea logArea;

	JScrollPane scroll;

	boolean autoScroll=true;

	final int SCROLL_BUFFER_SIZE = 1000;

	LogFrame(Client ui){
		super("日志");
		this.ui=ui;
		JPanel panel=(JPanel) getContentPane();


		logArea=new JTextArea();
		
		scroll = new JScrollPane(logArea); 
		
		panel.add(scroll,"width :10240:,height :10240: ,wrap");
		
		JPanel p3=new JPanel();
		panel.add(p3,"align center,wrap");

		final JCheckBox cb_lock=new JCheckBox("自动滚动",autoScroll);
		p3.add(cb_lock,"align center");
		cb_lock.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				autoScroll=cb_lock.isSelected();
			}

		});
		
		JButton button_clear=createButton("清空");
		p3.add(button_clear);
		button_clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				logArea.setText("");
			}
		});
		
	}
	
	public void trunkTextArea(JTextArea txtWin){
	    int numLinesToTrunk = txtWin.getLineCount() - SCROLL_BUFFER_SIZE;
	    if(numLinesToTrunk > 0)
	    {
	        try
	        {
	            int posOfLastLineToTrunk = txtWin.getLineEndOffset(numLinesToTrunk - 1);
	            txtWin.replaceRange("",0,posOfLastLineToTrunk);
	        }
	        catch (BadLocationException ex) {
	            ex.printStackTrace();
	        }
	    }
	}
	
	void showText(String text){
		logArea.append(text);
		trunkTextArea(logArea);
		if(autoScroll){
			JScrollBar vertical = scroll.getVerticalScrollBar();
			vertical.setValue(vertical.getMaximum() );
		}
	}

	@Override
	public void onAppendContent(LogOutputStream los,final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				logArea.append(text);
				trunkTextArea(logArea);
				if(autoScroll){
					logArea.setCaretPosition(logArea.getDocument().getLength());
//					JScrollBar vertical = scroll.getVerticalScrollBar();
//					vertical.setValue(vertical.getMaximum() );
				}
			}
		});
		
	}
	
	JButton createButton(String name){
		JButton button=new JButton(name);
		button.setMargin(new Insets(0,5,0,5));
		button.setFocusPainted(false);
		return button;
	}
	
	
}
