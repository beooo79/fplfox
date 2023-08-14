package com.github.beooo79;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;

public class FPLActionButton extends JButton
{

	private int index;
	private JTextArea textFPL;
	private FoxFrame foxFrame;

	public FPLActionButton(int i, JTextArea[] textFPL, FoxFrame foxFrame)
	{
		this.index = i;
		this.textFPL = textFPL[i];
		this.foxFrame = foxFrame;
		this.setText("FPL");
		setAction(new AbstractAction("FPL")
			{

				/**
				 * 
				 */
				private static final long serialVersionUID = 6106478438991937765L;

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					actionFPL(arg0);

				}
			});
	}

	public void actionFPL(ActionEvent e)
	{
		final JPopupMenu m = new JPopupMenu("Available FPL");
		for (final String s : FoxProperties.getInstance().getLastfpls())
		{
			String[] split = s.split(" ");
			if (split.length > 1)
			{
				m.add(new AbstractAction(split[0] + "->" + split[split.length - 1])
					{
						@Override
						public void actionPerformed(ActionEvent arg0)
						{
							textFPL.setText(s);
							m.setVisible(false);
						}
					});
			}
		}
		m.addSeparator();
		m.add(new AbstractAction("Close")
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					m.setVisible(false);
				}
			});
		m.show(this, 0, this.getHeight());
	}

	public Object getValue(String key)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void putValue(String key, Object value)
	{

	}

}
