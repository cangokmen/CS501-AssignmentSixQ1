# Assignment Six – Q1: Calculate Altitude Changes

## Overview
**Calculate Altitude Changes** is an Android app that functions as a simple **altimeter**, converting pressure sensor readings into altitude and displaying them in real time.  
The app dynamically updates both altitude values and the background color to visually reflect elevation changes.

---

## Features
- 🧭 **Live Altimeter UI** – Displays real-time altitude calculated from atmospheric pressure  
- 🌡️ **Pressure Sensor Integration** – Reads barometric pressure from the device sensor  
- 🧪 **Simulation Mode** – Allows testing by simulating pressure changes for devices without a sensor  
- 🎨 **Dynamic Background** – Background color darkens at higher altitudes for clear visual feedback  
- 🔢 **Accurate Formula Conversion** – Converts pressure readings to altitude using the following equation:  

  \[
  h = 44330 \times \left(1 - \left(\frac{P}{P_0}\right)^{\frac{1}{5.255}}\right)
  \]

  where:  
  - *h* = altitude (in meters)  
  - *P* = measured pressure  
  - *P₀* = reference pressure (1013.25 hPa)  

---

## How It Works
1. The app registers a **pressure sensor listener** (or uses a simulated pressure flow).  
2. Each sensor update triggers altitude recalculation using the above formula.  
3. The **UI updates in real time**, showing:
   - Current altitude  
   - Pressure reading  
   - Visual color changes with altitude level  
4. Users can simulate different altitudes for testing on devices without hardware sensors.  

---

## How to Run
```bash
git clone https://github.com/cangokmen/CS501-AssignmentSixQ1
# Open in Android Studio and run on an emulator or device
```

## How to Use
1. Launch the app — the home screen shows the live altitude reading.
2. Move your device or simulate changes to observe:
   - Updated altitude values  
   - Background color darkening at higher altitudes
3. Stop or reset the simulation as needed for testing.
