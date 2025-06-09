import cv2
import numpy as np

def get_dominant_color(mask_yellow, mask_blue, mask_red):
    yellow_pixels = np.sum(mask_yellow)
    blue_pixels = np.sum(mask_blue)
    red_pixels = np.sum(mask_red)
    max_pixels = max(yellow_pixels, blue_pixels, red_pixels)
    if max_pixels == yellow_pixels:
        return "YELLOW"
    elif max_pixels == blue_pixels:
        return "BLUE"
    else:
        return "RED"

def calculate_angle(binary_image):
    height, width = binary_image.shape
    slices = 20
    slice_width = width // slices
    center_mass = 0
    total_mass = 0
    for i in range(slices):
        slice_sum = np.sum(binary_image[:, i*slice_width:(i+1)*slice_width])
        center_mass += slice_sum * (i + 0.5)
        total_mass += slice_sum
    if total_mass == 0:
        return 0
    center_x = (center_mass / total_mass) * slice_width
    angle = np.arctan2(center_x - width/2, height) * 180 / np.pi
    return angle

def runPipeline(image, llrobot):
    # Convert to HSV
    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    # Define color ranges
    lower_yellow = np.array([20, 100, 100])
    upper_yellow = np.array([30, 255, 255])
    lower_blue = np.array([100, 100, 100])
    upper_blue = np.array([130, 255, 255])
    lower_red1 = np.array([0, 100, 100])
    upper_red1 = np.array([10, 255, 255])
    lower_red2 = np.array([160, 100, 100])
    upper_red2 = np.array([180, 255, 255])

    # Create masks
    mask_yellow = cv2.inRange(hsv, lower_yellow, upper_yellow)
    mask_blue = cv2.inRange(hsv, lower_blue, upper_blue)
    mask_red1 = cv2.inRange(hsv, lower_red1, upper_red1)
    mask_red2 = cv2.inRange(hsv, lower_red2, upper_red2)
    mask_red = cv2.bitwise_or(mask_red1, mask_red2)

    # Combine masks
    mask_combined = cv2.bitwise_or(mask_yellow, mask_blue)
    mask_combined = cv2.bitwise_or(mask_combined, mask_red)

    # Apply a small blur to reduce noise
    mask_combined = cv2.GaussianBlur(mask_combined, (5, 5), 0)

    # Calculate angle
    angle = calculate_angle(mask_combined)

    # Determine dominant color
    color = get_dominant_color(mask_yellow, mask_blue, mask_red)

    # Visualize the result
    result = image.copy()
    cv2.putText(result, f"Color: {color}", (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)
    cv2.putText(result, f"Angle: {angle:.2f}", (10, 70), cv2.FONT_HERSHEY_SIMPLEX, 1, (255, 255, 255), 2)

    # Draw a line indicating the calculated angle
    center_y = result.shape[0] // 2
    center_x = result.shape[1] // 2
    end_x = int(center_x + 100 * np.sin(np.radians(angle)))
    end_y = int(center_y - 100 * np.cos(np.radians(angle)))
    cv2.line(result, (center_x, center_y), (end_x, end_y), (0, 255, 0), 2)

    # Create llpython output
    llpython = [0] * 8
    llpython[0] = angle

    return np.array([[]]), result, llpython