package com.github.beooo79;

public class Benchmark
{
	private static long lastTime;

	public static String memoryString()
	{
		long memFreeBytes = Runtime.getRuntime().freeMemory();
		float memFreeMBytes = memFreeBytes / (1024f * 1024f);

		return "Benchmark.FreeVMMem" + Math.round(memFreeMBytes) + " MB"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Liefert den aktuellen Speicherverbrauch der VM
	 */
	public static void memory()
	{
		float memTotal = Runtime.getRuntime().totalMemory() / (1024f * 1024f);
		float memMax = Runtime.getRuntime().maxMemory() / (1024f * 1024f);
		float memFree = Runtime.getRuntime().freeMemory() / (1024f * 1024f);
		float memUsedMBytes = memTotal - memFree;

		System.out
				.println("(max, total, free):" + memMax + ", " + memTotal + ", " + memFree + " MB    --->   memory used: " + memUsedMBytes + " MB"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	/**
	 * Aktuelle Zeitspanne ausgeben
	 *
	 * @param action Beschreibung der Aktion, die gemessen wird
	 */
	public static void split(String action)
	{
		long tmp = (System.currentTimeMillis() - lastTime);
		if (tmp > 0)
		{
		System.out.println(action + " took " + 	tmp + "ms");
		}
	}

	/**
	 * Benchmark starten/resetten
	 */
	public static void reset()
	{
		lastTime = System.currentTimeMillis();
	}
}