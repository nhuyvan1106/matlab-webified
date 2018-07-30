package remote;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

public class TaskResult {
    private final Object[] result;

    public TaskResult(Object[] result) {
        this.result = result;
    }

    public String getString(int index) {
        if (index < 1 || index > result.length)
            throw new IllegalArgumentException(
                    "Index to retrieve task result must be between 1 and " + result.length + ". Got " + index
                            + " instead");
        return ((MWNumericArray) result[0]).get(index).toString();
    }

    public int getInteger(int index) {
        if (index < 1 || index > result.length)
            throw new IllegalArgumentException(
                    "Index to retrieve task result must be between 1 and " + result.length + ". Got " + index
                            + " instead");
        return ((MWNumericArray) result[0]).getInt(index);
    }

    public double getDouble(int index) {
        if (index < 1 || index > result.length)
            throw new IllegalArgumentException(
                    "Index to retrieve task result must be between 1 and " + result.length + ". Got " + index
                            + " instead");
        return ((MWNumericArray) result[0]).getDouble(index);
    }

    public void dispose() {
        if (result != null)
            MWArray.disposeArray(result);
    }
}
