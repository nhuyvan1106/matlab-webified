package remote.tasks;

import java.io.Serializable;

import com.mathworks.toolbox.javabuilder.MWException;

import add_two_numbers.Class1;
import remoteproxy.Task;

public class AddTwoNumbersTask extends Class1 implements Task, Serializable {

    private static final long serialVersionUID = 1L;
    private final int firstNumber;
    private final int secondNumber;

    public AddTwoNumbersTask(int first, int second) throws MWException {
        this.firstNumber = first;
        this.secondNumber = second;
    }

    @Override
    public Object[] doTask() {
        try {
            return add_two_numbers(1, firstNumber, secondNumber);
        } catch (MWException e) {
            return null;
        }
    }
}