package lib.kalu.sox;

import android.util.Log;

import androidx.annotation.FloatRange;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;

import java.io.File;

@Keep
public final class Sox {

    static {
        System.loadLibrary("sox-jni");
    }

    private static String analysisProf(@NonNull String fromPath) {
        File file = new File(fromPath);
        if (!file.exists())
            return null;
        String parent = file.getParent();
        String name = file.getName();
        int index = name.indexOf(".");
        if (index > 0) {
            name = name.substring(0, index);
        }
        String temp = parent + "/" + name + ".prof";
        Log.e("Sox", "analysisProf => fromPath = " + fromPath);
        Log.e("Sox", "analysisProf => prof = " + temp);
        File prof = new File(temp);
        if (prof.exists()) {
            prof.delete();
        }
        try {
            prof.createNewFile();
            String cmd = "sox " + fromPath + " -n noiseprof " + temp;
            Log.e("Sox", "analysisProf => cmd = " + cmd);
            int i = excuate(cmd);
            if (i == 0) {
                return temp;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean noisered(@NonNull String fromPath, @NonNull String toPath) {
        return noisered(fromPath, toPath, 0.21F);
    }

    public static boolean noisered(@NonNull String fromPath, @NonNull String toPath, @FloatRange(from = 0.2F, to = 0.3F) float v) {
        String profPath = analysisProf(fromPath);
        if (null == profPath || profPath.length() <= 0)
            return false;
        Log.e("Sox", "noisered => toPath = " + toPath);
        File file = new File(toPath);
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            String cmd = "sox " + fromPath + " " + toPath + " noisered " + profPath + " " + v;
            Log.e("Sox", "noisered => cmd = " + cmd);
            int i = excuate(cmd);
            File prof = new File(profPath);
            if (prof.exists()) {
                prof.delete();
            }
            if (i == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public native static int excuate(String cmd);
}
