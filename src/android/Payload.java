/*
 The MIT License (MIT)

 Copyright (c) 2017 Nedim Cholich

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all
 copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 SOFTWARE.
 */
package co.frontyard.cordova.plugin.exoplayer;

import android.view.*;
import android.util.Log;
import com.google.android.exoplayer2.*;
import java.lang.*;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.StackTraceElement;
import java.lang.StringBuffer;
import java.util.*;
import org.json.*;

public class Payload {

    private static String playbackStateToString(int playbackState) {
        String state = "UNKNOWN";
        switch (playbackState) {
            case ExoPlayer.STATE_IDLE:
                state = "STATE_IDLE";
                break;
            case ExoPlayer.STATE_BUFFERING:
                state = "STATE_BUFFERING";
                break;
            case ExoPlayer.STATE_READY:
                state = "STATE_READY";
                break;
            case ExoPlayer.STATE_ENDED:
                state = "STATE_ENDED";
                break;
        }
        return state;
    }

    public static JSONObject startEvent(ExoPlayer player, String audioFocus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "START_EVENT");
        map.put("audioFocus", audioFocus);
        addPlayerState(map, player);
        return new JSONObject(map);
    }

    public static JSONObject stopEvent(ExoPlayer player) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "STOP_EVENT");
        return new JSONObject(map);
    }

    public static JSONObject keyEvent(KeyEvent event) {
        int eventAction = event.getAction();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "KEY_EVENT");
        map.put("eventAction", eventAction == KeyEvent.ACTION_DOWN ? "ACTION_DOWN" : eventAction == KeyEvent.ACTION_UP ? "ACTION_UP" : "" + eventAction);
        map.put("eventKeycode", new Integer (event.getKeyCode()));
        return new JSONObject(map);
    }

    public static JSONObject touchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "TOUCH_EVENT");
        map.put("eventAction", eventAction == MotionEvent.ACTION_DOWN ? "ACTION_DOWN" : eventAction == MotionEvent.ACTION_UP ? "ACTION_UP" : eventAction == MotionEvent.ACTION_MOVE ? "ACTION_MOVE" : "" + eventAction);
        map.put("eventAxisX", new Float(event.getX()));
        map.put("eventAxisY", new Float(event.getY()));
        return new JSONObject(map);
    }

    public static JSONObject loadingEvent(ExoPlayer player, boolean loading) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "LOADING_EVENT");
        map.put("loading", new Boolean(loading));
        addPlayerState(map, player);
        return new JSONObject(map);
    }

    public static JSONObject stateEvent(ExoPlayer player, int playbackState, boolean controllerVisible) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "STATE_CHANGED_EVENT");
        addPlayerState(map, player);
        map.put("playbackState", playbackStateToString(playbackState));
        map.put("controllerVisible", new Boolean(controllerVisible));
        return new JSONObject(map);
    }

    public static JSONObject positionDiscontinuityEvent(ExoPlayer player) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "POSITION_DISCONTINUITY_EVENT");
        addPlayerState(map, player);
        return new JSONObject(map);
    }

    public static JSONObject seekEvent(ExoPlayer player, long offset) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "SEEK_EVENT");
        map.put("offset", new Long(offset));
        addPlayerState(map, player);
        return new JSONObject(map);
    }

    public static JSONObject audioFocusEvent(ExoPlayer player, String state) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("eventType", "AUDIO_FOCUS_EVENT");
        map.put("audioFocus", state);
        addPlayerState(map, player);
        return new JSONObject(map);
    }

    private static String getStackTrace(Throwable error) {
            StringBuffer stackTrace = new StringBuffer();
            if (null != error) {
                StackTraceElement[] st = error.getStackTrace();
                if (null != st && st.length > 0) {
                    for (int i = 0; i < st.length; i++) {
                        StackTraceElement elem = st[i];
                        stackTrace.append(elem.getClassName() + "#" + elem.getMethodName() + "@" + elem.getLineNumber() + (elem.isNativeMethod() ? " NATIVE" : "")).append("\n");
                    }
                }
            }
	    return stackTrace.toString();
    }


    public static JSONObject playerErrorEvent(ExoPlayer player, ExoPlaybackException origin, String message) {
        int type = 0;
        JSONObject map = new JSONObject();
	try {
            map.put("eventType", "PLAYER_ERROR_EVENT");

            if (null != origin) {
	        JSONArray stacks = new JSONArray();
                type = origin.type;
                Throwable error = (Throwable) origin;
	        do { 
                    JSONObject stack = new JSONObject();
                    stack.put("stackTrace", getStackTrace(error));
                    stack.put("errorMessage", error.getMessage());
		    stacks.put(stack);

                    error = error.getCause();
                } while (null != error);
                map.put("stacks", stacks);
            }
            if (null != message) {
                map.put("customMessage", message);
            }

            switch (type) {
                case ExoPlaybackException.TYPE_RENDERER:
                    map.put("errorType", "RENDERER");
                    break;
                case ExoPlaybackException.TYPE_SOURCE:
                    map.put("errorType", "SOURCE");
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    map.put("errorType", "UNEXPECTED");
                    break;
                default:
                    map.put("errorType", "UNKNOWN");
                    break;
            }
	} catch (JSONException e) {
        }

        return map;
    }

    private static void addPlayerState(Map<String, Object> map, ExoPlayer player) {
        if (null != player) {
            map.put("duration", new Long(player.getDuration()));
            map.put("position", new Long(player.getCurrentPosition()));
            map.put("playWhenReady", new Boolean(player.getPlayWhenReady()));
            map.put("playbackState", playbackStateToString(player.getPlaybackState()));
            map.put("bufferPercentage", new Integer(player.getBufferedPercentage()));
        }
    }
}
