/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.richie.utils;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;

import java.util.ArrayList;
import java.util.List;

/**
 * Camera-related utility functions.
 */
public class CameraUtils {
    public static final int CAMERA_MODE_16_9 = 1;
    public static final int CAMERA_MODE_4_3 = 2;
    public static final int CAMERA_MODE_1_1 = 3;
    public static final int CAMERA_16_9_WIDTH = 1280;
    public static final int CAMERA_16_9_HEIGHT = 720;
    public static final int CAMERA_4_3_WIDTH = 1440;
    public static final int CAMERA_4_3_HEIGHT = 1080;
    private static final String TAG = CameraUtils.class.getSimpleName();
    private static final boolean DEBUG = false;

    public static int getCameraOrientation(int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        return info.orientation;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    /**
     * 设置对焦，会影响camera吞吐速率
     */
    public static void setFocusModes(Camera.Parameters parameters) {
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
    }

    /**
     * 设置fps
     */
    public static void chooseFramerate(Camera.Parameters parameters, float frameRate) {
        int framerate = (int) (frameRate * 1000);
        List<int[]> rates = parameters.getSupportedPreviewFpsRange();
        int[] bestFramerate = rates.get(0);
        for (int i = 0; i < rates.size(); i++) {
            int[] rate = rates.get(i);
            if (DEBUG)
                Log.e(TAG, "supported preview pfs min " + rate[0] + " max " + rate[1]);
            int curDelta = Math.abs(rate[1] - framerate);
            int bestDelta = Math.abs(bestFramerate[1] - framerate);
            if (curDelta < bestDelta) {
                bestFramerate = rate;
            } else if (curDelta == bestDelta) {
                bestFramerate = bestFramerate[0] < rate[0] ? rate : bestFramerate;
            }
        }
        if (DEBUG)
            Log.e(TAG, "closet framerate min " + bestFramerate[0] + " max " + bestFramerate[1]);
        parameters.setPreviewFpsRange(bestFramerate[0], bestFramerate[1]);
    }

    public static int[] choosePreviewSize(Camera.Parameters parameters, int mode) {
        Camera.Size defaultSize = parameters.getPreferredPreviewSizeForVideo();
        for (Camera.Size s : parameters.getSupportedPreviewSizes()) {
            Log.i(TAG, "choosePreviewSize Supported " + s.width + "x" + s.height);
            if (mode == CAMERA_MODE_16_9 && s.width == CAMERA_16_9_WIDTH && s.height == CAMERA_16_9_HEIGHT
                    || mode == CAMERA_MODE_4_3 && s.width == CAMERA_4_3_WIDTH && s.height == CAMERA_4_3_HEIGHT) {
                parameters.setPreviewSize(s.width, s.height);
                Log.i(TAG, "choosePreviewSize select " + s.width + "x" + s.height);
                return new int[]{s.width, s.height};
            }
        }
        for (Camera.Size s : parameters.getSupportedPreviewSizes()) {
            if (mode == CAMERA_MODE_16_9 && 1.0f * s.width / s.height == 16f / 9
                    || mode == CAMERA_MODE_4_3 && 1.0f * s.width / s.height == 4f / 3) {
                parameters.setPreviewSize(s.width, s.height);
                Log.i(TAG, "choosePreviewSize adaptive " + s.width + "x" + s.height);
                return new int[]{s.width, s.height};
            }
        }
        if (defaultSize != null) {
            parameters.setPreviewSize(defaultSize.width, defaultSize.height);
            Log.i(TAG, "choosePreviewSize failure " + defaultSize.width + "x" + defaultSize.height);
            return new int[]{defaultSize.width, defaultSize.height};
        }
        return new int[]{0, 0};
    }

    public static String openFlash(Camera camera, String flashMode) {
        try {
            if (camera == null || TextUtils.isEmpty(flashMode))
                return null;
            Camera.Parameters parameters = camera.getParameters();
            List<String> flashModes = parameters.getSupportedFlashModes();
            if (flashModes == null)
                return null;
            for (String fm : flashModes) {
                if (fm.equals(flashMode)) {
                    parameters.setFlashMode(flashMode);
                    camera.setParameters(parameters);
                    return flashMode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isSupportedFlash(Camera camera) {
        try {
            if (camera == null)
                return false;
            Camera.Parameters parameters = camera.getParameters();
            List<String> flashModes = parameters.getSupportedFlashModes();
            if (flashModes != null && flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void setExposureCompensation(Camera camera, float v) {
        if (camera == null)
            return;
        Camera.Parameters parameters = camera.getParameters();
        float min = parameters.getMinExposureCompensation();
        float max = parameters.getMaxExposureCompensation();
        parameters.setExposureCompensation((int) (v * (max - min) + min));
        camera.setParameters(parameters);
    }

    public static float getExposureCompensation(Camera camera) {
        if (camera == null)
            return 0;
        float progress = camera.getParameters().getExposureCompensation();
        float min = camera.getParameters().getMinExposureCompensation();
        float max = camera.getParameters().getMaxExposureCompensation();
        return (progress - min) / (max - min);
    }

    public static void handleFocus(Camera camera, float x, float y, int width, int height, int cameraWidth, int cameraHeight) {
        if (camera == null)
            return;
        try {
            Rect focusRect = calculateTapArea(x / width * cameraWidth, y / height * cameraHeight, cameraWidth, cameraHeight);
            camera.cancelAutoFocus();
            Camera.Parameters params = camera.getParameters();
            if (params.getMaxNumFocusAreas() > 0) {
                List<Camera.Area> focusAreas = new ArrayList<>();
                focusAreas.add(new Camera.Area(focusRect, 800));
                params.setFocusAreas(focusAreas);
            } else {
                Log.e(TAG, "focus areas not supported");
            }
            final String currentFocusMode = params.getFocusMode();

            List<String> supportedFocusModes = params.getSupportedFocusModes();
            if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_FIXED))
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
            else if (supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_MACRO))
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);

            camera.setParameters(params);
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    camera.cancelAutoFocus();
                    Camera.Parameters params = camera.getParameters();
                    params.setFocusMode(currentFocusMode);
                    camera.setParameters(params);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Rect calculateTapArea(float x, float y, int width, int height) {
        int areaSize = 150;
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int top = clamp(centerX - areaSize / 2);
        int bottom = clamp(top + areaSize);
        int left = clamp(centerY - areaSize / 2);
        int right = clamp(left + areaSize);
        RectF rectF = new RectF(left, top, right, bottom);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        matrix.mapRect(rectF);
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x) {
        return x > 1000 ? 1000 : (x < -1000 ? -1000 : x);
    }
}
