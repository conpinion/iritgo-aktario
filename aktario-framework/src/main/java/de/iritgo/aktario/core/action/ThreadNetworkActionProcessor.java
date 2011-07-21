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

package de.iritgo.aktario.core.action;


import de.iritgo.aktario.core.Engine;
import de.iritgo.aktario.core.base.Transceiver;
import de.iritgo.aktario.core.network.Channel;
import de.iritgo.aktario.core.network.ClientTransceiver;
import de.iritgo.aktario.core.thread.Threadable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 *
 */
public class ThreadNetworkActionProcessor extends Threadable implements ActionProcessor,
				NetworkActionProcessorInterface, Cloneable
{
	protected HashMap channelProcessorMapping;

	protected List channelProcessors;

	protected Channel channel;

	protected NetworkActionProcessorInterface parentNetworkActionProcessor;

	protected List actions;

	protected Object listLock;

	/**
	 * Default constructor
	 */
	public ThreadNetworkActionProcessor (String typeId, Channel channel,
					NetworkActionProcessorInterface parentNetworkActionProcessor)
	{
		super (typeId);

		channelProcessorMapping = new HashMap ();
		channelProcessors = new LinkedList ();
		this.channel = channel;
		this.parentNetworkActionProcessor = parentNetworkActionProcessor;
		actions = new LinkedList ();
		listLock = new Object ();
		Engine.instance ().getThreadService ().add (this);
	}

	/**
	 * Set the channel for this thread action processor.
	 *
	 * @param channel The network channel
	 */
	public void setChannel (Channel channel)
	{
		this.channel = channel;
	}

	/**
	 * Init a network action processor
	 */
	public void init ()
	{
	}

	/**
	 * If a new channel is created this method will called.
	 *
	 * @param channel The new channel.
	 */
	public void newChannelCreated (Channel channel)
	{
		for (Iterator i = channelProcessorMapping.values ().iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next ()).newChannelCreated (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next ()).newChannelCreated (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * If a new channel is closed this method will called.
	 *
	 * @param channel The new channel.
	 */
	public void channelClosed (Channel channel)
	{
		for (Iterator i = channelProcessorMapping.keySet ().iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) channelProcessorMapping.get (i.next ())).channelClosed (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				((NetworkActionProcessorInterface) i.next ()).channelClosed (channel);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Checks the List and Processing the actions.
	 */
	@Override
	public void run ()
	{
		Action action = null;

		synchronized (listLock)
		{
			if (actions.size () > 0)
			{
				action = (Action) actions.get (0);
				actions.remove (action);
			}
		}

		if (action != null)
		{
			performAction (action, action.getTransceiver ());
		}

		synchronized (listLock)
		{
			if ((actions.size () == 0))
			{
				try
				{
					listLock.wait ();
				}
				catch (InterruptedException x)
				{
				}
			}
		}
	}

	/**
	 * Perform an action.
	 *
	 * @param action The action to perform.
	 */
	public void perform (Action action)
	{
		perform (action, action.getTransceiver ());
	}

	/**
	 * Add an action with a transceiver to the thread network action processor.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void perform (Action action, Transceiver transceiver)
	{
		synchronized (listLock)
		{
			actions.add (action);
			listLock.notify ();
		}
	}

	/**
	 * Perform an action with a transceiver.
	 *
	 * @param action The action to perform.
	 * @param transceiver The transceiver for this action.
	 */
	public void performAction (Action action, Transceiver transceiver)
	{
		Channel channel = ((ClientTransceiver) transceiver).getConnectedChannel ();

		if (channelProcessorMapping.get (channel) != null)
		{
			((ActionProcessor) channelProcessorMapping.get (channel)).perform (action, transceiver);
		}

		// 		for (Iterator i = channelProcessorMapping.keySet ().iterator (); i.hasNext ();)
		// 		{
		// 			Channel channelProcessor = (Channel) i.next ();
		// 			if (channelProcessor == channel)
		// 			{
		// 				((ActionProcessor) channelProcessorMapping.get (channel)).perform (
		// 					action, transceiver);
		// 			}
		// 		}
		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				((ActionProcessor) i.next ()).perform (action, transceiver);
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Add a output action processor to this network action processor and the specified channel.
	 *
	 * @param channel The channel.
	 * @param actionProcessor The action processor.
	 */
	public void addOutput (Channel channel, ActionProcessor actionProcessor)
	{
		channelProcessorMapping.put (channel, actionProcessor);
	}

	/**
	 * Remove a action processor from the output and the specified channel
	 *
	 * @param channel The channel.
	 */
	public void removeOutput (Channel channel)
	{
		channelProcessorMapping.remove (channel);
	}

	/**
	 * Add a output action processor to this network action processor. This processor will calls for all channels
	 *
	 * @param actionProcessor The action processor
	 */
	public void addOutput (ActionProcessor actionProcessor)
	{
		channelProcessors.add (actionProcessor);
	}

	/**
	 * Remove a action processor from the output.
	 *
	 * @param actionProcessor The action processor.
	 */
	public void removeOutput (ActionProcessor actionProcessor)
	{
		channelProcessors.remove (actionProcessor);
	}

	public boolean actionsInProcess ()
	{
		return actions.size () > 0;
	}

	/**
	 * Clone a new instance from this processor with all outputs
	 *
	 * @return NetworkActionProcessor
	 */
	@Override
	public Object clone ()
	{
		ThreadNetworkActionProcessor clone = new ThreadNetworkActionProcessor (typeId, channel,
						parentNetworkActionProcessor);

		cloneOutputs (clone);

		return clone;
	}

	public void cloneOutputs (NetworkActionProcessorInterface clone)
	{
		for (Iterator i = channelProcessorMapping.keySet ().iterator (); i.hasNext ();)
		{
			try
			{
				Channel channel = (Channel) i.next ();

				clone.addOutput (channel, (ActionProcessor) ((ActionProcessor) channelProcessorMapping.get (channel))
								.clone ());
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}

		for (Iterator i = channelProcessors.iterator (); i.hasNext ();)
		{
			try
			{
				clone.addOutput ((ActionProcessor) ((ActionProcessor) i.next ()).clone ());
			}
			catch (ClassCastException nothingToDo)
			{
			}
		}
	}

	/**
	 * Called from the ThreadController to close this Thread.
	 */
	@Override
	public void dispose ()
	{
		synchronized (listLock)
		{
			if (channel != null)
			{
				System.out.println ("Dispose ThreadNetworkActionProcessor for channel: "
								+ channel.getChannelNumber ()
								+ ":"
								+ ((channel.getCustomerContextObject () != null ? channel.getCustomerContextObject ()
												.toString () : "-")));
			}
			else
				System.out.println ("Dispose ThreadNetworkActionProcessor... ");

			setState (Threadable.CLOSING);
			listLock.notify ();
		}
	}

	/**
	 * Close a network action processor
	 */
	public void close ()
	{
		dispose ();
	}
}
