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


import de.iritgo.aktario.core.logger.Log;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 *
 */
public class ThreadController extends ThreadPoolExecutor
{
	private static int CORE_THREAD_SIZE = 8;

	private static int MAX_THREAD_SIZE = 300;

	private static int POOL_THREAD_TIMEOUT = 60 * 10;

	public ThreadController(@SuppressWarnings("unused") int corePoolSize)
	{
		super(CORE_THREAD_SIZE, MAX_THREAD_SIZE, POOL_THREAD_TIMEOUT, TimeUnit.SECONDS, new SynchronousQueue(true),
						Executors.defaultThreadFactory());
	}

	/**
	 * Add a threaded Object to pool
	 *
	 * @param threadable The Threadable Object
	 */
	public void add(Threadable threadable)
	{
		Log.logVerbose("thread", "ThreadController", "Threadable added: " + threadable.getName());
		Log.logInfo("thread", "ThreadController", "Active threads: " + getActiveCount() + " Pool size: "
						+ getPoolSize());

		try
		{
			execute(threadable);
		}
		catch (Exception x)
		{
			Log.logVerbose("thread", "ThreadController", "Can not add Threadable! " + threadable.getName());
		}
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t)
	{
		Threadable threadable = (Threadable) r;

		try
		{
			if (threadable.getState() == Threadable.FREE)
			{
				execute(threadable);
			}
			else if (threadable.getState() == Threadable.RUNNING)
			{
				execute(threadable);
			}
			else
			{
				Log.logInfo("thread", "ThreadController", "Thread removed: " + threadable.getName());
			}
		}
		catch (Exception x)
		{
			Log.logVerbose("thread", "ThreadController", "Can not add Threadable! " + threadable.getName());
		}
	}

	/**
	 * Kill all threads.
	 *
	 * @return True if all threads are successfully killed.
	 */
	public boolean release()
	{
		System.out.println("[Iritgo] Terminating all threads");

		shutdown();
		shutdownNow();

		while (! isTerminated())
		{
			try
			{
				Thread.sleep(500);
			}
			catch (Exception x)
			{
			}
		}

		System.out.println("[Iritgo] All threads terminated");

		return true;
	}

	public void notifySlotFree()
	{
	}

	/**
	 * Add a ThreadSlot.
	 */
	public void addSlot()
	{
		// 		setCorePoolSize (getCorePoolSize () + 1);
		// 		Log.logDebug (
		// 			"thread", "ThreadController", "Core pool size: " + getCorePoolSize ());
	}

	/**
	 * Return the available slots
	 *
	 * @return The available slots
	 */
	public int getAvailableSlots()
	{
		return getCorePoolSize() - getActiveCount();
	}
}
