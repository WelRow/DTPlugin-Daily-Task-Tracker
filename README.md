# DTPlugin-Daily-Task-Tracker
An explicit implementation of a DockTask Plugin which track user tasks.

## Setup
- An already configured DockTask running on the **dev** branch running on IntelliJ IDEA
    - Its IMPORTANT its in the dev branch due to a required file `PluginManager` not being in main.

## How to implement it and run (explicit)

1. Download the files `TaskCounterPlugin.java` and `taskcounter.css`
2. Store `TaskCounterPlugin.java` in file location *\src\main\java\com.ksaifstack.doctask\plugins\firstparty
    1. (*) is the root for DockTask
3. Store `taskcounter.css` in file location *\src\main\resources\plugins\firstparty
4. Navigate to *\src\main\java\com.ksaifstack.doctask\plugins and edit `PluginManager.java` and make the following additions:

``` 
package com.ksaifstack.docktask.plugins;

import com.ksaifstack.docktask.plugins.firstparty.PomodoroPlugin;
import com.ksaifstack.docktask.plugins.firstparty.TaskCounterPlugin; <-- ADD THIS LINE

import java.util.ArrayList;
import java.util.List;

public class PluginManager {
    private static final List<DockTaskPlugin> registry = new ArrayList<>();
    private static final List<Runnable> widgetVisibilityListeners = new ArrayList<>();

    static {
        // Hardcoded registration for v0.8.0 foundation
        registry.add(new PomodoroPlugin());
        registry.add(new TaskCounterPlugin()); <-- ADD THIS LINE
    }
```
5. Navigate to Main and run!

