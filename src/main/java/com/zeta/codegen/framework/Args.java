package com.zeta.codegen.framework;
import com.zeta.util.UQueue;

public class Args {

    private String[] args;
    private boolean[] argProcessed;

    Args(String[] cmdArgs) {
        args = cmdArgs;
        argProcessed = new boolean[args.length];
    }

    /**
     * Processes the last argument, which should be the
     * input class name.
     */
    String getLast() {
        if (args[args.length - 1].charAt(0) == '-')
            return null;
        String returnValue = args[args.length - 1];
        argProcessed[args.length - 1] = true;
        return returnValue;
    }

    /**
     * Returns the mandatory parameter string that follows the flag.
     * The flag must begin with hyphen.  Once the flag is found and the
     * parameter is removed from the args, both the flag and the parameter
     * entries in the args array are set to null.
     */
    String getFlagValue(String flag) {
        for (int i = 0; i < args.length - 1; i++)
            if (!argProcessed[i] && !argProcessed[i + 1]
                    && args[i].equals(flag)
                    && args[i].charAt(0) == '-'
                    && args[i + 1].charAt(0) != '-') {
                String returnValue = args[i + 1];
                argProcessed[i] = true;
                argProcessed[i + 1] = true;
                return returnValue;
            }
        return null;
    }

    /**
     * If a flag is permitted to be repeated in a command line, getFlagValues returns a Vector
     * of all the mandatory parameters that follow the matching flag. All matching flags and
     * corresponding parameters in args are set to null after processing.
     */
    UQueue getFlagValues(String flag) {
        UQueue values = new UQueue(String.class);
        String value = getFlagValue(flag);
        while (value != null) {
            values.add(value);
            value = getFlagValue(flag);
        }
        return values;
    }

    /**
     * Returns true if the flag appears in the command line (args).
     * If found, sets the args entry of the flag to null.
     */
    boolean hasFlag(String flag) {
        for (int i = 0; i < args.length; i++)
            if (args[i] != null && args[i].equals(flag)
                    && args[i].charAt(0) == '-') {
                argProcessed[i] = true;
                return true;
            }
        return false;
    }

    /**
     * Returns true if the all entries of the args array are null.
     */
    boolean complete() {
        for (boolean anArgProcessed : argProcessed)
            if (!anArgProcessed)
                return false;
        return true;
    }

    /**
     * Converts all non-processed entries of args into a single string.
     */
    public String toString() {
        String result = "";
        for (int i = 0; i < args.length; i++)
            if (!argProcessed[i])
                result += args[i] + " ";
        return result;
    }
}
