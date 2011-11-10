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

package de.iritgo.aktario.core.thread;


import de.iritgo.aktario.core.base.BaseObject;


/**
 * Threadables are objets that can be executed by a ThreadController.
 *
 * Threadables are working units that are thrown into a thread pool to
 * find a free thread that can execute the threadable.
 */
public abstract class Threadable extends BaseObject implements Runnable
{
	/** Nothing to do state. */
	public static final int FREE = 0;

	/** Currently working state. */
	public static final int RUNNING = 1;

	/** Closing state. */
	public static final int CLOSING = 2;

	/** The current state of the threadable. */
	private int currentState;

	/** The thread controller. */
	protected ThreadController controller;

	/** The name of the threadable. */
	private String name;

	/**
	 * Create a new Threadable.
	 */
	public Threadable()
	{
		currentState = FREE;
	}

	/**
	 * Create a new Threadable.
	 *
	 * @param name The name of the new threadable.
	 */
	public Threadable(String name)
	{
		super(name);
		currentState = FREE;
		this.name = name;
	}

	/**
	 * Set the thread controller.
	 *
	 * @param controller The thread controller.
	 */
	public void setThreadController(ThreadController controller)
	{
		this.controller = controller;
	}

	/**
	 * Retrieve the thread controller.
	 *
	 * @return The thread controller.
	 */
	public ThreadController getThreadController()
	{
		return controller;
	}

	/**
	 * Set the threadable state.
	 *
	 * @param currentState The new state.
	 */
	public void setState(int currentState)
	{
		this.currentState = currentState;
	}

	/**
	 * Get the current threadable state.
	 *
	 * @return The current state.
	 */
	public int getState()
	{
		return currentState;
	}

	/**
	 * Get the name of this threadable.
	 *
	 * @return The threadable name.
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * The work method.
	 * Subclasses should override this method to provide a threading
	 * task.
	 */
	public abstract void run();

	/**
	 * Called from the thread controller to free all resources of
	 * this threadable.
	 */
	public void dispose()
	{

	}
}
