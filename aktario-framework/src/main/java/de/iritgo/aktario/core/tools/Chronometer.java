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

package de.iritgo.aktario.core.tools;


import java.util.HashMap;


/**
 * Useful methods for time calculations
 */
public class Chronometer
{
	private static HashMap chronometers;

	private String key;

	private int loops = 0;

	private long startTime;

	private long stopTime;

	private long durationTime = 0;

	public Chronometer(String key)
	{
		this.key = key;
	}

	static synchronized private void checkChronometersMap()
	{
		if (chronometers == null)
		{
			chronometers = new HashMap();
		}
	}

	static public void startTimer(String key)
	{
		checkChronometersMap();

		Chronometer chronometer = (Chronometer) chronometers.get(key);

		if (chronometer == null)
		{
			chronometer = new Chronometer(key);
			chronometers.put(key, chronometer);
		}

		if (chronometer.getKey().equals(key))
		{
			chronometer.setStartTime();
		}
		else
		{
			Chronometer.startTimer(key);
		}
	}

	static public void stopTimer(String key)
	{
		checkChronometersMap();

		Chronometer chronometer = (Chronometer) chronometers.get(key);

		if (chronometer == null)
		{
			chronometer = new Chronometer(key);
			chronometers.put(key, chronometer);
		}

		if (chronometer.getKey().equals(key))
		{
			chronometer.setStopTime();
			chronometer.calc();
		}
		else
		{
			Chronometer.startTimer(key);
		}
	}

	static public long getResult(String key)
	{
		checkChronometersMap();

		Chronometer chronometer = (Chronometer) chronometers.get(key);

		if (chronometer == null)
		{
			chronometer = new Chronometer(key);
			chronometers.put(key, chronometer);
		}

		if (chronometer.getKey().equals(key))
		{
			return chronometer.getDurationResult();
		}
		else
		{
			Chronometer.startTimer(key);
		}

		return 0;
	}

	static public void printResult(String key, int mod)
	{
		checkChronometersMap();

		Chronometer chronometer = (Chronometer) chronometers.get(key);

		if (chronometer == null)
		{
			chronometer = new Chronometer(key);
			chronometers.put(key, chronometer);
		}

		if (chronometer.getKey().equals(key))
		{
			chronometer.printDurationResult(mod);
		}
		else
		{
			Chronometer.startTimer(key);
		}
	}

	public void printDurationResult(int mod)
	{
		if ((mod == 0) || loops % mod == 0)
		{
			System.out.println("Key: " + key + " Time: " + getDurationResult());
		}
	}

	public long getDurationResult()
	{
		if (durationTime == 0 || loops == 0)
		{
			return 0;
		}

		return durationTime / loops;
	}

	public void calc()
	{
		durationTime += (stopTime - startTime);
		++loops;
	}

	public void setStartTime()
	{
		startTime = System.currentTimeMillis();
	}

	public void setStopTime()
	{
		stopTime = System.currentTimeMillis();
	}

	public String getKey()
	{
		return key;
	}
}
