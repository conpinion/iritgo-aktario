/**
 * This file is part of the Iritgo/Aktario Framework.
 *
 * Copyright (C) 2005-2011 Iritgo Technologies.
 * Copyright (C) 2003-2005 BueroByte GbR.
 *
 * Iritgo licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.iritgo.aktario.core.filebrowser;


import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;


@SuppressWarnings("serial")
public class FileBrowser extends JTree
{
	public static final String DELETE_CMD = "delete";

	private Frame frame = null;

	private SimpleDateFormat formatter = null;

	/**
	 * Our TreeWillExpandListener
	 */
	TreeWillExpandListener twel = new TreeWillExpandListener ()
	{
		public void treeWillExpand (TreeExpansionEvent event) throws ExpandVetoException
		{
			TreeNode node = (TreeNode) event.getPath ().getLastPathComponent ();

			((FileNode) node).nodeWillExpand ();
			((DefaultTreeModel) getModel ()).reload (node);
		}

		public synchronized void treeWillCollapse (TreeExpansionEvent event) throws ExpandVetoException
		{
			TreeNode node = (TreeNode) event.getPath ().getLastPathComponent ();

			((FileNode) node).nodeWillCollapse ();
			((DefaultTreeModel) getModel ()).reload (node);
		}
	};

	/**
	 * The popup menu action listener.
	 */
	ActionListener pmal = new ActionListener ()
	{
		public void actionPerformed (ActionEvent evt)
		{
			setCursor (Cursor.WAIT_CURSOR);

			String command = evt.getActionCommand ();

			if (command.equals ("del"))
			{
				deleteSelectedFiles ();
			}
			else if (command.equals ("newfolder"))
			{
				createNewFolder ();
			}

			setCursor (Cursor.DEFAULT_CURSOR);
		}
	};

	/**
	 * Our ActionListener
	 */
	ActionListener al = new ActionListener ()
	{
		public void actionPerformed (ActionEvent evt)
		{
			String command = evt.getActionCommand ();

			if (command.equals (DELETE_CMD))
			{
				deleteSelectedFiles ();
			}
		}
	};

	/**
	 * Our MouseListener
	 */
	MouseAdapter mouseAdapter = new MouseAdapter ()
	{
		@Override
		public void mousePressed (MouseEvent e)
		{
			maybeShowPopup (e);
		}

		@Override
		public void mouseReleased (MouseEvent e)
		{
			maybeShowPopup (e);
		}

		private void maybeShowPopup (MouseEvent e)
		{
			if (e.isPopupTrigger ())
			{
				FileNode node = (FileNode) getLastSelectedPathComponent ();

				if (node != null)
				{
					getPopupMenu (node).show (e.getComponent (), e.getX (), e.getY ());
				}
			}
		}
	};

	public FileBrowser (FileNode root)
	{
		super (root);
		setRootVisible (false);
		setEditable (true);
		setLargeModel (true);
		setScrollsOnExpand (true);
		addTreeWillExpandListener (twel);

		KeyStroke delK = KeyStroke.getKeyStroke (KeyEvent.VK_DELETE, 0);

		registerKeyboardAction (al, DELETE_CMD, delK, WHEN_FOCUSED);
		addMouseListener (mouseAdapter);
	}

	public FileBrowser ()
	{
		this (new FileNode ("My Computer", new FilenameFilter ()
		{
			public boolean accept (File dir, String name)
			{
				return name.charAt (0) != '.';
			}
		}));

		((FileNode) getModel ().getRoot ()).initializeRootNode (this);
	}

	/**
	 * Get a DateFormat compliant with RFC 822 updated by RFC 1123.
	 * @return a SimpleDateFormat instance.
	 */
	private SimpleDateFormat getDateFormatter ()
	{
		if (formatter == null)
		{
			formatter = new SimpleDateFormat ("dd MMM yyyy HH:mm");

			//formatter.setTimeZone(TimeZone.getTimeZone(""));
		}

		return formatter;
	}

	/**
	 * Filter the TreePath array. Remove all nodes that have one of their
	 * parent in this array.
	 * @param paths the TreePath array
	 * @return the filtered array
	 */
	protected TreePath[] removeDescendants (TreePath[] paths)
	{
		if (paths == null)
		{
			return null;
		}

		Vector newpaths = new Vector ();

		for (int i = 0; i < paths.length; i++)
		{
			TreePath currentp = paths[i];
			boolean hasParent = false;

			for (int j = 0; j < paths.length; j++)
			{
				if ((! (j == i)) && (paths[j].isDescendant (currentp)))
				{
					hasParent = true;
				}
			}

			if (! hasParent)
			{
				newpaths.addElement (currentp);
			}
		}

		TreePath[] filteredPath = new TreePath[newpaths.size ()];

		newpaths.copyInto (filteredPath);

		return filteredPath;
	}

	protected void createNewFolder ()
	{
		TreePath path = getSelectionPath ();
		FileNode node = (FileNode) path.getLastPathComponent ();

		if (node.hasFile ())
		{
			File dir = node.getFile ();

			if (dir.isDirectory ())
			{
				File newfolder = new File (dir, "NewFolder");
				int i = 0;

				while (newfolder.exists ())
				{
					newfolder = new File (dir, "NewFolder" + (++i));
				}

				if (newfolder.mkdirs ())
				{
					FileNode newnode = new FileNode (this, node, newfolder);

					addNode (node, newnode);
				}
			}
		}
	}

	protected void deleteSelectedFiles ()
	{
		TreePath[] path = removeDescendants (getSelectionPaths ());

		if (path != null)
		{
			int result = JOptionPane.showConfirmDialog (this, "Delete selected resource(s)?", "Delete Resource(s)",
							JOptionPane.YES_NO_OPTION);

			if (result == JOptionPane.YES_OPTION)
			{
				DefaultTreeModel model = (DefaultTreeModel) getModel ();

				for (int i = 0; i < path.length; i++)
				{
					FileNode fnode = (FileNode) path[i].getLastPathComponent ();
					File file = fnode.getFile ();

					if ((file != null) && (file.delete ()))
					{
						MutableTreeNode node = (MutableTreeNode) path[i].getLastPathComponent ();

						model.removeNodeFromParent (node);
					}
					else
					{
						JOptionPane.showMessageDialog (this, "Can't delete!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}

	//Popup Menu

	/**
	 * Get the popup menu relative to the selected resource.
	 * @param file the selected file
	 * @return a JPopupMenu instance
	 */
	protected JPopupMenu getPopupMenu (FileNode node)
	{
		JPopupMenu popupMenu = new JPopupMenu ("Actions");
		JMenuItem menuItem = null;

		String name = node.getName ();

		if (getSelectionCount () > 1)
		{
			menuItem = new JMenuItem ("Delete files");
			menuItem.addActionListener (pmal);
			menuItem.setActionCommand ("del");
			popupMenu.add (menuItem);
		}
		else
		{
			File file = node.getFile ();

			if (file != null)
			{
				Date date = new Date (file.lastModified ());
				StringBuffer descr = new StringBuffer (name);

				descr.append (", ");
				descr.append (getDateFormatter ().format (date));

				if (file.isFile ())
				{
					long size = file.length ();
					String s = null;

					if (size > 1023)
					{
						s = " [" + (size / 1024) + " Kb]";
					}
					else
					{
						s = " [" + size + " bytes]";
					}

					descr.append (s);
				}

				menuItem = new JMenuItem (descr.toString ());
				menuItem.addActionListener (pmal);
				menuItem.setActionCommand ("info");
				popupMenu.add (menuItem);

				popupMenu.addSeparator ();

				if (file.isDirectory () && (getSelectionCount () == 1))
				{
					menuItem = new JMenuItem ("New Folder");
					menuItem.addActionListener (pmal);
					menuItem.setActionCommand ("newfolder");
					popupMenu.add (menuItem);
				}

				menuItem = new JMenuItem ("Delete " + name);
				menuItem.addActionListener (pmal);
				menuItem.setActionCommand ("del");
				popupMenu.add (menuItem);
			}
			else
			{
				menuItem = new JMenuItem (name);
				popupMenu.add (menuItem);
			}
		}

		return popupMenu;
	}

	//
	// node operations
	//
	protected void removeNodeFromParent (FileNode node)
	{
		((DefaultTreeModel) getModel ()).removeNodeFromParent (node);
	}

	protected void addNode (FileNode parent, FileNode child)
	{
		((DefaultTreeModel) getModel ()).insertNodeInto (child, parent, 0);
		child.setParent (parent);
	}

	//
	// Frame
	//
	protected Frame getFrame ()
	{
		if (frame == null)
		{
			frame = (Frame) SwingUtilities.getAncestorOfClass (Frame.class, this);
		}

		return frame;
	}

	public void setCursor (int cursor)
	{
		getFrame ().setCursor (Cursor.getPredefinedCursor (cursor));
	}

	public static FileBrowser getFileBrowser (@SuppressWarnings("unused") String rootname)
	{
		FilenameFilter filter = new FilenameFilter ()
		{
			public boolean accept (File dir, String name)
			{
				return (name.charAt (0) != '.');
			}
		};

		FileNode root = new FileNode ("My Computer", filter);
		FileBrowser browser = new FileBrowser (root);

		root.initializeRootNode (browser);

		return browser;
	}

	public static void main (String[] args)
	{
		//         Utils.unBoldSpecificFonts();
		JFrame frame = new JFrame ("File Browser");
		JScrollPane scrollpane = new JScrollPane (getFileBrowser ("My Computer"));

		frame.getContentPane ().add (scrollpane, BorderLayout.CENTER);
		frame.setSize (300, 600);
		frame.setVisible (true);
	}

	public void setRoot (File file)
	{
		FileNode root = new FileNode (file, new FilenameFilter ()
		{
			public boolean accept (File dir, String name)
			{
				return name.charAt (0) != '.';
			}
		});

		setModel (new DefaultTreeModel (root));
		root.initializeRootNode (this);
	}
}
