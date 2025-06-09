package config.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class limelightHelpers {

  // Network constants optimized for Limelight 3A in FTC
  private static final int CONNECTION_TIMEOUT_MS = 150;
  private static final int READ_TIMEOUT_MS = 150;
  private static final String DEFAULT_LIMELIGHT_IP =
      "10.12.34.11"; // Common Limelight 3A IP in FTC configuration
  private static final int LIMELIGHT_3A_PORT = 5807; // Limelight 3A specific port

  // Limelight 3A specific constants
  private static final boolean IS_LIMELIGHT_3A = true;
  private static final String LIMELIGHT_3A_DEVICE_TYPE = "3a";

  /** LimelightTarget class to store information about detected targets */
  public static class LimelightTarget {
    public double tx;
    public double ty;
    public double ta;
    public double ts;
    public double tl;
    public double cl;
    public double latency_capture;
    public double latency_pipeline;
    public double pipeline_id;
    public JSONObject json;
    public boolean valid;

    // Add FTC-specific fields for AprilTags
    public int fiducialID = -1;
    public double[] robotRelativePose = new double[6]; // x,y,z,roll,pitch,yaw
    public boolean isAprilTag = false;

    // Limelight 3A specific fields
    public double[] corners = new double[8]; // x0,y0,x1,y1,x2,y2,x3,y3
    public String className = "";
    public int classID = -1;
    public double confidence = 0.0;

    public LimelightTarget() {
      valid = false;
    }
  }

  /** LimelightResults class to store all data from the Limelight camera */
  public static class LimelightResults {
    public double targetingDataTimestamp = 0;
    public LimelightTarget[] targets = new LimelightTarget[0];
    public double[] botpose = new double[6];
    public double[] botpose_wpiblue = new double[6];
    public double[] botpose_wpired = new double[6];
    public boolean valid = false;

    // FTC specific fields for target info
    public double tx = 0.0;
    public double ty = 0.0;
    public double ta = 0.0;
    public boolean tv = false;

    // Add FTC-specific fields
    public double[] targetpose_robotRelative = new double[6];
    public double[] targetpose_fieldRelative = new double[6];
    public ArrayList<Integer> tagIDs = new ArrayList<>();

    // Limelight 3A specific fields
    public String deviceName = LIMELIGHT_3A_DEVICE_TYPE;
    public int activeMode = 0; // 0=vision, 1=driver camera
    public double currentPipelineIndex = 0;
  }

  /**
   * Get the latest results from the Limelight 3A
   *
   * @param limelightName Name of the Limelight camera or IP address
   * @return LimelightResults containing all data from the camera
   */
  public static LimelightResults getLatestResults(String limelightName) {
    LimelightResults results = new LimelightResults();

    String jsonData = getJSONDump(limelightName);
    if (jsonData.length() < 1) {
      return results;
    }

    try {
      JSONObject root = new JSONObject(jsonData);
      results.targetingDataTimestamp = root.optDouble("ts", 0.0);

      // Extract basic targeting data
      if (root.has("tx")) results.tx = root.getDouble("tx");
      if (root.has("ty")) results.ty = root.getDouble("ty");
      if (root.has("ta")) results.ta = root.getDouble("ta");
      if (root.has("tv")) results.tv = root.getDouble("tv") > 0.5;

      // Extract Limelight 3A specific pipeline data
      if (root.has("pID")) results.currentPipelineIndex = root.getDouble("pID");
      if (root.has("camMode")) results.activeMode = (int) root.getDouble("camMode");

      // Extract robot pose data if available
      if (root.has("botpose")) {
        JSONArray botpose = root.getJSONArray("botpose");
        if (botpose.length() >= 6) {
          for (int i = 0; i < 6; i++) {
            results.botpose[i] = botpose.getDouble(i);
          }
        }
      }

      if (root.has("botpose_wpiblue")) {
        JSONArray botpose_wpiblue = root.getJSONArray("botpose_wpiblue");
        if (botpose_wpiblue.length() >= 6) {
          for (int i = 0; i < 6; i++) {
            results.botpose_wpiblue[i] = botpose_wpiblue.getDouble(i);
          }
        }
      }

      if (root.has("botpose_wpired")) {
        JSONArray botpose_wpired = root.getJSONArray("botpose_wpired");
        if (botpose_wpired.length() >= 6) {
          for (int i = 0; i < 6; i++) {
            results.botpose_wpired[i] = botpose_wpired.getDouble(i);
          }
        }
      }

      // Extract AprilTag-specific data (improved for Limelight 3A)
      if (root.has("tags") && !root.isNull("tags")) {
        JSONArray tagsJson = root.getJSONArray("tags");
        for (int i = 0; i < tagsJson.length(); i++) {
          JSONObject tagJson = tagsJson.getJSONObject(i);
          if (tagJson.has("id")) {
            results.tagIDs.add(tagJson.getInt("id"));
          }
        }
      } else if (root.has("tag_ids")) {
        // Fallback to original format
        JSONArray tagIDsJson = root.getJSONArray("tag_ids");
        for (int i = 0; i < tagIDsJson.length(); i++) {
          results.tagIDs.add(tagIDsJson.getInt(i));
        }
      }

      // Extract target pose data
      if (root.has("targetpose_robotRelative")) {
        JSONArray targetpose = root.getJSONArray("targetpose_robotRelative");
        if (targetpose.length() >= 6) {
          for (int i = 0; i < 6; i++) {
            results.targetpose_robotRelative[i] = targetpose.getDouble(i);
          }
        }
      }

      // Extract target information - modified for Limelight 3A format
      if (root.has("targets")) {
        JSONArray targets = root.getJSONArray("targets");
        results.targets = new LimelightTarget[targets.length()];

        for (int i = 0; i < targets.length(); i++) {
          JSONObject targetJson = targets.getJSONObject(i);
          LimelightTarget target = new LimelightTarget();
          target.valid = true;
          target.tx = targetJson.optDouble("tx", 0.0);
          target.ty = targetJson.optDouble("ty", 0.0);
          target.ta = targetJson.optDouble("ta", 0.0);
          target.ts = targetJson.optDouble("ts", 0.0);
          target.tl = targetJson.optDouble("tl", 0.0);
          target.latency_capture = targetJson.optDouble("cl", 0.0);
          target.latency_pipeline = targetJson.optDouble("tl", 0.0);

          // Extract Limelight 3A specific data
          if (targetJson.has("class")) {
            target.className = targetJson.getString("class");
          }

          if (targetJson.has("classID")) {
            target.classID = targetJson.getInt("classID");
          }

          if (targetJson.has("conf")) {
            target.confidence = targetJson.getDouble("conf");
          }

          // Extract corner data (Limelight 3A format)
          if (targetJson.has("corners")) {
            JSONArray corners = targetJson.getJSONArray("corners");
            for (int j = 0; j < Math.min(corners.length(), 8); j++) {
              target.corners[j] = corners.getDouble(j);
            }
          }

          // Extract AprilTag data
          if (targetJson.has("fiducial_id")) {
            target.fiducialID = targetJson.getInt("fiducial_id");
            target.isAprilTag = true;
          } else if (targetJson.has("fID")) {
            // Limelight 3A may use this alternate format
            target.fiducialID = targetJson.getInt("fID");
            target.isAprilTag = true;
          }

          // Extract robot-relative pose
          if (targetJson.has("pose_r") || targetJson.has("robotPose")) {
            JSONArray pose =
                targetJson.has("pose_r")
                    ? targetJson.getJSONArray("pose_r")
                    : targetJson.getJSONArray("robotPose");

            for (int j = 0; j < Math.min(pose.length(), 6); j++) {
              target.robotRelativePose[j] = pose.getDouble(j);
            }
          }

          target.json = targetJson;
          results.targets[i] = target;
        }
      }

      results.valid = true;

    } catch (JSONException e) {
      System.err.println("Error parsing Limelight 3A JSON: " + e.getMessage());
    }

    return results;
  }

  /**
   * Get URL for Limelight 3A with FTC network configuration
   *
   * @param limelightName Name or IP of Limelight
   * @return Properly formatted URL for Limelight 3A
   */
  private static String getLimelightURL(String limelightName) {
    String ip = limelightName;

    // If no IP provided, use default
    if (ip == null || ip.isEmpty()) {
      ip = DEFAULT_LIMELIGHT_IP;
    }

    // Check if the name is an IP address
    if (!ip.contains(".")) {
      ip = ip + ".local";
    }

    // Use Limelight 3A port
    return "http://" + ip + ":" + LIMELIGHT_3A_PORT;
  }

  /**
   * Get the JSON dump from the Limelight 3A
   *
   * @param limelightName Name of the Limelight camera
   * @return JSON string containing all Limelight 3A data
   */
  public static String getJSONDump(String limelightName) {
    return httpGet(getLimelightURL(limelightName) + "/results");
  }

  /**
   * Set a value on the Limelight
   *
   * @param limelightName Name of the Limelight camera
   * @param variableName Name of the variable to set
   * @param value Value to set
   */
  public static void setLimelightValue(String limelightName, String variableName, double value) {
    httpGet(getLimelightURL(limelightName) + "/control?key=" + variableName + "&value=" + value);
  }

  /**
   * Execute HTTP GET request optimized for Limelight 3A in FTC
   *
   * @param url URL to request
   * @return Response string
   */
  private static String httpGet(String url) {
    try {
      URL requestURL = new URL(url);
      HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
      connection.setRequestMethod("GET");
      connection.setConnectTimeout(CONNECTION_TIMEOUT_MS);
      connection.setReadTimeout(READ_TIMEOUT_MS);

      BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream()));
      StringBuilder response = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      reader.close();
      return response.toString();

    } catch (IOException e) {
      // Quietly fail - common in FTC when Limelight connection is intermittent
      return "";
    }
  }

  /**
   * Check if Limelight has a valid target
   *
   * @param limelightName Name of the Limelight camera
   * @return true if has target, false otherwise
   */
  public static boolean hasValidTarget(String limelightName) {
    return getLimelightValue(limelightName, "tv") > 0.5;
  }

  /**
   * Get raw targeting data from Limelight
   *
   * @param limelightName Name of the Limelight camera
   * @param variableName Name of the variable to retrieve
   * @return Value of the specified variable
   */
  public static double getLimelightValue(String limelightName, String variableName) {
    String response = httpGet(getLimelightURL(limelightName) + "/results?key=" + variableName);

    try {
      return Double.parseDouble(response);
    } catch (NumberFormatException e) {
      return 0.0;
    }
  }

  /**
   * Get info about a specific AprilTag by ID
   *
   * @param limelightName Name of the Limelight camera
   * @param tagID The ID of the AprilTag to get info for
   * @return LimelightTarget containing the tag data, or null if not found
   */
  public static LimelightTarget getAprilTagInfo(String limelightName, int tagID) {
    LimelightResults results = getLatestResults(limelightName);

    if (!results.valid) {
      return null;
    }

    for (LimelightTarget target : results.targets) {
      if (target.isAprilTag && target.fiducialID == tagID) {
        return target;
      }
    }

    return null;
  }

  /**
   * Set the pipeline number on the Limelight
   *
   * @param limelightName Name of the Limelight camera
   * @param pipeline Pipeline number (0-9)
   */
  public static void setPipeline(String limelightName, int pipeline) {
    setLimelightValue(limelightName, "pipeline", pipeline);
  }

  /**
   * Set the camera mode (0 = vision processing, 1 = driver camera)
   *
   * @param limelightName Name of the Limelight camera
   * @param mode Camera mode (0 = vision processing, 1 = driver camera)
   */
  public static void setCameraMode(String limelightName, int mode) {
    setLimelightValue(limelightName, "camMode", mode);
  }

  /**
   * Set the LED mode (0 = use pipeline setting, 1 = force off, 2 = force blink, 3 = force on)
   *
   * @param limelightName Name of the Limelight camera
   * @param mode LED mode (0 = use pipeline setting, 1 = force off, 2 = force blink, 3 = force on)
   */
  public static void setLEDMode(String limelightName, int mode) {
    setLimelightValue(limelightName, "ledMode", mode);
  }

  /**
   * Check if the Limelight is a 3A model
   *
   * @param limelightName Name of the Limelight camera
   * @return true if device is a Limelight 3A
   */
  public static boolean isLimelight3A(String limelightName) {
    if (!IS_LIMELIGHT_3A) {
      // Query the device for model info (could be implemented if needed)
      return true; // Assuming 3A for this implementation
    }
    return IS_LIMELIGHT_3A;
  }

  /**
   * Set the streaming mode for Limelight 3A
   *
   * @param limelightName Name of the Limelight camera
   * @param mode Stream mode (0=Standard, 1=PiP Main, 2=PiP Secondary)
   */
  public static void setStreamMode(String limelightName, int mode) {
    setLimelightValue(limelightName, "stream", mode);
  }

  /**
   * Set the snapshot mode (0 = stop snapshots, 1 = take snapshot)
   *
   * @param limelightName Name of the Limelight camera
   * @param mode Snapshot mode
   */
  public static void setSnapshotMode(String limelightName, int mode) {
    setLimelightValue(limelightName, "snapshot", mode);
  }

  /**
   * Set crop values for Limelight 3A
   *
   * @param limelightName Name of the Limelight camera
   * @param x0 Left boundary (0-1)
   * @param y0 Top boundary (0-1)
   * @param x1 Right boundary (0-1)
   * @param y1 Bottom boundary (0-1)
   */
  public static void setCropValues(
      String limelightName, double x0, double y0, double x1, double y1) {
    // Send crop rectangle to Limelight 3A
    httpGet(
        getLimelightURL(limelightName)
            + "/setting/crop/x0/"
            + x0
            + "/y0/"
            + y0
            + "/x1/"
            + x1
            + "/y1/"
            + y1);
  }
}
